package com.samsthenerd.monthofswords.screen;

import net.minecraft.client.MinecraftClient;

public class ScreenLaunderer {
    public static void openCalendarScreen(){
        MinecraftClient.getInstance().setScreen(new SwordCalendarScreen());
    }
}
