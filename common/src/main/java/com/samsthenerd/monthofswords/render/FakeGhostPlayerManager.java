package com.samsthenerd.monthofswords.render;

import com.mojang.authlib.GameProfile;
import com.samsthenerd.monthofswords.mixins.MixinLivingEntAccessor;
import com.samsthenerd.monthofswords.utils.LivingEntDuck;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class FakeGhostPlayerManager {

    private static final Map<GameProfile, GhostlyPlayerEntity> GHOST_PLAYERS = new HashMap<>();

    public static void makeFakePlayer(GameProfile profile, PlayerEntity player){
        ClientWorld cWorld =  MinecraftClient.getInstance().world;
        var ghost = new GhostlyPlayerEntity(cWorld, MinecraftClient.getInstance().getGameProfile(), player);
        ghost.setUuid(UUID.randomUUID());
        ghost.noClip = true;
        cWorld.addEntity(ghost);
        GHOST_PLAYERS.put(profile, ghost);
    }

    public static void removePlayer(GameProfile profile){
        var ghost = GHOST_PLAYERS.get(profile);
        if(ghost == null) return;
        ghost.discard();
        GHOST_PLAYERS.remove(profile);
    }

    public static Optional<GhostlyPlayerEntity> getGhostPlayer(GameProfile profile){
        return Optional.ofNullable(GHOST_PLAYERS.get(profile));
    }

    public static void setPlayerPosition(GameProfile profile, Vec3d pos){
        getGhostPlayer(profile).ifPresent(ghost -> ghost.setPosition(pos));
    }

    public static void makeFakePlayer(){
        makeFakePlayer(MinecraftClient.getInstance().getGameProfile(), MinecraftClient.getInstance().player);
    }

    public static void removePlayer(){
        removePlayer(MinecraftClient.getInstance().getGameProfile());
    }

    public static Optional<GhostlyPlayerEntity> getGhostPlayer(){
        return getGhostPlayer(MinecraftClient.getInstance().getGameProfile());
    }

    public static void setPlayerPosition(Vec3d pos){
        setPlayerPosition(MinecraftClient.getInstance().getGameProfile(), pos);
    }

    public static class GhostlyPlayerEntity extends OtherClientPlayerEntity{

        public PlayerEntity playerBody;

        public GhostlyPlayerEntity(ClientWorld clientWorld, GameProfile gameProfile, PlayerEntity playerBody){
            super(clientWorld, gameProfile);
            this.playerBody = playerBody;
        }

        @Override
        public boolean collidesWith(Entity other) {
            return false;
        }

//        @Override
//        public boolean collidesWithStateAtPos(BlockPos pos, BlockState state) {
//            return super.collidesWithStateAtPos(pos, state);
//        }
//
//        @Override
//        protected void checkBlockCollision() {
//            super.checkBlockCollision();
//        }

        @Override
        public SkinTextures getSkinTextures() {
            return MinecraftClient.getInstance().getSkinProvider().getSkinTextures(getGameProfile());
        }

        @Override
        public boolean isPartVisible(PlayerModelPart modelPart) {
            return modelPart != PlayerModelPart.CAPE; // cape looks weird. idk maybe make it configurable or something
        }

        @Override
        public boolean shouldRenderName() {
            return false;
        }

        @Override
        public boolean isGlowing() {
            return !playerBody.canSee(this);
//            return super.isGlowing();
        }

        @Override
        public void tick() {
            if(((LivingEntDuck)playerBody).getLastEchoUsage() == -1){
                this.discard();
                removePlayer(getGameProfile());
                return;
            }
            getWorld().addParticle(
                ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS,
                getX()+getRandom().nextDouble()-0.5,
                getY()+getRandom().nextDouble(),
                getZ()+getRandom().nextDouble()-0.5,
                0, 0, 0
            );
            super.tick();
            if(this.isInSwimmingPose()){
                ((MixinLivingEntAccessor)this).mos$setLeaningPitch(1);
                ((MixinLivingEntAccessor)this).mos$setLastLeaningPitch(1);
            } else {
                ((MixinLivingEntAccessor)this).mos$setLeaningPitch(0);
                ((MixinLivingEntAccessor)this).mos$setLastLeaningPitch(0);
            }
        }

        @Override
        public void updatePose() {
//            SwordsMod.LOGGER.info("boop");
            if (this.canChangeIntoPose(EntityPose.SWIMMING)) {
                EntityPose entityPose = this.isFallFlying() ? EntityPose.FALL_FLYING : (this.isSleeping() ? EntityPose.SLEEPING : (this.isSwimming() ? EntityPose.SWIMMING : (this.isUsingRiptide() ? EntityPose.SPIN_ATTACK : (this.isSneaking() && !this.getAbilities().flying ? EntityPose.CROUCHING : EntityPose.STANDING))));
                EntityPose entityPose2 = this.isSpectator() || this.hasVehicle() || this.canChangeIntoPose(entityPose) ? entityPose : (this.canChangeIntoPose(EntityPose.CROUCHING) ? EntityPose.CROUCHING : EntityPose.SWIMMING);
                this.setPose(entityPose2);
            }
            if(this.canChangeIntoPose(playerBody.getPose())){
                this.setPose(playerBody.getPose());
            }
        }

        @Override
        public boolean canChangeIntoPose(EntityPose pose) {
            return super.canChangeIntoPose(pose);
        }

        @Override
        protected void tickCramming() {
//            super.tickCramming();
        }

        @Override
        public void pushOutOfBlocks(double x, double y, double z) {
            super.pushOutOfBlocks(x, y, z);
        }
    }

}
