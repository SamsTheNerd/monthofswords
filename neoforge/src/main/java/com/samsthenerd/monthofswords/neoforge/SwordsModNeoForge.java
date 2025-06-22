package com.samsthenerd.monthofswords.neoforge;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.neoforge.xplat.SwordsModXPlatNF;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(SwordsMod.MOD_ID)
public final class SwordsModNeoForge {
    public SwordsModNeoForge() {
        // Run our common setup.
        SwordsMod.init();
        SwordsMod.XPLAT_INSTANCE = new SwordsModXPlatNF();
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(
            NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
            registry -> {
                for(var entry : SwordsModXPlatNF.ATTACHMENT_TYPES.entrySet()){
                    registry.register(entry.getKey(), entry.getValue());
                }
            }
        );
    }
}
