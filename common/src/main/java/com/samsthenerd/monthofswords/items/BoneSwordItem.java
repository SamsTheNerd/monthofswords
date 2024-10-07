package com.samsthenerd.monthofswords.items;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;

import java.util.function.UnaryOperator;

public class BoneSwordItem extends SwordtemberItem{
    public static final ToolMaterial BONE_MATERIAL = new ClassyToolMaterial(180, 4f, 1.5f,
        BlockTags.INCORRECT_FOR_STONE_TOOL, 16, () -> Ingredient.ofItems(Items.BONE));

    public BoneSwordItem(Item.Settings itemSettings) {
        super(BONE_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(BONE_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0xe8e5d2);
    }
}
