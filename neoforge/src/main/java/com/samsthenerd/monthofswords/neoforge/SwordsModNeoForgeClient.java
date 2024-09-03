package com.samsthenerd.monthofswords.neoforge;

import com.samsthenerd.monthofswords.SwordsModClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = "monthofswords", value= Dist.CLIENT, bus= EventBusSubscriber.Bus.MOD)
public class SwordsModNeoForgeClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event){
        SwordsModClient.init();
    }
}
