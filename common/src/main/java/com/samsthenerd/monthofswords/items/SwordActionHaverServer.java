package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface SwordActionHaverServer {
    boolean doSwordAction(PlayerEntity player, ItemStack swordStack);
}
