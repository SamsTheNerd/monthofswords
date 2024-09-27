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
                LootPool.Builder pool = LootPool.builder().with(
                        ItemEntry.builder(SwordsModItems.CURSED_SWORD.get())
                                .conditionally(RandomChanceLootCondition.builder(0.2f))
                );
                context.addPool(pool);
            } else if(builtin && LootTables.JUNGLE_TEMPLE_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder().with(
                        ItemEntry.builder(SwordsModItems.MOON_SWORD.get())
                                // 2 chests per jungle temple + they aren't too rare so i think this is fine, most people encounter a jungle temple or two will find it but it's not guaranteed
                                .conditionally(RandomChanceLootCondition.builder(0.25f))
                );
                context.addPool(pool);
            } else if(builtin && LootTables.DESERT_PYRAMID_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder().with(
                        ItemEntry.builder(SwordsModItems.SUN_SWORD.get())
                                .conditionally(RandomChanceLootCondition.builder(0.2f))
                );
                context.addPool(pool);
            } else if(builtin && LootTables.SHIPWRECK_TREASURE_CHEST.equals(key)) {
                LootPool.Builder pool = LootPool.builder().with(
                        ItemEntry.builder(SwordsModItems.OCEAN_SWORD.get())
                                .conditionally(RandomChanceLootCondition.builder(0.3f))
                );
                context.addPool(pool);
            }
        });
    }
}
