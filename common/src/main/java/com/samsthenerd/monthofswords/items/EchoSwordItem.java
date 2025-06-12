package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.render.FakeGhostPlayerManager;
import net.minecraft.block.ShapeContext;
import net.minecraft.command.argument.EntityAnchorArgumentType.EntityAnchor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
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
                ghost.setPose(user.getPose());
                ghost.setGlowing(true);
                ghost.updatePose();
            });
        }
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(world.isClient()){
            FakeGhostPlayerManager.removePlayer();
        } else if(world instanceof ServerWorld sWorld){
            Vec3d tpPos = raycastForGhost(world, user);
            user.teleport(sWorld, tpPos.x, tpPos.y, tpPos.z, Set.of(), user.getYaw(), user.getPitch());
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
        if(hit.getType() == Type.BLOCK){
            if(hit.getSide() == Direction.UP) return hit.getPos();
            if(hit.getSide() == Direction.DOWN) return hit.getPos().subtract(new Vec3d(0,user.getHeight(),0));
            return hit.getPos();
        }
        return user.getRotationVector().multiply(16).add(user.getEyePos());
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
