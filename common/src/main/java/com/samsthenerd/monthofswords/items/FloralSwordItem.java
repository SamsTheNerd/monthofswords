package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class FloralSwordItem extends SwordtemberItem {

    private static Random random = Random.createLocal();

    public static final ClassyToolMaterial FLORAL_MATERIAL = new ClassyToolMaterial(150, 8f, 1f,
            BlockTags.INCORRECT_FOR_WOODEN_TOOL, 18, () -> Ingredient.EMPTY);

    public FloralSwordItem(Item.Settings itemSettings){
        super(FLORAL_MATERIAL, itemSettings.attributeModifiers(SwordItem.createAttributeModifiers(FLORAL_MATERIAL, 3, -2.4f)));
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(Formatting.GREEN);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        int skyLight = world.getLightingProvider().get(LightType.SKY).getLightLevel(entity.getBlockPos());
//        boolean probSaysYes = random.nextInt(100) < 5; // once a second?
        boolean probSaysYes = entity.age % 100 == 0; // 2 damage a second?
        if(world instanceof ServerWorld sWorld && stack.getDamage() > 0 && slot >= 0 && slot < 9
                && skyLight > 7 && sWorld.isDay() &&  probSaysYes){
            stack.setDamage(Math.max(stack.getDamage()-10, 0));
        }
    }
}
