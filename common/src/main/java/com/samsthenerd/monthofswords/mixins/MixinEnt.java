package com.samsthenerd.monthofswords.mixins;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public class MixinEnt {
//    @Inject(
//        method="teleportTo", at=@At("HEAD"), cancellable = true
//    )
//    public void monthOfSwords$cancelEntTeleport(TeleportTarget teleportTarget, CallbackInfoReturnable<Entity> cir){
//        if(((Object)this) instanceof LivingEntity livEnt
//        && livEnt.hasStatusEffect(SwordsModStatusEffects.getEffect(SwordsModStatusEffects.DISPLACED))){
//            cir.setReturnValue(livEnt);
//        }
//    }
}
