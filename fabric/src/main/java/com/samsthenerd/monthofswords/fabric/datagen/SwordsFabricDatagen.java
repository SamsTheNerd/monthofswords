package com.samsthenerd.monthofswords.fabric.datagen;

import com.samsthenerd.monthofswords.registry.SwordsModItems;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SwordsFabricDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        // Adding a provider example:
        //
         pack.addProvider(AdvancementsProvider::new);
    }

    static class AdvancementsProvider extends FabricAdvancementProvider {
        protected AdvancementsProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
            super(output, registryLookup);
        }

        @Override
        public void generateAdvancement(RegistryWrapper.WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer){
            for(Identifier itemId : SwordsModItems.ALL_SWORDS){
                ItemConvertible itemSupplier = () -> Registries.ITEM.get(itemId);
                Advancement.Builder.createUntelemetered()
                        .parent(AdvancementTabGenerator.reference("minecraft:adventure/root")) // TODO: make our own tab
                        .display(
                                itemSupplier,
                                Text.translatable("advancements.swordsmod." + itemId.getPath() + ".title"),
                                Text.translatable("advancements.swordsmod." + itemId.getPath() + ".description"),
                                null, AdvancementFrame.TASK, true, true, false
                        )
                        .criterion("get_sword", InventoryChangedCriterion.Conditions.items(itemSupplier))
                        .build(consumer, "monthofswords:acquire_" + itemId.getPath());
            }
        }
    }
}