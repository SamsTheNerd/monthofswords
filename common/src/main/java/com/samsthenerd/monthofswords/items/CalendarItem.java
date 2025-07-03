package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.screen.ScreenLaunderer;
import com.samsthenerd.monthofswords.utils.ItemDescriptions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CalendarItem extends DescriptableItem {
    public CalendarItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()){
            ItemStack stack = user.getStackInHand(hand);
            if(stack.getName().getString().equals("printMarkdown")){
                ItemDescriptions.printAllAsMarkdown();
            } else {
                ScreenLaunderer.openCalendarScreen();
            }

        }
        return TypedActionResult.success(user.getStackInHand(hand), true);
    }
}
