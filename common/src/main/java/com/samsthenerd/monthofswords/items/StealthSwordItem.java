package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.registry.SwordsModStatusEffects;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.FireworkExplosionComponent;
import net.minecraft.component.type.FireworksComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.UnaryOperator;

public class StealthSwordItem extends SwordItem {

    private static final Identifier STEALTH_SWORD_SNEAK_MODIFIER = Identifier.of(SwordsMod.MOD_ID, "stealthswordsneak");
    private static final Identifier STEALTH_SWORD_FALL_MODIFIER = Identifier.of(SwordsMod.MOD_ID, "stealthswordfall");

    private static final FireworksComponent DEFAULT_FIREWORK = new FireworksComponent(1,
            List.of(new FireworkExplosionComponent(
                    FireworkExplosionComponent.Type.SMALL_BALL,
                    IntList.of(Formatting.BLACK.getColorValue()),
                    IntList.of(Formatting.BLACK.getColorValue(), Formatting.DARK_RED.getColorValue()),
                    false, false
            )
        )
    );

    public StealthSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.IRON, itemSettings.attributeModifiers(
                SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -2.4f)
                        .with(
                                EntityAttributes.PLAYER_SNEAKING_SPEED,
                                new EntityAttributeModifier(STEALTH_SWORD_SNEAK_MODIFIER, 0.4, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        ).with(
                                EntityAttributes.GENERIC_SAFE_FALL_DISTANCE,
                                new EntityAttributeModifier(STEALTH_SWORD_FALL_MODIFIER, 2, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        )
        ));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean ready = !user.getItemCooldownManager().isCoolingDown(this);
        ItemStack handStack = user.getStackInHand(hand);
        if(world instanceof ServerWorld sWorld && ready){
            if(!user.isSneaking()){
                ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET);
                rocketStack.set(
                        DataComponentTypes.FIREWORKS,
                        user.getStackInHand(hand).getComponents().getOrDefault(DataComponentTypes.FIREWORKS, DEFAULT_FIREWORK)
                );
                FireworkRocketEntity rocketEntity = new FireworkRocketEntity(world, rocketStack, null, user.getX(), user.getEyeY() - (double)0.15f, user.getZ(), true);
                rocketEntity.setVelocity(user.getRotationVec(0));
                sWorld.spawnEntity(rocketEntity);

                // let the user escape by making all nearby mobs untarget them and all players have brief blindness
                for(Entity nearbyEnt : world.getOtherEntities(user, new Box(user.getEyePos(), user.getPos()).expand(user.getEntityInteractionRange()*2))){
                    if(nearbyEnt instanceof LivingEntity liveEnt){
                        liveEnt.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * 3)); // give them blindness for 3 seconds
                    }
                    if(nearbyEnt instanceof MobEntity nearbyMob){

                        nearbyMob.addStatusEffect(new StatusEffectInstance(SwordsModStatusEffects.getEffect(SwordsModStatusEffects.SMOKE_BOMBED), 20 * 5));
                    }
                }
                handStack.damage(2, user, EquipmentSlot.MAINHAND);
            }
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 30 * 20));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 15 * 20, 2));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, 15 * 20, 2));
            user.getItemCooldownManager().set(this, 20 * 30); // 30 sec cooldown - to encourage more frequent retreats
            return TypedActionResult.success(handStack);
        }
        return ready ? TypedActionResult.success(handStack) : TypedActionResult.pass(handStack);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        Entity attacker = damageSource.getAttacker();
        float extraDamage = super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
        if(!(attacker instanceof LivingEntity liveAttacker && liveAttacker.getWorld() instanceof ServerWorld sWorld)) return extraDamage;
        boolean wasSuperSneakAttack = false;
        if(liveAttacker.hasStatusEffect(StatusEffects.INVISIBILITY)){
            if(liveAttacker instanceof PlayerEntity playerAttacker && playerAttacker.getItemCooldownManager().isCoolingDown(this)){
                wasSuperSneakAttack = true;
                extraDamage += 0.75f;
            }
            extraDamage += 0.5f;
            liveAttacker.removeStatusEffect(StatusEffects.INVISIBILITY);
        }
        Vec3d targetLook = target.getRotationVec(0).multiply(1, 1, 0).normalize();
        Vec3d attackerToTargetVec = target.getPos().subtract(attacker.getPos()).normalize();
        double dotProd = targetLook.dotProduct(attackerToTargetVec);
        if(dotProd > 0){ // sneak attack !
            extraDamage += 1f;
            if(wasSuperSneakAttack && target instanceof LivingEntity liveTarget){
                // TODO: make this give an achievement for sneak attacking ?
                liveTarget.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 20 * 2)); // give them blindness for 2 seconds
            }
        }
        if(attacker.isSneaking()){
            extraDamage += 0.25f;
        }
        return extraDamage;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if(otherStack.getItem().equals(Items.FIREWORK_ROCKET)){
            stack.set(DataComponentTypes.FIREWORKS, otherStack.get(DataComponentTypes.FIREWORKS));
            otherStack.decrement(1);
            return true;
        }
        return false;
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(SwordtemberItem.hasShiftSafe()){
            MutableText infoText = Text.translatable(stack.getTranslationKey() + ".tooltip");
            infoText.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
            MutableText infoText2 = Text.translatable(stack.getTranslationKey() + ".tooltip.2");
            infoText2.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
            MutableText infoText3 = Text.translatable(stack.getTranslationKey() + ".tooltip.3");
            infoText3.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
            tooltip.add(infoText);
            tooltip.add(Text.literal(""));
            tooltip.add(infoText2);
            tooltip.add(Text.literal(""));
            tooltip.add(infoText3);
        } else {
            MutableText shiftMsg = Text.translatable("monthofswords.tooltip.shiftmsg");
            shiftMsg.setStyle(getSwordTooltipStyleModifier().apply(Style.EMPTY.withItalic(true)));
            tooltip.add(shiftMsg);
        }
        super.appendTooltip(stack, context, tooltip, type);
    }

    public UnaryOperator<Style> getSwordTooltipStyleModifier() {
        return (style) -> style.withColor(Formatting.DARK_RED);
    }
}