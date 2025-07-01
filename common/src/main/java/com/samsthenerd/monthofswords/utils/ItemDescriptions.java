package com.samsthenerd.monthofswords.utils;

import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.utils.Description.SwordPower;
import dev.architectury.platform.Platform;
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
//        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);

    public static final Description GLOW_DESC = Description.forItem(SwordsModItems.GLOW_SWORD)
        .withPower(new SwordPower("piercing_eyes", Description.PASSIVE_POWER, Optional.empty()))
        .withPower(new SwordPower("illuminance", Description.HIT_POWER, Optional.empty()))
        .withPower(new SwordPower("dark_vision", Description.USE_POWER, Optional.of(20*15)))
        .withPower(new SwordPower("radiance", Description.ACTION_POWER, Optional.of(20*45)))
        .withTextColor(0x90f0d0)
        .withMatchingRecipe()
        .finalize(ItemDescriptions::addDescription);
}
