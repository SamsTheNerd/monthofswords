package com.samsthenerd.monthofswords.utils;

import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.utils.Description.SwordPower;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ItemDescriptions {

    private static final Map<Identifier, Description> ITEM_DESCRIPTIONS = new HashMap<>();

    public static Optional<Description> getItemDescription(Item item){
        return Optional.ofNullable(ITEM_DESCRIPTIONS.get(Registries.ITEM.getId(item)));
    }

    public static void addDescription(Description desc){
        ITEM_DESCRIPTIONS.put(desc.item().getId(), desc);
    }

    public static final Description WING_DESC = Description.forItem(SwordsModItems.WING_SWORD)
        .finalize(ItemDescriptions::addDescription);

    public static final Description FLORAL_DESC = Description.forItem(SwordsModItems.FLORAL_SWORD)
        .withPower(new SwordPower("photosynthesis", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(Formatting.GREEN)
        .finalize(ItemDescriptions::addDescription);


    public static final Description CURSED_DESC = Description.forItem(SwordsModItems.CURSED_SWORD)
        .withPower(new SwordPower("curse", Description.HIT_POWER, Optional.empty()))
        .withTextColor(Formatting.RED)
        .finalize(ItemDescriptions::addDescription);


    public static final Description DIVINE_DESC = Description.forItem(SwordsModItems.DIVINE_SWORD)
        .withPower(new SwordPower("divinity", Description.ACTION_POWER, Optional.of(60*20)))
        .withTextColor(Formatting.YELLOW)
        .finalize(ItemDescriptions::addDescription);

    public static final Description STEALTH_DESC = Description.forItem(SwordsModItems.STEALTH_SWORD)
        .withPower(new SwordPower("smoke_bomb", Description.USE_POWER, Optional.of(20*30)))
        .withPower(new SwordPower("sneak_attack", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("set_firework", Description.INV_USE, Optional.empty()))
        .withTextColor(Formatting.RED)
        .finalize(ItemDescriptions::addDescription);

    public static final Description MOON_DESC = Description.forItem(SwordsModItems.MOON_SWORD)
        .withPower(new SwordPower("moonlit", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(Formatting.BLUE)
        .finalize(ItemDescriptions::addDescription);

    public static final Description SUN_DESC = Description.forItem(SwordsModItems.SUN_SWORD)
        .withPower(new SwordPower("sunlit", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(Formatting.YELLOW)
        .finalize(ItemDescriptions::addDescription);

    public static final Description OCEAN_DESC = Description.forItem(SwordsModItems.OCEAN_SWORD)
        .withPower(new SwordPower("oceanic", Description.PASSIVE_POWER, Optional.empty()))
        .withTextColor(0x1dc2d1)
        .finalize(ItemDescriptions::addDescription);

    public static final Description ENCHANTING_DESC = Description.forItem(SwordsModItems.ENCHANTING_SWORD)
        .withPower(new SwordPower("reroll", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0x91f788)
        .finalize(ItemDescriptions::addDescription);

    public static final Description DUELING_DESC = Description.forItem(SwordsModItems.DUELING_SWORD)
        .withPower(new SwordPower("block", Description.HOLD_POWER, Optional.empty()))
        .withPower(new SwordPower("parry", Description.HIT_POWER, Optional.empty()))
        .withTextColor(Formatting.GOLD)
        .finalize(ItemDescriptions::addDescription);

    public static final Description FLAME_DESC = Description.forItem(SwordsModItems.FLAME_SWORD)
        .withPower(new SwordPower("flame_lick", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("scorch", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("fireball", Description.USE_POWER, Optional.of(50)))
        .withPower(new SwordPower("inferno", Description.ACTION_POWER, Optional.of(20*15)))
        .withTextColor(0xfc6f03)
        .finalize(ItemDescriptions::addDescription);

    public static final Description ICE_DESC = Description.forItem(SwordsModItems.ICE_SWORD)
        .withPower(new SwordPower("frostbite", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("freezing", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("snowball", Description.USE_POWER, Optional.of(30)))
        .withPower(new SwordPower("blizzard", Description.ACTION_POWER, Optional.empty()))
        .withTextColor(0x5eefff)
        .finalize(ItemDescriptions::addDescription);

    public static final Description POISON_DESC = Description.forItem(SwordsModItems.POISON_DAGGER)
        .withPower(new SwordPower("venom", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0x476e2c)
        .finalize(ItemDescriptions::addDescription);

    public static final Description POTION_DESC = Description.forItem(SwordsModItems.POTION_SWORD)
        .withPower(new SwordPower("brew", Description.USE_POWER, Optional.empty()))
        .withPower(new SwordPower("inject", Description.HIT_POWER, Optional.empty()))
        .withTextColor(0x69adf0)
        .finalize(ItemDescriptions::addDescription);

    public static final Description BONE_DESC = Description.forItem(SwordsModItems.BONE_SWORD)
        .withTextColor(0xe8e5d2)
        .finalize(ItemDescriptions::addDescription);


    public static final Description PORTAL_DESC = Description.forItem(SwordsModItems.PORTAL_SWORD)
        .withPower(new SwordPower("dimension_hop", Description.ACTION_POWER, Optional.empty()))
        .withTextColor(0xaf4fe3)
        .finalize(ItemDescriptions::addDescription);

    public static final Description LUCKY_DESC = Description.forItem(SwordsModItems.LUCKY_SWORD)
        .withPower(new SwordPower("lucky_dice", Description.HIT_POWER, Optional.empty()))
        .withTextColor(Formatting.GOLD)
        .finalize(ItemDescriptions::addDescription);

}
