package com.samsthenerd.monthofswords.fabric.client;

import com.samsthenerd.monthofswords.SwordsModClient;
import net.fabricmc.api.ClientModInitializer;

public final class SwordsModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SwordsModClient.init();
    }
}
