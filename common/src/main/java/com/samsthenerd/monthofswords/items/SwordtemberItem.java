package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.utils.Description;
import com.samsthenerd.monthofswords.utils.Description.DescriptionItemComponent;
import com.samsthenerd.monthofswords.utils.ItemDescriptions;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Optional;
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

        Optional<Description> descOpt;
        if (Platform.getEnvironment() == Env.SERVER) {
            // idk
        } else if ((descOpt = ItemDescriptions.getItemDescription(stack.getItem())).isPresent()){
            // client!
            var desc = descOpt.get();
            tooltip.addAll(desc.getTooltipFull(stack, context, type));
        }

//        // TODO: rewrite this to grab multi-lines, only show shift msg when needed, and show alt descriptions for adventure mode
//        if(SwordtemberItem.hasShiftSafe()){
//            MutableText infoText = Text.translatable(stack.getTranslationKey() + ".tooltip", Text.keybind("key.swordsmod.action"));
//            infoText.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
//            tooltip.add(infoText);
//        } else {
//            MutableText shiftMsg = Text.translatable("monthofswords.tooltip.shiftmsg");
//            shiftMsg.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
//            tooltip.add(shiftMsg);
//        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.ofNullable(stack.get(SwordsModComponents.ITEM_DESCRIPTION_DATA))
            .filter(DescriptionItemComponent::hintMode)
            .flatMap(DescriptionItemComponent::ttData);
//        return Optional.of(new RecipeTooltipData());
    }
}
