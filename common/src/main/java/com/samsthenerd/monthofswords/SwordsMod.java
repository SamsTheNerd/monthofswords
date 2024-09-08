package com.samsthenerd.monthofswords;

import com.google.common.base.Suppliers;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.registry.SwordsModLoot;
import com.samsthenerd.monthofswords.registry.SwordsModNetworking;
import com.samsthenerd.monthofswords.registry.SwordsModStatusEffects;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class SwordsMod {
    public static final String MOD_ID = "monthofswords";

    public static Identifier id(String path){
        return Identifier.of(MOD_ID, path);
    }

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        SwordsModItems.register();
        SwordsModNetworking.commonInit();
        SwordsModLoot.init();
        SwordsModStatusEffects.init();
    }
}
