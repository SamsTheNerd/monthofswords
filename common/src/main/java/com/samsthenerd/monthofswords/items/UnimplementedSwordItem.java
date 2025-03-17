package com.samsthenerd.monthofswords.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class UnimplementedSwordItem extends SwordItem {

    public UnimplementedSwordItem(Item.Settings itemSettings){
        super(ToolMaterials.DIAMOND, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4f))
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {

        MutableText shiftMsg = Text.translatable("monthofswords.tooltip.unimplementedmsg");
        shiftMsg.setStyle(Style.EMPTY.withColor(Formatting.GRAY).withItalic(true));
        tooltip.add(shiftMsg);
        super.appendTooltip(stack, context, tooltip, type);
    }
}
