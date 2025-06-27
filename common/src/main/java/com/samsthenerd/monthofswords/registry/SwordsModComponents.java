package com.samsthenerd.monthofswords.registry;

import com.mojang.serialization.Codec;
import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.items.CrystalSwordItem.ResonatingComponent;
import com.samsthenerd.monthofswords.items.SummonableSwordItem.SummonSwordComponentData;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKeys;

import java.util.function.UnaryOperator;

public class SwordsModComponents {
    private static DeferredRegister<ComponentType<?>> COMPONENT_TYPES = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.DATA_COMPONENT_TYPE);

    public static ComponentType<Integer> POTION_HITS = component("potionhits", builder -> builder.codec(Codec.INT).packetCodec(PacketCodecs.INTEGER));

    public static ComponentType<SummonSwordComponentData> SUMMON_SWORD_DATA = component("summon_sword_data", builder ->
        builder.codec(SummonSwordComponentData.CODEC).packetCodec(SummonSwordComponentData.PACKET_CODEC));

    public static ComponentType<ResonatingComponent> RESONANCE_DATA = component("crystal_resonance_data", builder ->
        builder.codec(ResonatingComponent.CODEC).packetCodec(ResonatingComponent.PACKET_CODEC));

    private static <T> ComponentType<T> component(String id, UnaryOperator<ComponentType.Builder<T>> builderOperator){
        ComponentType<T> comp = builderOperator.apply(ComponentType.builder()).build();
        COMPONENT_TYPES.register(SwordsMod.id(id), () -> comp);
        return comp;
    }

    public static void register(){
        COMPONENT_TYPES.register();
    }
}
