package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.lucky.LuckyDataLoader;
import dev.architectury.registry.ReloadListenerRegistry;
import net.minecraft.resource.ResourceType;

public class SwordsModDataLoaders {
    public static void init(){
        ReloadListenerRegistry.register(ResourceType.SERVER_DATA, new LuckyDataLoader());
    }
}
