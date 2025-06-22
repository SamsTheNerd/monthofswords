package com.samsthenerd.monthofswords.xplat;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiPredicate;

public interface CAttachmentType<T>{

    interface Builder<T>{
        Builder<T> persistent(Codec<T> codec, boolean copyOnDeath);

        Builder<T> clientSyncable(PacketCodec<? super RegistryByteBuf, T> packetCodec, BiPredicate<CAttachmentTarget, ServerPlayerEntity> syncPredicate);
    }
}
