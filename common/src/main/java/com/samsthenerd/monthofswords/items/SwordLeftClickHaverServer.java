package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface SwordLeftClickHaverServer {
    void doSwordLCAction(PlayerEntity player, ItemStack swordStack);
}
