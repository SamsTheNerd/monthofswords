package com.samsthenerd.monthofswords.utils;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.EnumSet;

public class FollowLeaderGoal extends Goal {
    private final PathAwareEntity ent;
    private LivingEntity owner;
    private final double speed;
    private final EntityNavigation navigation;
    private int updateCountdownTicks;
    private final float maxDistance;
    private final float minDistance;
    private float oldWaterPathfindingPenalty;

    public FollowLeaderGoal(PathAwareEntity ent, LivingEntity owner, double speed, float minDistance, float maxDistance) {
        this.ent = ent;
        this.speed = speed;
        this.owner = owner;
        this.navigation = ent.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        if (!(ent.getNavigation() instanceof MobNavigation) && !(ent.getNavigation() instanceof BirdNavigation)) {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    public boolean canStart() {
        LivingEntity livingEntity = this.owner;
        if (livingEntity == null || (livingEntity instanceof ServerPlayerEntity sPlayer && sPlayer.isDisconnected())) {
            return false;
        } else if (this.ent.getTarget() != null && this.ent.getTarget().isAlive()) {
            return false;
        } else if (this.ent.squaredDistanceTo(livingEntity) < (double)(this.minDistance * this.minDistance)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean shouldContinue() {
//        if (this.navigation.isIdle()) {
//            return false;
//        } else
        if ((owner instanceof ServerPlayerEntity sPlayer && sPlayer.isDisconnected())) {
            return false;
        } else if (this.ent.getTarget() != null && this.ent.getTarget().isAlive()){
            return false;
        } else if (this.owner.age - this.owner.getLastAttackedTime() < 1000
            && this.owner.getLastAttacker() != null && this.owner.getLastAttacker().isAlive()){
            this.ent.setTarget(this.owner.getLastAttacker());
            return false;
        } else {
            return !(this.ent.squaredDistanceTo(this.owner) <= (double)(this.maxDistance * this.maxDistance));
        }
    }

    public void start() {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.ent.getPathfindingPenalty(PathNodeType.WATER);
        this.ent.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
    }

    public void stop() {
        this.navigation.stop();
        this.ent.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
    }

    public void tick() {
        this.ent.getLookControl().lookAt(this.owner, 10.0F, (float)this.ent.getMaxLookPitchChange());

        if (--this.updateCountdownTicks <= 0) {
            this.updateCountdownTicks = this.getTickCount(10);
            this.navigation.startMovingTo(this.owner, this.speed);
        }
    }
}