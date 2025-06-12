package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.render.FakeGhostPlayerManager;
import com.samsthenerd.monthofswords.utils.LivingEntDuck;
import net.minecraft.block.ShapeContext;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import net.minecraft.world.World;

import java.util.Set;

public class EchoSwordItem extends SwordtemberItem {

    public static final ToolMaterial ECHO_MATERIAL = new ClassyToolMaterial(2500, 5f, 4f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 18, () -> Ingredient.ofItems(Items.ECHO_SHARD));


    public EchoSwordItem(Item.Settings itemSettings) {
        super(ECHO_MATERIAL, itemSettings.attributeModifiers(
                SwordItem.createAttributeModifiers(ECHO_MATERIAL, 3, -2f)
            )
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(world.isClient()){
            FakeGhostPlayerManager.makeFakePlayer();
        }
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(world.isClient()){
            Vec3d tpPos = raycastForGhost(world, user);
            FakeGhostPlayerManager.setPlayerPosition(tpPos);
            FakeGhostPlayerManager.getGhostPlayer().ifPresent(ghost -> {

                ghost.lookAt(EntityAnchor.FEET,
                    ghost.getEyePos().subtract(user.getEyePos()).add(ghost.getEyePos()));
                ghost.lookAt(EntityAnchor.EYES, user.getRotationVector().add(ghost.getEyePos()));
                ghost.updatePose();
                if(ghost.canChangeIntoPose(user.getPose())){
                    ghost.setPose(user.getPose());
                }
//                if(ghost.isInsideWall()){
//                    ghost.setOnFire(true);
//                } else {
//                    ghost.setOnFire(false);
//                }
            });
        }
        ((LivingEntDuck)user).setLastEchoUsage(user.age);
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(world.isClient()){
            FakeGhostPlayerManager.removePlayer();
        } else if(world instanceof ServerWorld sWorld){
            Vec3d tpPos = raycastForGhost(world, user);
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_SCULK_CHARGE,
                SoundCategory.PLAYERS, 50f,
                user.getRandom().nextFloat() * 0.2f + 0.5f);

            for(int i = 0; i < 5; i++){
                sWorld.spawnParticles(
                    ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS,
                    user.getX()+user.getRandom().nextDouble()-0.5,
                    user.getY()+user.getRandom().nextDouble(),
                    user.getZ()+user.getRandom().nextDouble()-0.5,
                    1,
                    0, 0, 0, 0
                );
            }

            user.teleport(sWorld, tpPos.x, tpPos.y, tpPos.z, Set.of(), user.getYaw(), user.getPitch());

            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_SCULK_CHARGE,
                SoundCategory.PLAYERS, 100f,
                user.getRandom().nextFloat() * 0.2f + 0.5f);

            for(int i = 0; i < 10; i++){
                sWorld.spawnParticles(
                    ParticleTypes.TRIAL_SPAWNER_DETECTION_OMINOUS,
                    user.getX()+user.getRandom().nextDouble()-0.5,
                    user.getY()+user.getRandom().nextDouble(),
                    user.getZ()+user.getRandom().nextDouble()-0.5,
                    1,
                    0, 0, 0, 0
                );
            }
        }
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }

    public static Vec3d raycastForGhost(World world, LivingEntity user){
        BlockHitResult hit = world.raycast(new RaycastContext(
            user.getEyePos(),
            user.getRotationVector().multiply(32).add(user.getEyePos()),
            ShapeType.VISUAL,
            FluidHandling.NONE,
            ShapeContext.of(user)
        ));
        // height we should aim for, but we'd maybe fit into smaller
        double idealHeight = user.getDimensions(user.isSneaking() ? EntityPose.CROUCHING : EntityPose.STANDING).height();
        double smallestFitHeight = user.getDimensions(EntityPose.SWIMMING).height();

        Vec3d anchorPos = user.getRotationVector().multiply(16).add(user.getEyePos());
        if(hit.getType() == Type.BLOCK){
            if(hit.getSide().getAxis().isVertical()){
                anchorPos =  hit.getPos();
            } else if(hit.getSide().getAxis().isHorizontal()){
                Vec3i dirVec = hit.getSide().getVector();
                Vec3d normVec = new Vec3d(dirVec.getX(), dirVec.getY(), dirVec.getZ());
                BlockHitResult downHit = world.raycast(new RaycastContext(
                    hit.getPos().add(0, 0.5, 0).add(normVec.multiply(0.01)),
                    hit.getPos().add(normVec.multiply(-0.1)),
                    ShapeType.VISUAL,
                    FluidHandling.NONE,
                    ShapeContext.of(user)
                ));
                if(downHit.getType() == Type.BLOCK && downHit.getSide() == Direction.UP && !downHit.isInsideBlock()){
                    anchorPos = downHit.getPos();
                } else {
                    anchorPos = hit.getPos().add(normVec.multiply(user.getWidth()/2));
                }
            } else {
                anchorPos = hit.getPos();
            }
        }
        // see if the head will be in something and if we can fix that
        BlockHitResult upHit = world.raycast(new RaycastContext(
           anchorPos, anchorPos.add(0, idealHeight - 1E-6, 0),
            ShapeType.VISUAL, FluidHandling.NONE, ShapeContext.of(user)
        ));
        if(upHit.getType() == Type.BLOCK){
            // head is hitting something
            BlockHitResult backDownHit = world.raycast(new RaycastContext(
               upHit.getPos(), upHit.getPos().add(0, -idealHeight, 0),
                ShapeType.VISUAL, FluidHandling.NONE, ShapeContext.of(user)
            ));
            if(backDownHit.getType() == Type.BLOCK){
                // there's a surface somewhere between here and our ideal height, put self there
                return backDownHit.getPos();
            } else {
                // no surface, just plop down at the height we're at
                return upHit.getPos().subtract(0, idealHeight, 0);
            }
        }
        return anchorPos;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(world.isClient){
            FakeGhostPlayerManager.removePlayer();
        }
        return super.finishUsing(stack, world, user);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        if(user instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(this)) return 0;
        return 72000;
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }
}
