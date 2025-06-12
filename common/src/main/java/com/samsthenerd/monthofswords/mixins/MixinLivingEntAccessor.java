package com.samsthenerd.monthofswords.mixins;

import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface MixinLivingEntAccessor {
    @Accessor("leaningPitch")
    void mos$setLeaningPitch(float leanPitch);

    @Accessor("lastLeaningPitch")
    void mos$setLastLeaningPitch(float leanPitch);
}
