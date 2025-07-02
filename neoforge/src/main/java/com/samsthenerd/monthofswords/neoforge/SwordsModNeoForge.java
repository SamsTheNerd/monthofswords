package com.samsthenerd.monthofswords.neoforge;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.neoforge.xplat.SwordsModXPlatNF;
import com.samsthenerd.monthofswords.registry.SwordsModDataAttachments;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(SwordsMod.MOD_ID)
public final class SwordsModNeoForge {
    public SwordsModNeoForge(IEventBus modBus) {
        // Run our common setup.
        SwordsMod.XPLAT_INSTANCE = new SwordsModXPlatNF();
        SwordsMod.init();
        modBus.register(this);
//        SwordsModXPlatNF.ATTACHMENT_TYPES_REGISTRY.register(modBus);
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(
            NeoForgeRegistries.Keys.ATTACHMENT_TYPES,
            registry -> {
                SwordsModDataAttachments.init();
                for(var entry : SwordsModXPlatNF.ATTACHMENT_TYPES.entrySet()){
                    registry.register(entry.getKey(), entry.getValue());
                }
            }
        );
    }
}
