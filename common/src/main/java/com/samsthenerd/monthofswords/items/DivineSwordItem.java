package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.function.UnaryOperator;

public class DivineSwordItem extends SwordtemberItem implements SwordActionHaverServer{
    public DivineSwordItem(Item.Settings itemSettings){
        super(ToolMaterials.NETHERITE, itemSettings.attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -2.1f)));
    }

    @Override
    public boolean doSwordAction(PlayerEntity player, ItemStack swordStack){
        SwordsMod.LOGGER.info("action !!");
        return true;
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.WHITE);
    }
}
