package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.utils.Description;
import com.samsthenerd.monthofswords.utils.Description.DescriptionItemComponent;
import com.samsthenerd.monthofswords.utils.ItemDescriptions;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class DescriptableItem extends Item {

    public DescriptableItem(Settings settings) {
        super(settings);
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
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return Optional.ofNullable(stack.get(SwordsModComponents.ITEM_DESCRIPTION_DATA))
            .filter(DescriptionItemComponent::hintMode)
            .flatMap(DescriptionItemComponent::ttData);
    }
}
