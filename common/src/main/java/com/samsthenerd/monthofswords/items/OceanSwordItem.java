package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.util.function.UnaryOperator;

public class OceanSwordItem extends SwordtemberItem{

    public static final ClassyToolMaterial OCEAN_MATERIAL = new ClassyToolMaterial(300, 6f, 2f,
            BlockTags.INCORRECT_FOR_IRON_TOOL, 18, () -> Ingredient.ofItems(Items.PRISMARINE_SHARD));

    private static final Identifier OCEAN_MOVEMENT_MOD = Identifier.of(SwordsMod.MOD_ID, "oceanmove");
    private static final Identifier OCEAN_OXYGEN_MOD = Identifier.of(SwordsMod.MOD_ID, "oceanoxy");
    private static final Identifier OCEAN_MINING_MOD = Identifier.of(SwordsMod.MOD_ID, "oceanmining");

    public OceanSwordItem(Item.Settings itemSettings) {
        super(OCEAN_MATERIAL, itemSettings.attributeModifiers(
                SwordItem.createAttributeModifiers(ToolMaterials.IRON, 3, -2.4f)
                        .with(
                                EntityAttributes.GENERIC_WATER_MOVEMENT_EFFICIENCY,
                                new EntityAttributeModifier(OCEAN_MOVEMENT_MOD, 0.5, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        ).with(
                                EntityAttributes.GENERIC_OXYGEN_BONUS,
                                new EntityAttributeModifier(OCEAN_OXYGEN_MOD, 150, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        ).with(
                                EntityAttributes.PLAYER_SUBMERGED_MINING_SPEED,
                                new EntityAttributeModifier(OCEAN_MINING_MOD, 0.6, EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.MAINHAND
                        )
        ));
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0x1dc2d1);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(damageSource.getAttacker() instanceof LivingEntity attacker){
            return attacker.isSubmergedInWater() ? 1.5f : 0;
        }
        return 0;
    }
}
