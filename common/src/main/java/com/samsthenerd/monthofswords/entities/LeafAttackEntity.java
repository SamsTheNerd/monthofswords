package com.samsthenerd.monthofswords.entities;

import com.samsthenerd.monthofswords.registry.SwordsModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class LeafAttackEntity extends ThrownItemEntity{

    public LeafAttackEntity(World world) {
        this(SwordsModEntities.LEAF_ATTACK.get(), world);
    }

    public LeafAttackEntity(EntityType<? extends LeafAttackEntity> type, World world) {
        super(type, world);
    }

    protected void onEntityHit(EntityHitResult hit) {
        if(!(getWorld() instanceof ServerWorld sWorld)) return;
        hit.getEntity().damage(getDamageSources().magic(), 1);
        hit.getEntity().addVelocity(
            this.getVelocity().multiply(1,0,1).normalize()
                .multiply(0.5, 0, 0.5).add(0,0.4,0));

        sWorld.spawnParticles(ParticleTypes.CHERRY_LEAVES,
            hit.getPos().getX() + getRandom().nextDouble(),
            hit.getPos().getY() + getRandom().nextDouble(),
            hit.getPos().getZ() + getRandom().nextDouble(),
            3,
            0, 0, 0, 0);
    }

    @Override
    protected void onBlockHit(BlockHitResult hit) {
//        if(getWorld() instanceof ServerWorld sWorld && hit.getType() == Type.BLOCK){
//            if(getRandom().nextFloat() < 0.1){
//                var petalEnt = new ItemEntity(sWorld, hit.getPos().getX(), hit.getPos().getY(), hit.getPos().getZ(),
//                    new ItemStack(SwordsModItems.FOREST_PETALS.get()));
//                sWorld.spawnEntity(petalEnt);
//            }
//        }
        super.onBlockHit(hit);
    }

    @Override
    public void tick() {
        if(getWorld() instanceof ServerWorld sWorld){
            sWorld.spawnParticles(ParticleTypes.CHERRY_LEAVES,
                getPos().getX() + getRandom().nextDouble(),
                getPos().getY() + getRandom().nextDouble(),
                getPos().getZ() + getRandom().nextDouble(),
                1,
                0, 0, 0, 0);
        }
        super.tick();
    }

    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!this.getWorld().isClient) {
            this.discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return Items.PINK_PETALS;
    }
}
