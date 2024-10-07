package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

public class PotionSwordItem extends SwordtemberItem {

    public PotionSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.IRON, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -2.4f))
        );
    }

    // mutates given item stacks if needed.
    // returns a side-product stack, ex: glass bottle if filling with a potion, potion if emptying into a glass bottle.
    public ItemStack tryFillSword(ItemStack swordStack, ItemStack fillStack, boolean shouldDecrement){
        if(swordStack.getItem() != this) return fillStack;
        // it'd be cool to be able to empty the sword back into potions, but doesn't seem very do-able since i don't see an easy way to split the duration
//        if(swordStack.contains(DataComponentTypes.POTION_CONTENTS)){
//            if(fillStack.getItem() == Items.GLASS_BOTTLE){
//                // handle emptying
//                PotionContentsComponent swordPotionContents = swordStack.get(DataComponentTypes.POTION_CONTENTS);
//                fillStack.decrement(1);
//                int hitsLeft = swordStack.getOrDefault(SwordsModComponents.POTION_HITS, 0);
//
//                ItemStack newPotion = new ItemStack(Items.POTION);
//            }
//            return ItemStack.EMPTY;
//        }
        if(swordStack.contains(DataComponentTypes.POTION_CONTENTS)) return ItemStack.EMPTY;
        // handle filling
        if(fillStack.getItem() != Items.POTION) return ItemStack.EMPTY; // TODO: this *could* be tag based, but that makes the glass bottle returning part harder-ish
        if(!fillStack.contains(DataComponentTypes.POTION_CONTENTS)) return ItemStack.EMPTY;
        PotionContentsComponent potionContents = fillStack.get(DataComponentTypes.POTION_CONTENTS);

        swordStack.set(DataComponentTypes.POTION_CONTENTS, potionContents);
        swordStack.set(SwordsModComponents.POTION_HITS, 5);
        if(shouldDecrement){
            fillStack.decrement(1);
        }

        return Items.GLASS_BOTTLE.getDefaultStack();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack swordStack = user.getStackInHand(hand);
        Hand otherHand = hand == Hand.MAIN_HAND ? Hand.OFF_HAND : Hand.MAIN_HAND; // a hand.otherHand() helper would be nice..
        ItemStack otherHandStack = user.getStackInHand(otherHand);
        ItemStack resultStack = tryFillSword(swordStack, otherHandStack, !user.getAbilities().creativeMode);
        if(resultStack.isEmpty()){
            return TypedActionResult.pass(swordStack);
        }
        Random random = world.getRandom();
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_BREWING_STAND_BREW, SoundCategory.PLAYERS,
            1f, (random.nextFloat() - random.nextFloat()) * 0.2f + 2f);
        if(otherHandStack.isEmpty()){
            user.setStackInHand(otherHand, resultStack);
        } else {
            if(!user.getAbilities().creativeMode){
                user.giveItemStack(resultStack);
            }
        }
        return TypedActionResult.success(swordStack);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(stack.contains(DataComponentTypes.POTION_CONTENTS)){
            PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
            int hitsLeft = stack.getOrDefault(SwordsModComponents.POTION_HITS, 0);
            if(hitsLeft > 0){
                potionContents.forEachEffect(effect -> {
                    StatusEffectInstance lesserInstance = new StatusEffectInstance(effect.getEffectType(),
                        Math.round(effect.getDuration() / 5f), effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
                    target.addStatusEffect(lesserInstance, attacker);
                });
            }
            if(hitsLeft - 1 > 0){
                if(!(attacker instanceof PlayerEntity player && player.getAbilities().creativeMode))
                    stack.set(SwordsModComponents.POTION_HITS, hitsLeft-1);
            } else {
                stack.remove(SwordsModComponents.POTION_HITS);
                stack.remove(DataComponentTypes.POTION_CONTENTS);
            }
        }
        return super.postHit(stack, target, attacker);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0x69adf0);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        if(stack.contains(DataComponentTypes.POTION_CONTENTS)){
            PotionContentsComponent potionContents = stack.get(DataComponentTypes.POTION_CONTENTS);
            List<Text> potionTooltip = new ArrayList<>();
            potionContents.buildTooltip((potionTooltipLine) -> {
                potionTooltip.add(potionTooltipLine);
            }, 1, context.getUpdateTickRate());
            tooltip.addAll(2, potionTooltip);
        }
    }
}
