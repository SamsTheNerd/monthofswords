package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.lucky.LuckyHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.function.UnaryOperator;

public class LuckySwordItem extends SwordtemberItem{
    public LuckySwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.GOLD, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.GOLD, 2, -2.4f))
        );
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        double luck = 0;
        if(attacker instanceof PlayerEntity player){
            luck = player.getLuck();
        }
        stack.damage(LuckyHandler.getLucky(luck).attack(target, attacker), attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.GOLD);
    }
}
