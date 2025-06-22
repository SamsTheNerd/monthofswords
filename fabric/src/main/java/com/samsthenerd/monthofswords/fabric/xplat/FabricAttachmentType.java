package com.samsthenerd.monthofswords.fabric.xplat;

import com.mojang.serialization.Codec;
import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.function.BiPredicate;

public record FabricAttachmentType<T>(AttachmentType<T> attTypeFabric) implements CAttachmentType<T> {

    public record FabricATBuilder<T>(AttachmentRegistry.Builder<T> fabricBuilder) implements  CAttachmentType.Builder<T>{

        @Override
        public Builder<T> persistent(Codec<T> codec, boolean copyOnDeath) {
            var builder = fabricBuilder.persistent(codec);
            if(copyOnDeath) builder = builder.copyOnDeath();
            return new FabricATBuilder<T>(builder);
        }

        @Override
        public Builder<T> clientSyncable(PacketCodec<? super RegistryByteBuf, T> packetCodec, BiPredicate<CAttachmentTarget, ServerPlayerEntity> syncPredicate) {
            var builder = fabricBuilder.syncWith(packetCodec, (at, sPlayer) -> syncPredicate.test(null, sPlayer));
            return new FabricATBuilder<T>(builder);
        }
    }
}
