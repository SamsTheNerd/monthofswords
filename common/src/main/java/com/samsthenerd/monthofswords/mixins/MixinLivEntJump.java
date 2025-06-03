package com.samsthenerd.monthofswords.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.samsthenerd.monthofswords.items.WovenSwordItem;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.registry.SwordsModStatusEffects.FriendOfEntityStatusEffect;
import com.samsthenerd.monthofswords.utils.LivingEntDuck;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * this is yoinked hard from artifact's cloud in a bottle:
 * https://github.com/florensie/artifacts-fabric/blob/3a4e29d152172a5424a84b11ee2b9755c4be6c56/src/main/java/artifacts/mixin/item/cloudinabottle/LivingEntityMixin.java
 * via gloop. so has some artifacts from that maybe
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivEntJump extends Entity implements LivingEntDuck {
    @Shadow
    protected boolean jumping;
    // Is entity double jumping in this tick
//    @Unique
//    private boolean isDoubleJumping = false;
    // Has entity released jump key since last jump
    @Unique
    private boolean jumpWasReleased = false;
//    // Has entity double jumped during current airtime
//    @Unique
//    private boolean hasDoubleJumped = false;

    @Unique
    private long startedWovenDash = -1;

    @Shadow
    public abstract boolean isClimbing();

    @Inject(method="tickMovement()V", at=@At("HEAD"))
    public void checkForHittingTheGround(CallbackInfo info){
        LivingEntity self = (LivingEntity) (Object) this;
        jumpWasReleased |= !this.jumping;

//
//        boolean flying = self instanceof PlayerEntity player && player.getAbilities().flying;
//        if (this.jumping && this.jumpWasReleased && !this.hasVehicle() && !flying
//            && lastHitGroundAfterDash != -1 && self.getWorld().getTime() - lastHitGroundAfterDash <= 5) {
//            SwordsMod.LOGGER.info("maybe jump?");
////            this.hasDoubleJumped = true;
//        }

        long dashLength = startedWovenDash == -1 ? -1 : self.getWorld().getTime() - startedWovenDash;

        // if we hit the ground reset our thing and track when that happened
        if ((this.isOnGround() || this.isClimbing()) && !this.isTouchingWater()) {
            if(dashLength > 10 && self instanceof ServerPlayerEntity user){
                startedWovenDash = -1;
                user.getItemCooldownManager().set(SwordsModItems.WOVEN_SWORD.get(), 0);
            }
        }

        // do some sparkles :p
        if(self instanceof ServerPlayerEntity user){
            if(dashLength >= 0){
                WovenSwordItem.makeTransParticles(user);
            }
        }
    }

    @Inject(method = "jump", at = @At("RETURN"))
    private void setJumpReleased(CallbackInfo info) {
        this.jumpWasReleased = false;
    }

//    @Inject(method = "setJumping", at = @At("HEAD"))
//    private void hookListenToSetJumping(boolean jumpingInput, CallbackInfo ci) {
//        if(jumpingInput && !jumping) SwordsMod.LOGGER.info("setJumping");
////        this.jumpWasReleased = false;
//    }

    // garbage to shut the compiler up
    public MixinLivEntJump(EntityType<?> type, World world){
        super(type, world);
    }

    @Override
    public boolean isDashingTransgenderly(){
        return startedWovenDash != -1;
    }

    @Override
    public void makeDashTransgenderly(){
        LivingEntity self = (LivingEntity) (Object) this;
        startedWovenDash = self.getWorld().getTime();
    }


    @ModifyReturnValue(
        method="Lnet/minecraft/entity/LivingEntity;canTarget(Lnet/minecraft/entity/LivingEntity;)Z",
        at = @At("RETURN")
    )
    public boolean monthOfSwords$becomeUntargetable(boolean original, LivingEntity target){
        if(original){
            for(StatusEffectInstance effInst : target.getStatusEffects()){
                if(effInst.getEffectType().value() instanceof FriendOfEntityStatusEffect friendEff
                && friendEff.friendPredicate.test(this)){
                    return false;
                }
            }

        }
        return original;
    }
}
