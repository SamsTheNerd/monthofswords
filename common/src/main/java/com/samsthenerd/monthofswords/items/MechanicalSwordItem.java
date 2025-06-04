package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class MechanicalSwordItem extends SwordtemberItem {

    public static final TagKey<Item> BRONZE_TAG = TagKey.of(RegistryKeys.ITEM, Identifier.of("c", "ingots/bronze"));

    public static final ToolMaterial BRONZEISH_MATERIAL = new ClassyToolMaterial(500, 5f, 2.5f,
        BlockTags.INCORRECT_FOR_IRON_TOOL, 10,
        hasBronze() ? () -> Ingredient.fromTag(BRONZE_TAG) : () -> Ingredient.ofItems(Items.GOLD_INGOT));

    public static boolean hasBronze(){
        return Registries.ITEM.getEntryList(BRONZE_TAG).map(l -> l.size() > 0).orElse(false);
    }

    public MechanicalSwordItem(Item.Settings itemSettings) {
        super(BRONZEISH_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(BRONZEISH_MATERIAL, 3, -2f)
                .with(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                    new EntityAttributeModifier(SwordsMod.id("mechanical_reach_mod"), 2, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.MAINHAND)
                .with(EntityAttributes.PLAYER_ENTITY_INTERACTION_RANGE,
                    new EntityAttributeModifier(SwordsMod.id("mechanical_reach_mod"), 2, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.MAINHAND)
            )
        );
    }
}
