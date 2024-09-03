package com.samsthenerd.monthofswords;

import com.samsthenerd.monthofswords.registry.SwordsModItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SwordsMod {
    public static final String MOD_ID = "monthofswords";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        SwordsModItems.register();
    }
}
