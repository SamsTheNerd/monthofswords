package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.registry.SwordsModStatusEffects;
import com.samsthenerd.monthofswords.utils.FollowLeaderGoal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.SilverfishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class InfestationSwordItem extends SwordtemberItem {
    public static final ToolMaterial INFESTATION_MATERIAL = new ClassyToolMaterial(500, 5f, 3f,
        BlockTags.INCORRECT_FOR_STONE_TOOL, 15, () -> Ingredient.ofItems(Items.STONE));

    public InfestationSwordItem(Item.Settings itemSettings) {
        super(INFESTATION_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(INFESTATION_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(selected && entity instanceof LivingEntity livEnt && !world.isClient()){
            livEnt.addStatusEffect(new StatusEffectInstance(SwordsModStatusEffects.getEffect(SwordsModStatusEffects.FRIEND_OF_BUGS),
                20*15));
        }
    }

    public static List<Entity> findEnemies(Vec3d nearPos, ServerPlayerEntity owner){
        var enemyList = owner.getServerWorld().getOtherEntities(null,
            Box.of(nearPos, 16, 8, 16),
            ent -> ent instanceof HostileEntity hent && hent.isAlive() &&
                (hent.getTarget() == owner || hent.getTarget() instanceof SilverfishEntity));
        enemyList.sort(Comparator.comparing(ent -> ent.getPos().distanceTo(nearPos)));
        return enemyList;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if(user instanceof ServerPlayerEntity sPlayer){
            int timeHeld = 72000 - remainingUseTicks;
            int fishCount = Math.clamp(timeHeld / (5), 1, 6); // fish every quarter second
            sPlayer.getItemCooldownManager().set(this, 20*fishCount*3);
            var enemyList = findEnemies(sPlayer.getPos(), sPlayer);
            for(int i = 0; i < fishCount; i++){
                var bug = new SilverfishEntity(EntityType.SILVERFISH, sPlayer.getWorld()){

                    @Override
                    public void setTarget(@Nullable LivingEntity target) {
                        if(target == sPlayer) return; // friend
                        super.setTarget(target);
                    }

                    @Override
                    public void tick() {
                        LivingEntity tgt = this.getTarget();
                        if(tgt != null && !tgt.isAlive()){
                            var enemyList = findEnemies(this.getPos(), sPlayer);
                            if(!enemyList.isEmpty()){
                                var target = enemyList.get(sPlayer.getRandom().nextBetween(0, Math.min(enemyList.size()-1, 3)));
                                this.setTarget((HostileEntity) target);
                            } else {
                                this.setTarget(null);
                            }
                        }
                        if(tgt == null){
                            var lastFight = Math.max(this.getLastAttackedTime(), this.getLastAttackTime());
                            var peaceTime = this.age - lastFight;
                            if(peaceTime > 20 * 20 && lastFight != 0 && sPlayer.getRandom().nextFloat() < 0.003){
                                this.playSpawnEffects();
                                this.discard();
                            }
                        }
                        super.tick();
                    }

                    @Override
                    public boolean onKilledOther(ServerWorld world, LivingEntity other) {
                        return super.onKilledOther(world, other);
                    }

                    @Override
                    protected void initGoals() {
                        this.goalSelector.add(4, new FollowLeaderGoal(this, sPlayer, 1.0, 4.0F, 2.0F));
                        super.initGoals();
                        Predicate<Goal> isWander = goal -> goal instanceof WanderAroundGoal;
                        this.goalSelector.clear(isWander);
                    }
                };
                bug.setPosition(sPlayer.getPos());
                sPlayer.getServerWorld().spawnEntity(bug);
                if(!enemyList.isEmpty()){
                    var target = enemyList.get(sPlayer.getRandom().nextBetween(0, Math.min(enemyList.size()-1, 3)));
                    bug.setTarget((HostileEntity) target);
                }
            }
        }
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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(target instanceof LivingEntity livEnt && livEnt.getType().isIn(EntityTypeTags.ARTHROPOD)){
            return -100;
        }
        if(target instanceof LivingEntity livEnt && livEnt.hasStatusEffect(StatusEffects.INFESTED)){
            return 2f;
        } else return 0;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker.getWorld() instanceof ServerWorld sWorld
        && !target.getType().isIn(EntityTypeTags.ARTHROPOD)){
            var bugsList = sWorld.getOtherEntities(target,
                Box.of(target.getPos(), 16, 8, 16),
                ent -> ent.getType().isIn(EntityTypeTags.ARTHROPOD) && ent instanceof HostileEntity);
            for(var bug : bugsList){
                if(bug instanceof HostileEntity hostEnt){
                    var existTarget = hostEnt.getTarget();
                    if(existTarget == null || !existTarget.isAlive()
                        || target.distanceTo(bug) - existTarget.distanceTo(bug) < 2 || attacker.getRandom().nextFloat() < 0.2){
                        hostEnt.setTarget(target);
                    }
                }
            }
        }
        return super.postHit(stack, target, attacker);
    }

    /**
     * TODO: abstract silverfish control logic. make a goal for after they've killed
     */

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.GRAY);
    }
}
