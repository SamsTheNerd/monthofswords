package com.samsthenerd.monthofswords.neoforge.xplat;

import com.samsthenerd.monthofswords.neoforge.xplat.NFAttachmentType.NFATBuilder;
import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import com.samsthenerd.monthofswords.xplat.CAttachmentType.Builder;
import com.samsthenerd.monthofswords.xplat.SwordsModXPlat;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SwordsModXPlatNF implements SwordsModXPlat {

    @Override
    public <T> Builder<T> getAttachmentBuilder(Supplier<T> defaultValue) {
        return new NFATBuilder<>(AttachmentType.builder(defaultValue));
    }

    public static final Map<Identifier, AttachmentType<?>> ATTACHMENT_TYPES = new HashMap<>();

    @Override
    public <T> CAttachmentType<T> registerAttachment(Identifier id, Builder<T> builder) {
        var attType = ((NFATBuilder<T>)builder).nfBuilder().build();
        ATTACHMENT_TYPES.put(id, attType);
        return new NFAttachmentType<>(attType);
    }

    @Override
    public CAttachmentTarget getEntityTarget(Entity ent) {
        return new NFAttachmentTarget(ent);
    }
}
