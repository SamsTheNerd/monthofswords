package com.samsthenerd.monthofswords;

import com.samsthenerd.monthofswords.registry.SwordsModItems;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class SwordsModClient {
    public static void init(){
        setupModelPreds();
    }

    private static void setupModelPreds(){
        ItemPropertiesRegistry.register(SwordsModItems.FLORAL_SWORD.get(), Identifier.of(SwordsMod.MOD_ID, "floweryness"),
                (ItemStack itemStack, ClientWorld clientWorld, LivingEntity livingEntity, int i) -> {
                    // 0 when no damage, 1 when fully damaged.
                    return itemStack.getDamage()/(float)(itemStack.getMaxDamage());
                });
    }
}
