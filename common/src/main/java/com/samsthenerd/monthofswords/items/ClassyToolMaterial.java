package com.samsthenerd.monthofswords.items;

import net.minecraft.block.Block;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.TagKey;

import java.util.function.Supplier;

public class ClassyToolMaterial implements ToolMaterial {
    private int durability;
    private float miningSpeedMult;
    private float damage;
    private TagKey<Block> inverseTag; // whatever that means
    private int enchantability;
    private Supplier<Ingredient> repairIngredient;


    public ClassyToolMaterial(int dur, float miningSpeed, float damage, TagKey<Block> invTag, int enchantability, Supplier<Ingredient> repairIngredient){
        this.durability = dur;
        this.miningSpeedMult = miningSpeed;
        this.damage = damage;
        this.inverseTag = invTag;
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
    }

    public int getDurability(){return this.durability; }

    public float getMiningSpeedMultiplier(){ return this.miningSpeedMult; }

    public float getAttackDamage(){ return this.damage; }

    public TagKey<Block> getInverseTag(){ return this.inverseTag; }

    public int getEnchantability(){ return this.enchantability; }

    public Ingredient getRepairIngredient(){ return this.repairIngredient.get(); }
}
