package com.samsthenerd.monthofswords.neoforge;

import com.samsthenerd.monthofswords.SwordsModClient;
import com.samsthenerd.monthofswords.registry.SwordsModEntities;
import com.samsthenerd.monthofswords.registry.SwordsModTooltips;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.tooltip.TooltipData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;

import java.util.Map.Entry;
import java.util.function.Function;

@EventBusSubscriber(modid = "monthofswords", value= Dist.CLIENT, bus= EventBusSubscriber.Bus.MOD)
public class SwordsModNeoForgeClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        SwordsModClient.init();
    }

    @SubscribeEvent
    public static void entityRenderListen(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(SwordsModEntities.LEAF_ATTACK.get(), FlyingItemEntityRenderer::new);
    }


    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent evt) {
        SwordsModTooltips.init();
        for(Entry<Class<? extends TooltipData>, Function<TooltipData, TooltipComponent>> entry : SwordsModTooltips.tooltipDataToComponent.entrySet()){
            evt.register(entry.getKey(), entry.getValue());
        }
    }
}
