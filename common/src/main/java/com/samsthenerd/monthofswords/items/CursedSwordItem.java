package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.registry.SwordsModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.function.UnaryOperator;

public class CursedSwordItem extends SwordtemberItem {

    public CursedSwordItem(Item.Settings itemSettings){
        super(ToolMaterials.NETHERITE, itemSettings.attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -2.9f)));
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        float targetHealthPortion = target.getMaxHealth() * 0.05f;
        float attackerHealthPortion = attacker.getMaxHealth() * 0.05f;
        target.damage(SwordsModDamageTypes.getCurseDamage(attacker), Math.max(attackerHealthPortion, targetHealthPortion));
        attacker.damage(SwordsModDamageTypes.getCurseDamage(attacker.getWorld()), attackerHealthPortion);
        super.postDamageEntity(stack, target, attacker);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.RED);
    }
}
