package com.samsthenerd.monthofswords.neoforge;

import net.neoforged.fml.common.Mod;

import com.samsthenerd.monthofswords.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModNeoForge {
    public ExampleModNeoForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}
