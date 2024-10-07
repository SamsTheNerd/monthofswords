package com.samsthenerd.monthofswords.registry;

import com.mojang.serialization.Codec;
import com.samsthenerd.monthofswords.SwordsMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;

import java.util.function.UnaryOperator;

public class SwordsModComponents {
    private static DeferredRegister<ComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.DATA_COMPONENT_TYPE);

    public static ComponentType<Integer> POTION_HITS = component("potionhits", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));

    private static <T> ComponentType<T> component(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator){
        ComponentType<T> comp = builderOperator.apply(ComponentType.builder()).build();
        COMPONENT_TYPES.register(SwordsMod.id(id), () -> comp);
        return comp;
    }

    public static void register(){
        COMPONENT_TYPES.register();
    }
}
