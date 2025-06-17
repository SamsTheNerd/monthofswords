package com.samsthenerd.monthofswords.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.samsthenerd.monthofswords.registry.SwordsModAttributes;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EndermanEntity.class)
public class MixinEndermanStaring {
    @ModifyReturnValue(
        method="isPlayerStaring", at=@At("RETURN")
    )
    public boolean monthOfSwords$makePlayerNotStare(boolean originalStare, PlayerEntity player){
        if(!originalStare) return false;
        if(player.getWorld().isClient()) return originalStare;
        if(player.getAttributes().hasAttribute(SwordsModAttributes.ENDERMAN_FRIENDLY))
            return !(player.getAttributeValue(SwordsModAttributes.ENDERMAN_FRIENDLY) > 0);
        return originalStare;
    }
}
