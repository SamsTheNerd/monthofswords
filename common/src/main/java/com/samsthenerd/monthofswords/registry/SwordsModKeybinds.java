package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.items.SwordActionHaverClient;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.networking.NetworkManager;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;

public class SwordsModKeybinds {
    public static final KeyBinding SWORDS_KEY = new KeyBinding(
            "key.swordsmod.action", // The translation key of the name shown in the Controls screen
            InputUtil.Type.KEYSYM, // This key mapping is for Keyboards by default
            InputUtil.GLFW_KEY_I, // The default keycode
            "category.swordsmod.swordscat" // The category translation key used to categorize in the Controls screen
    );

    public static void init(){
        KeyMappingRegistry.register(SWORDS_KEY);
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            while (SWORDS_KEY.wasPressed()) {
                boolean handled = false;
                if(minecraft.player == null) continue;
                for(ItemStack handItem : minecraft.player.getHandItems()){
                    if(handItem.getItem() instanceof SwordActionHaverClient clientActionHaver){
                        if(clientActionHaver.doSwordActionClient(handItem)){
                            handled = true;
                            break;
                        }
                    }
                }
                if(!handled){
                    NetworkManager.sendToServer(new SwordsModNetworking.SwordActionPayload());
                }
            }
        });
    }
}
