package com.samsthenerd.monthofswords.fabric;

import com.samsthenerd.monthofswords.fabric.xplat.SwordsModXPlatFabric;
import net.fabricmc.api.ModInitializer;

import com.samsthenerd.monthofswords.SwordsMod;

public final class SwordsModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        SwordsMod.init();
        SwordsMod.XPLAT_INSTANCE = new SwordsModXPlatFabric();
    }
}
