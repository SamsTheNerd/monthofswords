package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.utils.BFSHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class IceSwordItem extends SwordtemberItem implements SwordActionHaverServer{

    public static final ToolMaterial ICE_MATERIAL = new ClassyToolMaterial(1000, 7.5f, 1.5f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 20, () -> Ingredient.ofItems(Items.BLUE_ICE));

    public static final Identifier ICE_SWORD_NO_BURN_MODIFIER = SwordsMod.id("iceswordnoburn");

    // TODO: make this data driven maybe?
    private static final Map<Block, BlockState> FREEZE_MAP = new HashMap<>();

    static {
        FREEZE_MAP.put(Blocks.WATER, Blocks.FROSTED_ICE.getDefaultState());
        FREEZE_MAP.put(Blocks.FROSTED_ICE, Blocks.ICE.getDefaultState());
        FREEZE_MAP.put(Blocks.ICE, Blocks.PACKED_ICE.getDefaultState());
        FREEZE_MAP.put(Blocks.PACKED_ICE, Blocks.BLUE_ICE.getDefaultState());
        FREEZE_MAP.put(Blocks.LAVA, Blocks.OBSIDIAN.getDefaultState());
    }

    public IceSwordItem(Item.Settings itemSettings) {
        super(ICE_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ICE_MATERIAL, 3, -2.4f)
                .with(
                    EntityAttributes.GENERIC_BURNING_TIME,
                    new EntityAttributeModifier(ICE_SWORD_NO_BURN_MODIFIER, -10.0, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.MAINHAND
            ))
        );
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(damageSource.getAttacker() instanceof LivingEntity attacker){
            return attacker.isFrozen() ? 1f : 0;
        }
        return 0;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        World world = attacker.getWorld();
        Random random = world.getRandom();
        world.playSound(null, attacker.getBlockPos(), SoundEvents.BLOCK_GLASS_BREAK, SoundCategory.PLAYERS, 0.5f, (random.nextFloat() - random.nextFloat()) * 0.2f + 2f);
        target.setInPowderSnow(true);
        // 1 damage every 40 ticks (2 seconds) when frozen
        // entities take damage when freezeTicks >= 140
        // when not actively freezing, freezeTicks go down 2 ft per tick.
        // ((totalFreeze - 140)/2)/40 == amount of damage-ish
        // totalFreeze ~= damage * 80 + 140
        // fair to take like, 3 damage from it? - note that isn't per hit, necessarily. like it won't stack between quick consecutive hits
        target.setFrozenTicks(Math.max(380, target.getFrozenTicks()));
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        Random random = world.getRandom();
        if(!player.getItemCooldownManager().isCoolingDown(this)){
            SnowballEntity snowball = new SnowballEntity(player.getWorld(), player){
                @Override
                protected void onEntityHit(EntityHitResult entityHitResult) {
                    super.onEntityHit(entityHitResult);
                    Entity entity = entityHitResult.getEntity();
                    entity.setInPowderSnow(true);
                    entity.setFrozenTicks(Math.max(250, entity.getFrozenTicks()));
                }
            };
            snowball.setVelocity(player.getRotationVector().multiply(1.5f));
            Vec3d lookVec = player.getRotationVector();
            snowball.setPosition(player.getPos().add(lookVec.x, 1.4, lookVec.z));
            player.getWorld().spawnEntity(snowball);
            player.getItemCooldownManager().set(this, 30);
            stack.damage(1, player, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
            world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_POWDER_SNOW_PLACE, SoundCategory.BLOCKS, 2f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public boolean doSwordAction(PlayerEntity player, ItemStack swordStack){

        if(player.getItemCooldownManager().isCoolingDown(this)) return false;

        World world = player.getWorld();
        Vec3d lookVec = player.getRotationVector();

        Random random = world.getRandom();
        List<Entity> nearbyEnts = world.getOtherEntities(player, new Box(player.getBlockPos()).expand(6));

        for(Entity target : nearbyEnts){
            target.setInPowderSnow(true);
            target.setFrozenTicks(Math.max(460, target.getFrozenTicks()));
            world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_BUCKET_EMPTY_POWDER_SNOW, SoundCategory.PLAYERS, 2.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
        }

        Map<BlockPos, Integer> iceZone = BFSHelper.runBFS(world, player.getBlockPos(),
            (worldArg, pos, dist) -> {
                Block block = worldArg.getBlockState(pos).getBlock();
                return dist < 2 || FREEZE_MAP.containsKey(block);
            }, 4, true);

        for(BlockPos fPos : iceZone.keySet()){
            Block fBlock = world.getBlockState(fPos).getBlock();
            BlockState newState = FREEZE_MAP.get(fBlock);
            if(newState != null){
                world.setBlockState(fPos, newState);
            }
        }

        player.getItemCooldownManager().set(this, 50 + Math.min(nearbyEnts.size() * 20, 300));
        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_POWDER_SNOW_HIT, SoundCategory.PLAYERS, 1.0f, (random.nextFloat() - random.nextFloat()) * 0.2f + 1.0f);
        swordStack.damage(3 + Math.min(nearbyEnts.size() * 2, 12), player, player.getMainHandStack() == swordStack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        return true;
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0x5eefff);
    }
}
