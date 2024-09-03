package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.MathHelper;

public class WingSwordItem extends SwordItem {
    public static final ClassyToolMaterial WING_MATERIAL = new ClassyToolMaterial(2031, 12f, 0,
            BlockTags.INCORRECT_FOR_GOLD_TOOL, 22, () -> Ingredient.EMPTY);

    public WingSwordItem(Item.Settings itemSettings){
        super(WING_MATERIAL, itemSettings.attributeModifiers(SwordItem.createAttributeModifiers(WING_MATERIAL, -1, 0)));
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // nop, don't take durability.
        target.takeKnockback(1,
                MathHelper.sin(attacker.getYaw() * ((float)Math.PI / 180)), -MathHelper.cos(attacker.getYaw() * ((float)Math.PI / 180)));
    }
}
