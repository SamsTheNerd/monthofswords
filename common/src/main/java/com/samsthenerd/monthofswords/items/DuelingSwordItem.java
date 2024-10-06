package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.registry.SwordsModDamageTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class DuelingSwordItem extends SwordtemberItem{

    public static int WAIT_TICKS = 5; // attacking too quick will fail the parry and damage you.
    public static int PARRY_TICKS = 5; // attacking after will crit
    public static int TOTAL_TICKS = WAIT_TICKS + PARRY_TICKS; // just for readability.

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
//        if(user.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.pass(itemStack);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        ItemStack swordStack = damageSource.getWeaponStack();
        if(swordStack == null) return 0;
        if(!(damageSource.getAttacker() instanceof PlayerEntity player)) return 0;
        int cooldown = (int)(player.getItemCooldownManager().getCooldownProgress(this, 0) * TOTAL_TICKS);
        if(cooldown > 0 && cooldown < PARRY_TICKS){
            SwordsMod.LOGGER.info("parried (extra damage) !! -- " + cooldown);
            return baseAttackDamage * 1.5f;
        }
        return 0;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(attacker instanceof PlayerEntity player){
            int cooldown = (int)(player.getItemCooldownManager().getCooldownProgress(this, 0) * TOTAL_TICKS);
            if(cooldown > PARRY_TICKS){
                attacker.playSound(SoundEvents.BLOCK_ANVIL_DESTROY);
                attacker.damage(SwordsModDamageTypes.getParryMiss(attacker),
                        1.25f * stack.getOrDefault(SwordsModComponents.PARRY_DAMAGE, 2f));
                SwordsMod.LOGGER.info("parry miss ?!! -- " + cooldown);
            } else if (cooldown > 0){
                player.addCritParticles(target);
                SwordsMod.LOGGER.info("parried (crit particles) !! -- " + cooldown);
            }
            stack.remove(SwordsModComponents.PARRY_DAMAGE);
            if(cooldown > 0){
                player.getItemCooldownManager().remove(this);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier() {
        return (style) -> style.withColor(Formatting.GOLD);
    }
}
