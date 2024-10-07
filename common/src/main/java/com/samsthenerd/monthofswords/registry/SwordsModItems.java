package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.items.*;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

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
            () -> new Item(defaultSettings().food(
                    new FoodComponent.Builder()
                            .nutrition(4)
                            .saturationModifier(0.1f)
                            .alwaysEdible()
                            .snack()
                            .build()
            )){
                @Override
                public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
                    if(Screen.hasShiftDown()){
                        MutableText infoText = Text.translatable(stack.getTranslationKey() + ".tooltip");
                        infoText.setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.AQUA));
                        tooltip.add(infoText);
                    } else {
                        MutableText shiftMsg = Text.translatable("monthofswords.tooltip.shiftmsg");
                        shiftMsg.setStyle(Style.EMPTY.withItalic(true).withColor(Formatting.AQUA));
                        tooltip.add(shiftMsg);
                    }
                    super.appendTooltip(stack, context, tooltip, type);
                }
            });

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
