package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.items.CursedSwordItem;
import com.samsthenerd.monthofswords.items.DivineSwordItem;
import com.samsthenerd.monthofswords.items.FloralSwordItem;
import com.samsthenerd.monthofswords.items.WingSwordItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class SwordsModItems {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.ITEM);
    public static final DeferredRegister<ItemGroup> TABS = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.ITEM_GROUP);

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
            )));


    // make our creative tab.
    public static final RegistrySupplier<ItemGroup> SWORDS_MOD_GROUP = TABS.register("monthofswords_tab", () ->
            CreativeTabRegistry.create(Text.translatable("itemgroup.monthofswords.general"),
                    () -> new ItemStack(CURSED_SWORD.get())));

    /*
     * Helper function for registering an item.
     * register the item supplied with the id `monthofswords:name`
     */
    public static <T extends Item> RegistrySupplier<T> item(String name, Supplier<T> item) {
        return ITEMS.register(Identifier.of(SwordsMod.MOD_ID, name), item);
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
