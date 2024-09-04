package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.items.SwordActionHaverServer;
import dev.architectury.networking.NetworkManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public class SwordsModNetworking {
    public static void commonInit(){
        NetworkManager.registerReceiver(NetworkManager.Side.C2S, SwordActionPayload.ID, SwordActionPayload.CODEC,
                (payload, context) -> {
                    PlayerEntity player = context.getPlayer();
                    if(player == null) return;
                    for(ItemStack handItem : player.getHandItems()){
                        if(handItem.getItem() instanceof SwordActionHaverServer serverActionHaver){
                            if(serverActionHaver.doSwordAction(player, handItem)){
                                break;
                            }
                        }
                    }
                });
    }

    // no way this is the proper way to do this. Absolute brain rot if so
    public record SwordActionPayload() implements CustomPayload {
        public static final CustomPayload.Id<SwordActionPayload> ID = new CustomPayload.Id<>(Identifier.of(SwordsMod.MOD_ID, "sword_action_payload"));
        public static final PacketCodec<RegistryByteBuf, SwordActionPayload> CODEC = PacketCodec.unit(new SwordActionPayload());


        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
}
