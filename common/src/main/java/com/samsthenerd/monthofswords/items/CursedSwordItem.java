package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class CursedSwordItem extends SwordItem {

    public CursedSwordItem(Item.Settings itemSettings){
        super(ToolMaterials.NETHERITE, itemSettings.attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -3.4f)));
    }

    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        float targetHealthPortion = target.getMaxHealth() * 0.05f;
        float attackerHealthPortion = attacker.getMaxHealth() * 0.05f;
        target.damage(target.getWorld().getDamageSources().magic(), Math.max(attackerHealthPortion, targetHealthPortion));
        attacker.damage(target.getWorld().getDamageSources().magic(), attackerHealthPortion);
        // TODO: custom damage type ?
    }
}
