package com.samsthenerd.monthofswords.utils;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.registry.SwordsModLoot;
import com.samsthenerd.monthofswords.tooltips.RecipeTooltipData;
import com.samsthenerd.monthofswords.utils.Description.AcquisitionDesc.CraftingDesc;
import com.samsthenerd.monthofswords.utils.Description.AcquisitionDesc.LootDropDesc;
import com.samsthenerd.monthofswords.utils.Description.AcquisitionDesc.SpecificText;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.loot.LootTable;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

// for now just assume this stuff is only called on the client ig?
public record Description(RegistrySupplier<? extends Item> item, List<SwordPower> powers, List<AcquisitionDesc> acqDescs,
                          int textColor){


    public List<Text> getSummaryTooltip(){
//        var briefTexts = getContinuousText(makeLangPatternProvider(item.get().getTranslationKey() + ".brief", List.of()))
//            .stream().map(t -> applyColor(t).formatted(Formatting.ITALIC)).toList();
        List<Text> resTT = new ArrayList<>();
            resTT.addAll(powers.stream().map(p -> p.getPowerTitle(this)).toList());
            if(!resTT.isEmpty()){
                resTT.add(Text.literal(""));
                resTT.add(applyColor(Text.translatable("monthofswords.tooltip.shiftpowers")).formatted(Formatting.ITALIC));
            }
        return resTT;
    }

    public List<Text> getPowerTooltip(){
        List<Text> powTTs = new ArrayList<>();
        for(var pow : powers){
            powTTs.addAll(pow.getPowerTooltip(this));
            powTTs.add(Text.literal(""));
        }
        return powTTs;
    }

    public List<Text> getAcquisitionTooltip(){
        List<Text> acqTTs = new ArrayList<>();
        for(var acqd : acqDescs){
            acqTTs.addAll(acqd.getAcqTooltip().stream().map(this::applyColor).toList());
            acqTTs.add(Text.literal(""));
        }
        return acqTTs;
    }

    public List<Text> getTooltipFull(ItemStack stack, TooltipContext context, TooltipType type){
        var descData = stack.get(SwordsModComponents.ITEM_DESCRIPTION_DATA);
        if(descData == null){
            // normal !
            if (hasShiftSafe()) {
                return getPowerTooltip();
            } else {
                return getSummaryTooltip();
            }
        } else {
            // in calendar
            List<Text> tt = new ArrayList<>();
            if(descData.hintMode){
                tt.addAll(getAcquisitionTooltip());
                tt.add(applyColor(Text.translatable("monthofswords.descriptionutil.switchtopower")).formatted(Formatting.ITALIC));
            } else {
                if (hasShiftSafe()) {
                    tt.addAll(getPowerTooltip());
                } else {
                    tt.addAll(getSummaryTooltip());
                }
                tt.add(Text.literal(""));
                tt.add(applyColor(Text.translatable("monthofswords.descriptionutil.switchtohint")).formatted(Formatting.ITALIC));
            }
            return tt;
        }
    }

    public static Description forItem(RegistrySupplier<? extends Item> item){
        return new Description(item, new ArrayList<>(), new ArrayList<>(), 0xFFFFFF);
    }

    public Description withPower(SwordPower... power){
        List<SwordPower> newPowers = new ArrayList<>(powers);
        newPowers.addAll(Arrays.asList(power));
        return new Description(item, newPowers, acqDescs, textColor);
    }

    public Description withAcquisitionDesc(AcquisitionDesc... descs){
        List<AcquisitionDesc> newAcqs = new ArrayList<>(acqDescs);
        newAcqs.addAll(Arrays.asList(descs));
        return new Description(item, powers, newAcqs, textColor);
    }

    public Description withLootAcqDesc(){
        return withAcquisitionDesc(new LootDropDesc(SwordsModLoot.LOOT_LISTS.get(item.getId()).stream().toList()));
    }

    public Description withMatchingRecipe(){
        return withAcquisitionDesc(new CraftingDesc(item.getId()));
    }

    public Description withSpecificAcqDesc(){
        return withAcquisitionDesc(new SpecificText(item.getId().toTranslationKey()));
    }

    public Description withTextColor(int color){
        return new Description(item, powers, acqDescs, color);
    }

    public Description withTextColor(Formatting fm){
        return new Description(item, powers, acqDescs, fm.isColor() ? fm.getColorValue() : textColor);
    }

    public Description finalize(Consumer<Description> consumer){
        consumer.accept(this);
        return this;
    }

    // most condensed way to make it show only when create is installed lol
    public Description conditionally(UnaryOperator<Description> op, boolean condition){
        return condition ? op.apply(this) : this;
    }

    public MutableText applyColor(Text t){
        return t.copy().withColor(textColor);
    }

    /**
     * clientside helper for getting a sequence of translation entries whose length is determined by the available
     * translations, with support for ranked order key choice.
     *
     * The sequence will end when no lang keys have translations or any of them are translated as just "/endseq/"
     *
     * This is mostly overengineered for the purpose of modpack devs being able to change stuff if they want,
     * will that ever happen? prob not
     *
     * @param langPatterns a function that takes an integer and returns a ranked list of possible lang keys
     * @param args args to provide to each line of text
     * @return the sequence of texts
     */
    public static List<Text> getContinuousText(Function<Integer, List<String>> langPatterns, Object... args){
        int i = 0;
        boolean keepGoing = true;
        List<Text> ts = new ArrayList<>();
        while(keepGoing){
            var keys = langPatterns.apply(i);
            keepGoing = false;
            for(String k : keys){
                if(!I18n.hasTranslation(k)){
                    continue;
                }
                String str = I18n.translate(k, args);
                if(str.equals("/endseq/")) break;
                ts.add(Text.translatable(k, args));
                keepGoing = true;
                break;
            }
            i++;
        }
        return ts;
    }

    public static Function<Integer, List<String>> makeLangPatternProvider(String base, List<String> flags){
        return i -> {
            List<String> keys = new ArrayList<>();
            for(String f : flags){
                if(i == 0) keys.add(base + "." + f);
                keys.add(base + "." + i + "." + f);
            }
            if(i == 0) keys.add(base);
            keys.add(base + "." + i);
            return keys;
        };
    }

    public record SwordPower(String name, Text powerType, Optional<Integer> cooldown){
        public List<Text> getPowerTooltip(Description desc){
            String powerTitleKey = desc.item.get().getTranslationKey() + ".power." + name;
            Text fullPowerTitle = getPowerTitle(desc);
            List<Text> powerText = new ArrayList<>();
            powerText.add(fullPowerTitle);
            var powerDescs = getContinuousText(makeLangPatternProvider(powerTitleKey + ".desc", List.of()));
            cooldown.ifPresent(cd -> powerDescs.add(Text.translatable("monthofswords.descriptionutil.powercooldown", Description.getFormattedTime(cd))));
            powerText.addAll(
                powerDescs.stream().map(
                    t -> Text.literal(" ")
                        .append(t)
                        .formatted(Formatting.ITALIC)
                        .withColor(desc.textColor))
                .toList()
            );
            return powerText;
        }

        public Text getPowerTitle(Description desc){
            String powerTitleKey = desc.item.get().getTranslationKey() + ".power." + name;
            var powerTitle = Text.translatableWithFallback(powerTitleKey, "");
            Text fullPowerTitle;
            if(I18n.hasTranslation(powerTitleKey)){
                fullPowerTitle = Text.translatable("monthofswords.descriptionutil.powertitleformat", powerTitle, powerType);
            } else {
                fullPowerTitle = powerType;
            }
            return fullPowerTitle.copy().formatted(Formatting.BOLD, Formatting.UNDERLINE).withColor(desc.textColor());
        }
    }

    public static final Text PASSIVE_POWER = Text.translatable("monthofswords.descriptionutil.powertype.passivepower");
    public static final Text HIT_POWER = Text.translatable("monthofswords.descriptionutil.powertype.hitpower");
    public static final Text USE_POWER = Text.translatable("monthofswords.descriptionutil.powertype.usepower");
    public static final Text CHARGE_USE_POWER = Text.translatable("monthofswords.descriptionutil.powertype.chargeusepower");
    public static final Text HOLD_POWER = Text.translatable("monthofswords.descriptionutil.powertype.holdusepower");
    public static final Text ACTION_POWER = Text.translatable("monthofswords.descriptionutil.powertype.actionpower", Text.keybind("key.swordsmod.action"));
    public static final Text SWING_POWER = Text.translatable("monthofswords.descriptionutil.powertype.swingpower");
    public static final Text INV_USE = Text.translatable("monthofswords.descriptionutil.powertype.invusepower");


    public interface AcquisitionDesc {

        List<Text> getAcqTooltip();

        record LootDropDesc(List<Pair<RegistryKey<LootTable>, Float>> tableChances) implements AcquisitionDesc{
            @Override
            public List<Text> getAcqTooltip() {
                List<Text> tt = new ArrayList<>();
                tt.add(Text.translatable("monthofswords.descriptionutil.acq.title.loot"));
                for(var tc : tableChances){
                    MutableText t = Text.literal(" ");
                    Text tableText = getLootTableName(tc.getLeft());
                    float roundedVal = Math.round(tc.getRight()*10000)/100f;
                    t.append(Text.translatable("monthofswords.descriptionutil.acq.loot", roundedVal, tableText));
                    tt.add(t);
                }
                return tt;
            }

            // idk why EMI loot has their stuff prefixed like this
            public static Map<String, String> EMI_LOOT_TYPE_LOOKUP = new HashMap<>(Map.of(
                "chests", "chest",
                "spawners", "chest",
                "dispensers", "chest"
            ));

            public static Text getLootTableName(RegistryKey<LootTable> lootTableKey){
                Identifier lootId = lootTableKey.getValue();
                String sensibleKey = "loot." + lootId.getNamespace() + "." + lootId.getPath();
                // descriptions could be just sensibleKey with .desc or .description suffix ?
                if(I18n.hasTranslation(sensibleKey)){
                    return Text.translatable(sensibleKey);
                }
                String lootType = lootId.getPath().split("/")[0];
                String lootTypeEMI = EMI_LOOT_TYPE_LOOKUP.getOrDefault(lootType, lootType);
                String emiKey = "emi_loot." + lootTypeEMI + "." + lootId;
                if(I18n.hasTranslation(emiKey)){
                    return Text.translatable(emiKey);
                }
                return Text.literal(lootId.toString());
            }
        }

        record CraftingDesc(Identifier recId) implements AcquisitionDesc{
            @Override
            public List<Text> getAcqTooltip() {
                return List.of();
            }
        }

        record SpecificText(String baseItemKey) implements AcquisitionDesc{
            @Override
            public List<Text> getAcqTooltip() {
                return getContinuousText(makeLangPatternProvider( baseItemKey + ".acquisition", List.of()));
            }
        }
    }

    public static int[] TIME_HIERARCHY = {24 * 60 * 60 * 20, 60*60*20, 60*20, 20};
    public static String[] TIME_HIERARCHY_LABELS = {"day", "hour", "min", "sec"};

    public static Text getFormattedTime(int tickCount){
//        List<Integer> times = new ArrayList<>();
        MutableText timeText = Text.empty();
        boolean modified = false;
        for(int i = 0; i < 3; i++){
//            times.add(timeUnit);
            int timeUnit = tickCount / TIME_HIERARCHY[i];
            if(timeUnit != 0){
                timeText.append(
                    Text.translatable("monthofswords.descriptionutil.timelabel." + TIME_HIERARCHY_LABELS[i],
                        timeUnit));
                modified = true;
            }

            tickCount %= TIME_HIERARCHY[i];
        }
        double secs = tickCount / 20.0;
        if(secs != 0 || !modified){
            timeText.append(Text.translatable("monthofswords.descriptionutil.timelabel.sec", secs));
        }
        return timeText;
    }

    public record DescriptionItemComponent(boolean hintMode, Optional<RecipeTooltipData> ttData){
        public static final Codec<DescriptionItemComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Codec.BOOL.fieldOf("hintMode").forGetter(DescriptionItemComponent::hintMode),
                RecipeTooltipData.CODEC.optionalFieldOf("ttData").forGetter(DescriptionItemComponent::ttData)
            ).apply(instance, DescriptionItemComponent::new)
        );

        public static final PacketCodec<RegistryByteBuf, DescriptionItemComponent> PACKET_CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL, DescriptionItemComponent::hintMode,
            RecipeTooltipData.PACKET_CODEC.collect(PacketCodecs::optional), DescriptionItemComponent::ttData,
            DescriptionItemComponent::new
        );
    }

    // returns true if shift is down or if it's on the server
    // meant to be used for tooltips to not break polydex
    public static boolean hasShiftSafe(){
        return Platform.getEnvironment() == Env.SERVER || Screen.hasShiftDown();
    }
}
