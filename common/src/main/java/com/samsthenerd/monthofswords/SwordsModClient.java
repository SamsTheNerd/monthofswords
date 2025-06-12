package com.samsthenerd.monthofswords;

import com.samsthenerd.monthofswords.registry.*;
import com.samsthenerd.monthofswords.render.GhostifyTexture;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.ReloadListenerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.item.ItemPropertiesRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.Hand;

public class SwordsModClient {
    public static void init(){
        setupModelPreds();
        setupColorProviders();
        SwordsModKeybinds.init();
        registerEntityRenderers();

        InteractionEvent.CLIENT_LEFT_CLICK_AIR.register((PlayerEntity player, Hand hand) -> {
            NetworkManager.sendToServer(new SwordsModNetworking.SwordLeftClickPayload(hand == Hand.MAIN_HAND));
        });

        ReloadListenerRegistry.register(ResourceType.CLIENT_RESOURCES, (SynchronousResourceReloader) GhostifyTexture::clearTextures);
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

    private static void registerEntityRenderers(){
        EntityRendererRegistry.register(SwordsModEntities.LEAF_ATTACK, FlyingItemEntityRenderer::new);
    }
}
