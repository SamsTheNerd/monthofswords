package com.samsthenerd.monthofswords.items;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.UnaryOperator;

// utility class for all swords
public class SwordtemberItem extends SwordItem {
    public SwordtemberItem(ToolMaterial toolMats, Item.Settings itemSettings){
        super(toolMats, itemSettings);
    }

    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.GRAY);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        // TODO: rewrite this to grab multi-lines, only show shift msg when needed, and show alt descriptions for adventure mode
        if(SwordtemberItem.hasShiftSafe()){
            MutableText infoText = Text.translatable(stack.getTranslationKey() + ".tooltip", Text.keybind("key.swordsmod.action"));
            infoText.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
            tooltip.add(infoText);
        } else {
            MutableText shiftMsg = Text.translatable("monthofswords.tooltip.shiftmsg");
            shiftMsg.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
            tooltip.add(shiftMsg);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    // returns true if shift is down or if it's on the server
    // meant to be used for tooltips to not break polydex
    public static boolean hasShiftSafe(){
        return Platform.getEnvironment() == Env.SERVER || Screen.hasShiftDown();
    }
}
