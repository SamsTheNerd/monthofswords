package com.samsthenerd.monthofswords.fabric.datagen;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.utils.ItemDescriptions;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.AdvancementRewards;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.TickCriterion;
import net.minecraft.data.server.advancement.AdvancementTabGenerator;
import net.minecraft.item.ItemConvertible;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.PlayerPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

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

            var allSwordItems = SwordsModItems.ALL_SWORDS.stream()
                // the world if java had compact function composition
                .map(Registries.ITEM::getEntry)
                .flatMap(Optional::stream)
                .map(entry -> (ItemConvertible)entry::value)
                .toArray(ItemConvertible[]::new);

            Advancement.Builder.createUntelemetered()
                .display(
                    SwordsModItems.CALENDAR_ITEM::get,
                    Text.translatable("advancements.swordsmod.root.title"),
                    Text.translatable("advancements.swordsmod.root.description"),
                    Identifier.of("textures/gui/advancements/backgrounds/adventure.png"), AdvancementFrame.TASK, true, true, false
                )
                .criterion("get_sword", InventoryChangedCriterion.Conditions.items(ItemPredicate.Builder.create().items(allSwordItems).build()))
                .rewards(AdvancementRewards.Builder.recipe(SwordsMod.id("calendar")))
                .build(consumer, "monthofswords:root");

            List<AdvancementEntry> swordAdvancements = new ArrayList<>();

            for(Identifier itemId : SwordsModItems.ALL_SWORDS){
                ItemConvertible itemSupplier = () -> Registries.ITEM.get(itemId);
                var descOpt = ItemDescriptions.getItemDescriptionFromId(itemId);
                var textColorer = descOpt
                    .map(desc -> (UnaryOperator<Text>)desc::applyColor)
                    .orElse(UnaryOperator.identity());
                var swordAdv = Advancement.Builder.createUntelemetered()
                        .parent(AdvancementTabGenerator.reference("monthofswords:root"))
                        .display(
                                itemSupplier,
                                textColorer.apply(Text.translatable("advancements.swordsmod." + itemId.getPath() + ".title")),
                                textColorer.apply(Text.translatable("advancements.swordsmod." + itemId.getPath() + ".description")),
                                null, AdvancementFrame.TASK, false, false, false
                        )
                        .criterion("get_sword", InventoryChangedCriterion.Conditions.items(itemSupplier))
                        .build(consumer, "monthofswords:acquire_" + itemId.getPath());

                swordAdvancements.add(swordAdv);
            }

            var endOfMonthBuilder = Advancement.Builder.createUntelemetered()
                .parent(AdvancementTabGenerator.reference("monthofswords:root"))
                .display(
                    SwordsModItems.CALENDAR_ITEM::get,
                    Text.translatable("advancements.swordsmod.long_month.title"),
                    Text.translatable("advancements.swordsmod.long_month.description"),
                    Identifier.of("textures/gui/advancements/backgrounds/adventure.png"), AdvancementFrame.CHALLENGE, true, true, false
                );
            for(AdvancementEntry swordAdvEntry : swordAdvancements){
                endOfMonthBuilder.criterion(swordAdvEntry.id().toTranslationKey(), TickCriterion.Conditions.createLocation(
                    EntityPredicate.Builder.create().typeSpecific(
                        PlayerPredicate.Builder.create().advancement(swordAdvEntry.id(), true).build()
                    )
                ));
            }
            endOfMonthBuilder.build(consumer, "monthofswords:long_month");
        }
    }
}