package com.samsthenerd.monthofswords.neoforge.xplat;

import com.mojang.serialization.Codec;
import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.BiPredicate;

public record NFAttachmentType<T>(AttachmentType<T> attType) implements CAttachmentType<T> {

    public record NFATBuilder<T>(AttachmentType.Builder<T> nfBuilder) implements Builder<T>{

        @Override
        public Builder<T> persistent(Codec<T> codec, boolean copyOnDeath) {
            nfBuilder.serialize(codec);
            if(copyOnDeath) nfBuilder.copyOnDeath();
            return this;
        }

        @Override
        public Builder<T> clientSyncable(PacketCodec<? super RegistryByteBuf, T> packetCodec, BiPredicate<CAttachmentTarget, ServerPlayerEntity> syncPredicate) {
            // TODO: implement this
            return this;
        }
    }
}
