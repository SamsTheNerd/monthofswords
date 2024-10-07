package com.samsthenerd.monthofswords;

import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.registry.SwordsModKeybinds;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class SwordsModClient {
    public static void init(){
        setupModelPreds();
        setupColorProviders();
        SwordsModKeybinds.init();
    }

    private static void setupModelPreds(){
        ItemPropertiesRegistry.register(SwordsModItems.FLORAL_SWORD.get(), SwordsMod.id("floweryness"),
            (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int i) -> {
                // 0 when no damage, 1 when fully damaged.
                return itemStack.getDamage()/(float)(itemStack.getMaxDamage());
            });
        ItemPropertiesRegistry.register(SwordsModItems.POTION_SWORD.get(), SwordsMod.id("potionlevel"),
            (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int i) -> {
                return itemStack.getOrDefault(SwordsModComponents.POTION_HITS, 0) / 5f;
            });
    }

    private static void setupColorProviders(){
        ColorHandlerRegistry.registerItemColors((stack, layer) -> {
            if(stack.contains(DataComponentTypes.POTION_CONTENTS) && layer == 1){
                PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
                return potionContents.getColor();
            }
            return 0xFF_FFFFFF;
        }, SwordsModItems.POTION_SWORD);
    }
}
