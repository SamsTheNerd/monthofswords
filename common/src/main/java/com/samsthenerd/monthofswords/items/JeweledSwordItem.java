package com.samsthenerd.monthofswords.items;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class JeweledSwordItem extends SwordtemberItem {

    public JeweledSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.GOLD, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.GOLD, 3, -2.4f))
        );
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier() {
        return (style) -> style.withColor(Formatting.GOLD);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(!world.isClient()){
            // is this inefficient? probably! i don't know a better way to do it though!!
            var lootEnchantMaybe = world.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(Enchantments.LOOTING);
            lootEnchantMaybe.ifPresent(le ->
                stack.apply(DataComponentTypes.ENCHANTMENTS, ItemEnchantmentsComponent.DEFAULT, enchComp -> {
                    var builder = new ItemEnchantmentsComponent.Builder(enchComp);
                    builder.add(le, 5);
                    return builder.build();
                })
            );
        }
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}
