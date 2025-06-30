package com.samsthenerd.monthofswords.utils;

import com.samsthenerd.monthofswords.registry.SwordsModLoot;
import com.samsthenerd.monthofswords.utils.Description.AcquisitionDesc.LootDropDesc;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.Item;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

// for now just assume this stuff is only called on the client ig?
public record Description(RegistrySupplier<? extends Item> item, List<SwordPower> powers, List<AcquisitionDesc> acqDescs,
                          int textColor){


    public List<Text> getSummaryTooltip(){
        var briefTexts = getContinuousText(makeLangPatternProvider(item.get().getTranslationKey() + ".brief", List.of()))
            .stream().map(t -> applyColor(t).formatted(Formatting.ITALIC)).toList();
        var powerTexts = getPowerTooltip();
//        List<Text> resTT = new ArrayList<>(briefTexts);
        List<Text> resTT = new ArrayList<>();
//        if(powerTexts.size() > 3){
            resTT.addAll(powers.stream().map(p -> p.getPowerTitle(this)).toList());
            resTT.add(Text.literal(""));
            resTT.add(applyColor(Text.translatable("monthofswords.tooltip.shiftpowers")).formatted(Formatting.ITALIC));
//        } else {
//            resTT.addAll(powerTexts);
//        }
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
            acqTTs.addAll(acqd.getAcqTooltip());
            acqTTs.add(Text.literal(""));
        }
        return acqTTs;
    }

    public static Description forItem(RegistrySupplier<? extends Item> item){
        return new Description(item, new ArrayList<>(), null, 0xFFFFFF);
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
                    String lootId = tc.getLeft().getValue().toString();
                    Text tableText = Text.literal(lootId);
                    t.append(Text.translatable("monthofswords.descriptionutil.acq.loot", tc.getRight(), tableText));
                    tt.add(t);
                }
                return tt;
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
}
