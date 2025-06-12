package com.samsthenerd.monthofswords.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntityRenderer.class)
public class MixinTurnOffLabel {
    @ModifyReturnValue(
        method = "hasLabel(Lnet/minecraft/entity/LivingEntity;)Z",
        at = @At("RETURN")
    )
    private boolean monthofswords$killPlayerLabel(boolean original, LivingEntity entity){
        if(!original) return original;
        if(entity instanceof PlayerEntity otherPlayer){
            return otherPlayer.shouldRenderName();
        }
        return original;
    }
}