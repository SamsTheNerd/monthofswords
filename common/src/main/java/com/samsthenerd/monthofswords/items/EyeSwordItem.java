package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.registry.SwordsModAttributes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class EyeSwordItem extends SwordtemberItem {
    public static final ClassyToolMaterial ENDER_MATERIAL = new ClassyToolMaterial(2000, 6f, 3f,
        BlockTags.INCORRECT_FOR_IRON_TOOL, 9, () -> Ingredient.ofItems(Items.ENDER_EYE));

    public static Identifier ENDERMAN_FRIENDLY_MOD = SwordsMod.id("enderman_eyesword_friendly");

    public static TagKey<EntityType<?>> ENDER_ENTITY = TagKey.of(RegistryKeys.ENTITY_TYPE, SwordsMod.id("ender"));

    public EyeSwordItem(Item.Settings itemSettings) {
        super(ENDER_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ENDER_MATERIAL, 3, -2.4f)
                .with(
                    SwordsModAttributes.ENDERMAN_FRIENDLY,
                    new EntityAttributeModifier(ENDERMAN_FRIENDLY_MOD, 1, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.MAINHAND
                )
        ));
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        float bonus = super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
        if(target.getWorld().getRegistryKey().equals(World.END)) bonus += 0.5f;
        if(target.getType().isIn(ENDER_ENTITY)) bonus += 1f;
        return bonus;
    }
}
