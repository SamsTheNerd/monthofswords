package com.samsthenerd.monthofswords.utils;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.registry.SwordsModLoot;
import com.samsthenerd.monthofswords.utils.Description.AcquisitionDesc.LootDropDesc;
import com.samsthenerd.monthofswords.utils.Description.DescriptionItemComponent;
import com.samsthenerd.monthofswords.utils.Description.SwordPower;
import dev.architectury.platform.Platform;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ItemDescriptions {

    private static final Map<Identifier, Description> ITEM_DESCRIPTIONS = new HashMap<>();

    public static Optional<Description> getItemDescription(Item item){
        return Optional.ofNullable(ITEM_DESCRIPTIONS.get(Registries.ITEM.getId(item)));
    }

    public static Optional<Description> getItemDescriptionFromId(Identifier id){
        return Optional.ofNullable(ITEM_DESCRIPTIONS.get(id));
    }

    public static void addDescription(Description desc){
        ITEM_DESCRIPTIONS.put(desc.item().getId(), desc);
    }

    public static final Description WING_DESC = Description.forItem(SwordsModItems.WING_SWORD)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description FLORAL_DESC = Description.forItem(SwordsModItems.FLORAL_SWORD)
        .withPower(new SwordPower("photosynthesis", Description.PASSIVE_POWER, Optional.empty()))
        .withMatchingRecipe()
        .withTextColor(Formatting.GREEN)
        .finalize(ItemDescriptions::addDescription);


    public static final Description CURSED_DESC = Description.forItem(SwordsModItems.CURSED_SWORD)
        .withPower(new SwordPower("curse", Description.HIT_POWER, Optional.empty()))
        .withTextColor(Formatting.RED)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);


    public static final Description DIVINE_DESC = Description.forItem(SwordsModItems.DIVINE_SWORD)
        .withPower(new SwordPower("divinity", Description.ACTION_POWER, Optional.of(60*20)))
        .withTextColor(Formatting.YELLOW)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);


    public static final Description GUMMY_DESC = Description.forItem(SwordsModItems.GUMMY_SWORD)
        .withTextColor(Formatting.AQUA)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description STEALTH_DESC = Description.forItem(SwordsModItems.STEALTH_SWORD)
        .withPower(new SwordPower("smoke_bomb", Description.USE_POWER, Optional.of(20*30)))
        .withPower(new SwordPower("sneak_attack", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("set_firework", Description.INV_USE, Optional.empty()))
        .withTextColor(Formatting.RED)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description MOON_DESC = Description.forItem(SwordsModItems.MOON_SWORD)
        .withPower(new SwordPower("moonlit", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(Formatting.BLUE)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description SUN_DESC = Description.forItem(SwordsModItems.SUN_SWORD)
        .withPower(new SwordPower("sunlit", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(Formatting.YELLOW)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description OCEAN_DESC = Description.forItem(SwordsModItems.OCEAN_SWORD)
        .withPower(new SwordPower("oceanic", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(0x1dc2d1)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description ENCHANTING_DESC = Description.forItem(SwordsModItems.ENCHANTING_SWORD)
        .withPower(new SwordPower("reroll", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0x91f788)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description DUELING_DESC = Description.forItem(SwordsModItems.DUELING_SWORD)
        .withPower(new SwordPower("block", Description.HOLD_POWER, Optional.empty()))
        .withPower(new SwordPower("parry", Description.HIT_POWER, Optional.empty()))
        .withTextColor(Formatting.GOLD)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description FLAME_DESC = Description.forItem(SwordsModItems.FLAME_SWORD)
        .withPower(new SwordPower("flame_lick", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("scorch", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("fireball", Description.USE_POWER, Optional.of(50)))
        .withPower(new SwordPower("inferno", Description.ACTION_POWER, Optional.of(20*15)))
        .withTextColor(0xfc6f03)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description ICE_DESC = Description.forItem(SwordsModItems.ICE_SWORD)
        .withPower(new SwordPower("frostbite", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("freezing", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("snowball", Description.USE_POWER, Optional.of(30)))
        .withPower(new SwordPower("blizzard", Description.ACTION_POWER, Optional.empty()))
        .withTextColor(0x5eefff)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description POISON_DESC = Description.forItem(SwordsModItems.POISON_DAGGER)
        .withPower(new SwordPower("venom", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0x476e2c)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description POTION_DESC = Description.forItem(SwordsModItems.POTION_SWORD)
        .withPower(new SwordPower("brew", Description.USE_POWER, Optional.empty()))
        .withPower(new SwordPower("inject", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0x69adf0)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description BONE_DESC = Description.forItem(SwordsModItems.BONE_SWORD)
        .withTextColor(0xe8e5d2)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);


    public static final Description PORTAL_DESC = Description.forItem(SwordsModItems.PORTAL_SWORD)
        .withPower(new SwordPower("dimension_hop", Description.ACTION_POWER, Optional.empty()))
        .withTextColor(0xaf4fe3)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description LUCKY_DESC = Description.forItem(SwordsModItems.LUCKY_SWORD)
        .withPower(new SwordPower("lucky_dice", Description.HIT_POWER, Optional.empty()))
        .withTextColor(Formatting.GOLD)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description FOREST_DESC = Description.forItem(SwordsModItems.FOREST_SWORD)
        .withPower(new SwordPower("leaf_attack", Description.SWING_POWER, Optional.of(15)))
        .withTextColor(0xf7b9dc)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description ECHO_DESC = Description.forItem(SwordsModItems.ECHO_SWORD)
        .withPower(new SwordPower("displace", Description.CHARGE_USE_POWER, Optional.empty()))
        .withTextColor(0x1ecad4)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description EYE_DESC = Description.forItem(SwordsModItems.EYE_SWORD)
        .withPower(new SwordPower("void_taste", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("enderman_eye_protection", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(0x97d16b)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description MECHANICAL_DESC = Description.forItem(SwordsModItems.MECHANICAL_SWORD)
        .withTextColor(0xfdd47b)
        .conditionally(Description::withMatchingRecipe, Platform.isModLoaded("create"))
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description SUMMONED_DESC = Description.forItem(SwordsModItems.SUMMONED_SWORD)
        .withTextColor(0x7cd8f7)
        .withPower(new SwordPower("summon", Description.ACTION_POWER, Optional.empty()))
        .withPower(new SwordPower("banish", Description.ACTION_POWER, Optional.empty()))
        .withSpecificAcqDesc()
        .withAcquisitionDesc(new LootDropDesc(SwordsModLoot.LOOT_LISTS.get(SwordsModItems.SUMMON_FRUIT.getId()).stream().toList()))
        .finalize(ItemDescriptions::addDescription);

    public static final Description JEWELED_DESC = Description.forItem(SwordsModItems.JEWELED_SWORD)
        .withTextColor(Formatting.GOLD)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description WOVEN_DESC = Description.forItem(SwordsModItems.WOVEN_SWORD)
        .withPower(new SwordPower("dash", Description.USE_POWER, Optional.empty()))
        .withTextColor(0xfccbe7)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description SHOCK_DESC = Description.forItem(SwordsModItems.SHOCK_SWORD)
        .withPower(new SwordPower("summon_lightning", Description.USE_POWER, Optional.empty()))
        .withPower(new SwordPower("storm_damage", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(0xadebf7)
        .withLootAcqDesc()
        .finalize(ItemDescriptions::addDescription);

    public static final Description INFESTATION_DESC = Description.forItem(SwordsModItems.INFESTATION_SWORD)
        .withPower(new SwordPower("friend_of_bugs", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("bugbait", Description.CHARGE_USE_POWER, Optional.empty()))
        .withTextColor(0x8c9b8c)
        .withMatchingRecipe()
        .withAcquisitionDesc(new LootDropDesc(SwordsModLoot.LOOT_LISTS.get(SwordsModItems.SILVERFISH_SHELL.getId()).stream().toList()))
        .finalize(ItemDescriptions::addDescription);

    public static final Description CRYSTAL_DESC = Description.forItem(SwordsModItems.CRYSTAL_SWORD)
        .withPower(new SwordPower("resonance", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0xcfa0f3)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description NECROMANCER_DESC = Description.forItem(SwordsModItems.NECROMANCERS_SWORD)
        .withPower(new SwordPower("necromancer", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("raise_the_dead", Description.CHARGE_USE_POWER, Optional.empty()))
        .withTextColor(0xff637b)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description GLOW_DESC = Description.forItem(SwordsModItems.GLOW_SWORD)
        .withPower(new SwordPower("piercing_eyes", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("illuminance", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("dark_vision", Description.USE_POWER, Optional.of(20*15)))
        .withPower(new SwordPower("radiance", Description.ACTION_POWER, Optional.of(20*45)))
        .withTextColor(0x90f0d0)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);


    public static String printAllAsMarkdown(){
        StringBuilder res = new StringBuilder("## Swords\n");
        for(var itemId : SwordsModItems.ALL_SWORDS){
            Item swordItem = Registries.ITEM.get(itemId);
            ItemStack swordStack = swordItem.getDefaultStack();
            swordStack.set(SwordsModComponents.ITEM_DESCRIPTION_DATA, new DescriptionItemComponent(false, Optional.empty(), true));
            List<Text> tooltipText = swordStack.getTooltip(TooltipContext.DEFAULT, MinecraftClient.getInstance().player, TooltipType.BASIC);
            for(int i = 0; i < tooltipText.size(); i++){
                Text t = tooltipText.get(i);
                String lineStr = textToMarkdown(t);
                if(i == 0){
                    res.append("## ").append(lineStr).append("\n");
                } else if(lineStr.isEmpty()){
                    continue;
                } else if(lineStr.startsWith(" ") || lineStr.startsWith("+")) {
                    res.append("  - ").append(lineStr).append("\n");
                } else {
                    res.append("- ").append(lineStr).append("\n");
                }
            }
//            tooltipText.stream()
//                .map(ItemDescriptions::textToMarkdown)
//                .forEachOrdered(str -> res.append(str).append("\n"));

//            String swordName = I18n.translate(swordItem.getTranslationKey());
//            res.append("### ").append(swordName).append("\n");
//            var descOpt = getItemDescriptionFromId(itemId);
//            if(descOpt.isEmpty()) continue;
//            var desc = descOpt.get();
//            desc.getPowerTooltip().stream()
//                .map(ItemDescriptions::textToMarkdown)
//                .forEachOrdered(str -> res.append(str).append("\n"));
        }
        String resStr = res.toString();
        SwordsMod.LOGGER.info(resStr);
        MinecraftClient.getInstance().keyboard.setClipboard(resStr);
        return resStr;
    }

//    public record MDStyleState(Style style, boolean )

    public static String getStyleChangeDelim(Style oldStyle, Style newStlye){
        if(oldStyle.equals(newStlye)) return ""; // if identical styles do nothing
        // if no bold/italic change return nothing
        if(oldStyle.isBold() == newStlye.isBold() && oldStyle.isItalic() == newStlye.isItalic()){
            return "";
        }
        // if no italic change, then must have just a bold change
        if(oldStyle.isItalic() == newStlye.isItalic()){
            return "**";
        }
        // if no bold change, then must have just an italic change
        if(oldStyle.isBold() == newStlye.isBold()){
            return "*";
        }
        // change of both
        return "***";
    }

    public static String textToMarkdown(Text text){
        StringBuilder res = new StringBuilder();
        AtomicReference<Style> currentStyle = new AtomicReference<>(Style.EMPTY);
        AtomicReference<String> trailingWhitespace = new AtomicReference<>("");
        text.visit((sty, str) -> {
            String stripLead = str.stripLeading();
            String stripTrail = str.stripTrailing();
            String stripped = str.strip();
            String leadStr = str.substring(0, str.length() - stripLead.length());
            String trailStr = str.substring(stripTrail.length());
            String delim = getStyleChangeDelim(currentStyle.get(), sty);
            currentStyle.set(sty);
//            res.append(trailingWhitespace.get()).append(leadStr).append(delim).append(stripped);
            res.append(leadStr).append(delim).append(stripped);
            trailingWhitespace.set(trailStr);
            return Optional.empty();
        }, Style.EMPTY);
        String delim = getStyleChangeDelim(currentStyle.get(), Style.EMPTY);
        res.append(delim).append(trailingWhitespace.get());
        return res.toString();
    }
}
