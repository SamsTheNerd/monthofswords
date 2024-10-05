package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class DuelingSwordItem extends SwordtemberItem{
    public DuelingSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.IRON, itemSettings.attributeModifiers(
                SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -2.4f))
        );
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    // this is what bows have ?
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if(user.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.pass(itemStack);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        ItemStack swordStack = damageSource.getWeaponStack();
        if(swordStack == null) return 0;
        if(damageSource.getAttacker() instanceof PlayerEntity player
        && player.getItemCooldownManager().isCoolingDown(this)){
            return baseAttackDamage * 0.5f;
        }
        return 0;
    }

    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker instanceof PlayerEntity player){
            player.addCritParticles(target);
            if(player.getItemCooldownManager().isCoolingDown(this)){
                player.getItemCooldownManager().remove(this);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    public UnaryOperator<Style> getSwordTooltipStyleModifier() {
        return (style) -> style.withColor(Formatting.GOLD);
    }
}
