package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.items.*;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SwordsModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.ITEM_GROUP);

    public static final List<Identifier> ALL_SWORDS = new ArrayList<>();

    public static final RegistrySupplier<WingSwordItem> WING_SWORD = item("wing_sword",
            () -> new WingSwordItem(defaultSettings()));

    public static final RegistrySupplier<FloralSwordItem> FLORAL_SWORD = item("floral_sword",
            () -> new FloralSwordItem(defaultSettings()));

    public static final RegistrySupplier<CursedSwordItem> CURSED_SWORD = item("cursed_sword",
            () -> new CursedSwordItem(defaultSettings()));

    public static final RegistrySupplier<DivineSwordItem> DIVINE_SWORD = item("divine_sword",
            () -> new DivineSwordItem(defaultSettings()));

    public static final RegistrySupplier<Item> GUMMY_SWORD = item("gummy_sword",
            () -> new DescriptableItem(defaultSettings().food(
                    new FoodComponent.Builder()
                            .nutrition(4)
                            .saturationModifier(0.1f)
                            .alwaysEdible()
                            .snack()
                            .build()
            )));

    public static final RegistrySupplier<StealthSwordItem> STEALTH_SWORD = item("stealth_sword",
            () -> new StealthSwordItem(defaultSettings()));

    public static final RegistrySupplier<TimeBasedSwordItem> MOON_SWORD = item("moon_sword",
            () -> new TimeBasedSwordItem(Items.IRON_INGOT, defaultSettings(), false));

    public static final RegistrySupplier<TimeBasedSwordItem> SUN_SWORD = item("sun_sword",
            () -> new TimeBasedSwordItem(Items.GOLD_INGOT, defaultSettings(), true));

    public static final RegistrySupplier<OceanSwordItem> OCEAN_SWORD = item("ocean_sword",
            () -> new OceanSwordItem(defaultSettings()));

    public static final RegistrySupplier<EnchantSwordItem> ENCHANTING_SWORD = item("enchanting_sword",
            () -> new EnchantSwordItem(defaultSettings()));

    public static final RegistrySupplier<DuelingSwordItem> DUELING_SWORD = item("dueling_sword",
            () -> new DuelingSwordItem(defaultSettings()));

    public static final RegistrySupplier<FlameSwordItem> FLAME_SWORD = item("flame_sword",
            () -> new FlameSwordItem(defaultSettings().fireproof()));

    public static final RegistrySupplier<IceSwordItem> ICE_SWORD = item("ice_sword",
        () -> new IceSwordItem(defaultSettings()));

    public static final RegistrySupplier<PoisonDaggerItem> POISON_DAGGER = item("poison_dagger",
        () -> new PoisonDaggerItem(defaultSettings()));

    public static final RegistrySupplier<PotionSwordItem> POTION_SWORD = item("potion_sword",
        () -> new PotionSwordItem(defaultSettings()));

    public static final RegistrySupplier<BoneSwordItem> BONE_SWORD = item("bone_sword",
        () -> new BoneSwordItem(defaultSettings()));

    public static final RegistrySupplier<PortalSwordItem> PORTAL_SWORD = item("portal_sword",
        () -> new PortalSwordItem(defaultSettings()));

    public static final RegistrySupplier<LuckySwordItem> LUCKY_SWORD = item("lucky_sword",
        () -> new LuckySwordItem(defaultSettings()));

    public static final RegistrySupplier<ForestSwordItem> FOREST_SWORD = item("forest_sword",
        () -> new ForestSwordItem(defaultSettings()));

    public static final RegistrySupplier<EchoSwordItem> ECHO_SWORD = item("echo_sword",
        () -> new EchoSwordItem(defaultSettings()));

    public static final RegistrySupplier<EyeSwordItem> EYE_SWORD = item("eye_sword",
        () -> new EyeSwordItem(defaultSettings()));

    public static final RegistrySupplier<MechanicalSwordItem> MECHANICAL_SWORD = item("mechanical_sword",
        () -> new MechanicalSwordItem(defaultSettings()));

    public static final RegistrySupplier<SummonableSwordItem> SUMMONED_SWORD = item("summoned_sword",
        () -> new SummonableSwordItem(defaultSettings()
        ));

    public static final RegistrySupplier<JeweledSwordItem> JEWELED_SWORD = item("jeweled_sword",
        () -> new JeweledSwordItem(defaultSettings()
        ));

    public static final RegistrySupplier<WovenSwordItem> WOVEN_SWORD = item("woven_sword",
        () -> new WovenSwordItem(defaultSettings()));

    public static final RegistrySupplier<ShockSwordItem> SHOCK_SWORD = item("shock_sword",
        () -> new ShockSwordItem(defaultSettings()));

    public static final RegistrySupplier<InfestationSwordItem> INFESTATION_SWORD = item("infestation_sword",
        () -> new InfestationSwordItem(defaultSettings()));

    public static final RegistrySupplier<CrystalSwordItem> CRYSTAL_SWORD = item("crystal_sword",
        () -> new CrystalSwordItem(defaultSettings()));

    public static final RegistrySupplier<NecromancerSwordItem> NECROMANCERS_SWORD = item("necro_sword",
        () -> new NecromancerSwordItem(defaultSettings()));

    public static final RegistrySupplier<GlowSwordItem> GLOW_SWORD = item("glow_sword",
        () -> new GlowSwordItem(defaultSettings()));

    public static final RegistrySupplier<Item> SILVERFISH_SHELL = item("silverfish_shell",
        () -> new DescriptableItem(defaultSettings().rarity(Rarity.RARE)), false);

    public static final RegistrySupplier<Item> SUMMON_FRUIT = item("summon_fruit",
        () -> new SummonFruitItem(defaultSettings().rarity(Rarity.RARE)
            .food(new FoodComponent.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build()), true)
        , false);

    public static final RegistrySupplier<Item> BANISH_FRUIT = item("banish_fruit",
        () -> new SummonFruitItem(defaultSettings().rarity(Rarity.RARE)
            .food(new FoodComponent.Builder().nutrition(4).saturationModifier(0.3f).alwaysEdible().build()), false)
        , false);

    public static final RegistrySupplier<Item> CALENDAR_ITEM = item("calendar",
        () -> new CalendarItem(defaultSettings()), false);

    // make our creative tab.
    public static final RegistrySupplier<ItemGroup> SWORDS_MOD_GROUP = TABS.register("monthofswords_tab", () ->
            CreativeTabRegistry.create(Text.translatable("itemgroup.monthofswords.general"),
                    () -> new ItemStack(CURSED_SWORD.get())));

    /*
     * Helper function for registering an item.
     * register the item supplied with the id `monthofswords:name`
     */
    public static <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item, boolean isSword) {
        Identifier itemId = Identifier.of(SwordsMod.MOD_ID, name);
        if(isSword) ALL_SWORDS.add(itemId);
        return ITEMS.register(itemId, item);
    }

    public static <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item) {
        return item(name, item, true);
    }

    // returns default item settings, here it just puts the item in rpi mod tab
    public static Item.Settings defaultSettings(){
        return new Item.Settings().arch$tab(SWORDS_MOD_GROUP);
    }

    // tell the registry that we're ready for registering.
    // note: should be called after RPIModBlocks.register() since the block items rely on the blocks being registered.
    //
    public static void register(){
        TABS.register();
        ITEMS.register();
    }
}
