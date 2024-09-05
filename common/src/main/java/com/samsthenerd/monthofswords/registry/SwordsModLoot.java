package com.samsthenerd.monthofswords.registry;

import dev.architectury.event.events.common.LootEvent;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;

public class SwordsModLoot {
    public static void init() {
        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            if (builtin && LootTables.BASTION_TREASURE_CHEST.equals(key)) {
                // Create a loot pool with a single item entry of Items.DIAMOND
                LootPool.Builder pool = LootPool.builder().with(
                        ItemEntry.builder(SwordsModItems.CURSED_SWORD.get())
                                .conditionally(RandomChanceLootCondition.builder(0.2f))
                );
                context.addPool(pool);
            }
        });
    }
}
