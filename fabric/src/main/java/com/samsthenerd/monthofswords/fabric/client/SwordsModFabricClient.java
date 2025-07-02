package com.samsthenerd.monthofswords.fabric.client;

import com.samsthenerd.monthofswords.SwordsModClient;
import com.samsthenerd.monthofswords.registry.SwordsModEntities;
import com.samsthenerd.monthofswords.registry.SwordsModTooltips;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public final class SwordsModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        SwordsModClient.init();
        SwordsModTooltips.init();
        TooltipComponentCallback.EVENT.register(SwordsModTooltips::getTooltipComponent);
        EntityRendererRegistry.register(SwordsModEntities.LEAF_ATTACK.get(), FlyingItemEntityRenderer::new);
    }
}
