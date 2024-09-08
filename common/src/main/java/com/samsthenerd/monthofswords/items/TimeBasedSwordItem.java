package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

import java.util.function.Function;
import java.util.function.UnaryOperator;

// this is really just for sun and moon mod so it won't be too too extendable.
public class TimeBasedSwordItem extends SwordtemberItem{

    public static final Function<Item, ClassyToolMaterial> MAT_SUPPLIER = (repairItem) -> new ClassyToolMaterial(750, 8f, 2f,
            BlockTags.INCORRECT_FOR_IRON_TOOL, 18, () -> Ingredient.ofItems(repairItem));

    private final boolean dayOrNight;

    public TimeBasedSwordItem(Item repairItem, Item.Settings settings, boolean dayOrNight){
        super(MAT_SUPPLIER.apply(repairItem), settings.attributeModifiers(SwordItem.createAttributeModifiers(MAT_SUPPLIER.apply(repairItem), 3, -2.4f)));
        this.dayOrNight = dayOrNight;
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(dayOrNight){
            return target.getWorld().isDay() ? 1.5f : 0;
        } else {
            return target.getWorld().isNight() ? 1.5f : 0;
        }
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(dayOrNight ? Formatting.YELLOW : Formatting.BLUE);
    }
}
