package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class DivineSwordItem extends SwordtemberItem{
    public DivineSwordItem(Item.Settings itemSettings){
        super(ToolMaterials.NETHERITE, itemSettings.attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 3, -2.4f)));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        boolean ready = !user.getItemCooldownManager().isCoolingDown(this);
        ItemStack handStack = user.getStackInHand(hand);
        if(world instanceof ServerWorld sWorld && ready){
            // TODO: make this apply to players around them too maybe?
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 120));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 120));
            user.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 120));
            user.getItemCooldownManager().set(this, 20 * 60 * 2); // 2 minute cooldown?
            return TypedActionResult.success(handStack);
        }
        return ready ? TypedActionResult.success(handStack) : TypedActionResult.pass(handStack);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.YELLOW);
    }
}
