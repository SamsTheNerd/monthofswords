package com.samsthenerd.monthofswords.fabric.xplat;

import com.samsthenerd.monthofswords.fabric.xplat.FabricAttachmentType.FabricATBuilder;
import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import com.samsthenerd.monthofswords.xplat.CAttachmentType.Builder;
import com.samsthenerd.monthofswords.xplat.SwordsModXPlat;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

public class SwordsModXPlatFabric implements SwordsModXPlat {
    @Override
    public <T> Builder<T> getAttachmentBuilder(Supplier<T> defaultValue) {
        AttachmentRegistry.Builder<T> builder = AttachmentRegistry.builder();
        builder.initializer(defaultValue);
        return new FabricATBuilder<>(builder);
    }

    @Override
    public <T> CAttachmentType<T> registerAttachment(Identifier id, Builder<T> builder) {
        return new FabricAttachmentType<T>(((FabricATBuilder<T>)builder).fabricBuilder().buildAndRegister(id));
    }

    @Override
    public CAttachmentTarget getEntityTarget(Entity ent) {
        return new FabricAttachmentTarget(ent);
    }
}
