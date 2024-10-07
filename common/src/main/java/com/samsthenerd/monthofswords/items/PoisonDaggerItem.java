package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;

import java.util.function.UnaryOperator;

public class PoisonDaggerItem extends SwordtemberItem{

    public static final ToolMaterial POISONOUS_MATERIAL = new ClassyToolMaterial(200, 7f, 1f,
        BlockTags.INCORRECT_FOR_STONE_TOOL, 16, () -> Ingredient.ofItems(Items.SPIDER_EYE));

    public PoisonDaggerItem(Item.Settings itemSettings) {
        super(POISONOUS_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(POISONOUS_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 20*20, 3));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0x476e2c);
    }
}
