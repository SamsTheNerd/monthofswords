package com.samsthenerd.monthofswords.registry;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import dev.architectury.event.events.common.LootEvent;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class SwordsModLoot {

    public static void init() {
        addLoot(SwordsModItems.CURSED_SWORD, 0.2f, LootTables.BASTION_TREASURE_CHEST);
        addLoot(SwordsModItems.MOON_SWORD, 0.25f, LootTables.JUNGLE_TEMPLE_CHEST);
        addLoot(SwordsModItems.SUN_SWORD, 0.2f, LootTables.DESERT_PYRAMID_CHEST);
        addLoot(SwordsModItems.OCEAN_SWORD, 0.3f, LootTables.SHIPWRECK_TREASURE_CHEST);
        addLoot(SwordsModItems.POISON_DAGGER, 0.35f, LootTables.SIMPLE_DUNGEON_CHEST, LootTables.PILLAGER_OUTPOST_CHEST);
        addLoot(SwordsModItems.PORTAL_SWORD, 0.2f, LootTables.NETHER_BRIDGE_CHEST);
        addLoot(SwordsModItems.PORTAL_SWORD, 0.02f, LootTables.RUINED_PORTAL_CHEST);
        addLoot(SwordsModItems.LUCKY_SWORD, 0.05f, LootTables.BASTION_BRIDGE_CHEST);

        LootEvent.MODIFY_LOOT_TABLE.register((key, context, builtin) -> {
            for(LootEvent.ModifyLootTable modifier : LOOT_MODIFIERS.get(key.getValue())){
                modifier.modifyLootTable(key, context, builtin);
            }
        });
    }

    private static final Multimap<Identifier, LootEvent.ModifyLootTable> LOOT_MODIFIERS = MultimapBuilder.hashKeys().linkedHashSetValues().build();

    @SafeVarargs
    private static void addLoot(Supplier<? extends Item> itemSupplier, float chance, RegistryKey<LootTable>... tables){
        for(RegistryKey<LootTable> table : tables){
            LOOT_MODIFIERS.put(table.getValue(), (key, context, builtin) -> {
                LootPool.Builder pool = LootPool.builder().with(
                    ItemEntry.builder(itemSupplier.get())
                        .conditionally(RandomChanceLootCondition.builder(chance))
                );
                context.addPool(pool);
            });
        }
    }

}
