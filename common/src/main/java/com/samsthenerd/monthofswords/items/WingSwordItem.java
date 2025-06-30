package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;

public class WingSwordItem extends SwordtemberItem {
    public static final ClassyToolMaterial WING_MATERIAL = new ClassyToolMaterial(2031, 12f, 0.1f,
            BlockTags.INCORRECT_FOR_GOLD_TOOL, 22, () -> Ingredient.EMPTY);

    public WingSwordItem(Item.Settings itemSettings){
        super(WING_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(WING_MATERIAL, -1, 0)
            .with(
                EntityAttributes.GENERIC_ATTACK_KNOCKBACK,
                new EntityAttributeModifier(SwordsMod.id("wing_sword_knockback"), 1f, EntityAttributeModifier.Operation.ADD_VALUE),
                AttributeModifierSlot.MAINHAND
                )
            ));
    }
}
