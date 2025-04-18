package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.utils.BFSHelper;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class FlameSwordItem extends SwordtemberItem implements SwordActionHaverServer{

    public static final ToolMaterial FLAME_MATERIAL = new ClassyToolMaterial(1500, 7f, 3f,
            BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 14, () -> Ingredient.ofItems(Items.BLAZE_POWDER));

    public FlameSwordItem(Item.Settings itemSettings) {
        super(FLAME_MATERIAL, itemSettings.attributeModifiers(
                SwordItem.createAttributeModifiers(FLAME_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(damageSource.getAttacker() instanceof LivingEntity attacker){
            return attacker.isOnFire() ? 1f : 0;
        }
        return 0;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        Random random = world.getRandom();
        world.playSound(null, attacker.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.PLAYERS, 0.5f, (random.nextFloat() - random.nextFloat()) * 0.2f + 2f);
        target.setOnFireForTicks(100);
        return super.postHit(stack, target, attacker);
    }

    private static SmallFireballEntity makeSafeFireball(PlayerEntity player, Vec3d vel){
        return new SmallFireballEntity(player.getWorld(), player, vel){
            @Override
            protected void onBlockHit(BlockHitResult bhs){
                if(SwordsMod.canBeDestructive(player, bhs.getBlockPos())){
                    super.onBlockHit(bhs);
                } else {
                    BlockState blockState = this.getWorld().getBlockState(bhs.getBlockPos());
                    blockState.onProjectileHit(this.getWorld(), blockState, bhs, this);
                    List<Entity> nearbyEnts = player.getWorld().getOtherEntities(player, new Box(bhs.getBlockPos()).expand(2));
                    for(Entity target : nearbyEnts){
                        target.setOnFireForTicks(100);
                    }
                }
            }
        };
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(player.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.pass(stack);

        SmallFireballEntity fireball = makeSafeFireball(player, player.getRotationVector().multiply(3));
        Vec3d lookVec = player.getRotationVector();
        fireball.setPosition(player.getPos().add(lookVec.x, 1.4, lookVec.z));
        player.getWorld().spawnEntity(fireball);
        player.getItemCooldownManager().set(this, 50);
        stack.damage(1, player, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        Random random = world.getRandom();
        world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.BLOCKS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
        return TypedActionResult.success(stack);

    }

    @Override
    public boolean doSwordAction(PlayerEntity player, ItemStack swordStack) {

        if (player.getItemCooldownManager().isCoolingDown(this)) return false;

        World world = player.getWorld();
        Vec3d lookVec = player.getRotationVector();

        Vec3d sideVec = lookVec.crossProduct(new Vec3d(0, 1, 0)).normalize().multiply(0.1f);
        Vec3d[] sideVels = {sideVec, new Vec3d(0, 0, 0), sideVec.multiply(-1)};
        Vec3d[] verVels = {new Vec3d(0, 0.1f, 0), new Vec3d(0, 0, 0), new Vec3d(0, -0.1f, 0)};

        for (Vec3d vVel : verVels) {
            for (Vec3d hVel : sideVels) {
                SmallFireballEntity fireballCenter = makeSafeFireball(player,
                    player.getRotationVector().add(vVel).add(hVel).multiply(3));
                fireballCenter.setPosition(player.getPos().add(lookVec.x, 1.4, lookVec.z));
                player.getWorld().spawnEntity(fireballCenter);
            }
        }

        player.setStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 400), player);

        if (SwordsMod.canBeDestructive(player, null)) {
            Map<BlockPos, Integer> fireZone = BFSHelper.runBFS(world, player.getBlockPos(),
                (worldArg, pos, dist) -> true, 3, false);

            for (BlockPos fPos : fireZone.keySet()) {
                if (world.getBlockState(fPos).getBlock() == Blocks.AIR
                    && world.getBlockState(fPos.offset(Direction.DOWN)).getBlock() != Blocks.AIR) {
                    world.setBlockState(fPos, AbstractFireBlock.getState(world, fPos));
                }
            }
        } else {
            List<Entity> nearbyEnts = world.getOtherEntities(player, new Box(player.getBlockPos()).expand(3));
            for(Entity target : nearbyEnts){
                target.setOnFireForTicks(100);
            }
        }

        Random random = world.getRandom();
        player.getItemCooldownManager().set(this, 200);
        world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.PLAYERS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
        swordStack.damage(15, player, player.getMainHandStack() == swordStack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        return true;
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0xfc6f03);
    }
}
