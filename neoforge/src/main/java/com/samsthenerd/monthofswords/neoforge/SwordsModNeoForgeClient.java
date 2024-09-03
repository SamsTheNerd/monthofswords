package com.samsthenerd.monthofswords.neoforge;

import com.samsthenerd.monthofswords.SwordsModClient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class SwordsModNeoForgeClient {
    @SubscribeEvent
    public void clientSetup(FMLClientSetupEvent event){
        SwordsModClient.init();
    }
}
