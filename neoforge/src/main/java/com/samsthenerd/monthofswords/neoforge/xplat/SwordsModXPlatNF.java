package com.samsthenerd.monthofswords.neoforge.xplat;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.neoforge.xplat.NFAttachmentType.NFATBuilder;
import com.samsthenerd.monthofswords.xplat.CAttachmentTarget;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import com.samsthenerd.monthofswords.xplat.CAttachmentType.Builder;
import com.samsthenerd.monthofswords.xplat.SwordsModXPlat;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SwordsModXPlatNF implements SwordsModXPlat {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES_REGISTRY = DeferredRegister.create(
        NeoForgeRegistries.Keys.ATTACHMENT_TYPES, SwordsMod.MOD_ID);

    @Override
    public <T> Builder<T> getAttachmentBuilder(Supplier<T> defaultValue) {
        return new NFATBuilder<>(AttachmentType.builder(defaultValue));
    }

    public static final Map<Identifier, AttachmentType<?>> ATTACHMENT_TYPES = new HashMap<>();

    @Override
    public <T> CAttachmentType<T> registerAttachment(Identifier id, Builder<T> builder) {
        var attType = ((NFATBuilder<T>)builder).nfBuilder().build();
//        var holder = ATTACHMENT_TYPES_REGISTRY.register(id.toString(), attType::build);
        ATTACHMENT_TYPES.put(id, attType);
        return new NFAttachmentType<>(attType);
    }

    @Override
    public CAttachmentTarget getEntityTarget(Entity ent) {
        return new NFAttachmentTarget(ent);
    }
}
