package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Style;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class GlowSwordItem extends SwordtemberItem implements SwordActionHaverServer{

    public GlowSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.DIAMOND, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 2, -2f))
        );
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier() {
        return (style) -> style.withColor(0xAFFAE2);
    }

    // glow and do damage
    @Override
    public boolean doSwordAction(PlayerEntity player, ItemStack stack) {
        if(!player.getItemCooldownManager().isCoolingDown(this)){
            var nearEnts = player.getWorld().getOtherEntities(player, Box.of(player.getPos(), 32, 32, 16));
            for(var ent : nearEnts){
                if(ent instanceof LivingEntity livEnt){
                    livEnt.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 60));
                    ent.damage(player.getDamageSources().magic(), 5);
                }
            }
            player.getItemCooldownManager().set(this, 20 * 45);
            stack.damage(5, player, player.getMainHandStack() == stack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }
        return true;
    }

    //
    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return target.isGlowing() ? 2f : 0;
    }

    // glow for 45 seconds on hit
    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 45));
        return super.postHit(stack, target, attacker);
    }

    // give all nearby glowing for a minute
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if(!player.getItemCooldownManager().isCoolingDown(this) && !world.isClient()){
            var nearEnts = world.getOtherEntities(player, Box.of(player.getPos(), 8, 8, 8));
            for(var ent : nearEnts){
                if(ent instanceof LivingEntity livEnt) livEnt.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 20 * 60));
            }
            player.getItemCooldownManager().set(this, 20 * 15);
            stack.damage(3, player, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
        }
        return TypedActionResult.pass(stack);
    }
}
