package com.samsthenerd.monthofswords.xplat;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface SwordsModXPlat {

    static SwordsModXPlat getInstance(){
        return SwordsMod.XPLAT_INSTANCE;
    }

    <T> CAttachmentType.Builder<T> getAttachmentBuilder(Supplier<T> defaultValue);

    // should be called before registration time
    <T> CAttachmentType<T> registerAttachment(Identifier id, CAttachmentType.Builder<T> builder);

    default <T> CAttachmentType<T> createAttachment(Identifier id, Supplier<T> defaultValue, UnaryOperator<CAttachmentType.Builder<T>> instructions){
        return registerAttachment(id, instructions.apply(getAttachmentBuilder(defaultValue)));
    }

    CAttachmentTarget getEntityTarget(Entity ent);
}
