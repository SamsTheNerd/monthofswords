package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.screen.SwordCalendarScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CalendarItem extends Item {
    public CalendarItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()){
            MinecraftClient.getInstance().setScreen(new SwordCalendarScreen());
        }
        return TypedActionResult.success(user.getStackInHand(hand), true);
    }
}
