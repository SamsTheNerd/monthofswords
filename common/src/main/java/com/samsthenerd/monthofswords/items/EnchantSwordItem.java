package com.samsthenerd.monthofswords.items;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.Property;
import net.minecraft.text.Style;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class EnchantSwordItem extends SwordtemberItem{

    private final Random random = Random.create();
    private final Property seed = Property.create();

    public EnchantSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.DIAMOND, itemSettings.attributeModifiers(
                SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2.4f))
        );
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0xcb88f7);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        // remove enchants
        EnchantmentHelper.apply(stack, components -> components.remove(enchantment -> !enchantment.isIn(EnchantmentTags.CURSE)));

        // generate some new enchants
        this.random.setSeed(this.seed.get() + attacker.getPos().hashCode());
        Optional<RegistryEntryList.Named<Enchantment>> optional = attacker.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT)
                .getEntryList(EnchantmentTags.IN_ENCHANTING_TABLE);
        if (!optional.isEmpty()) {
            List<EnchantmentLevelEntry> list = EnchantmentHelper.generateEnchantments(this.random, stack, 30, optional.get().stream());
            for (EnchantmentLevelEntry enchantmentinstance : list) {
                stack.addEnchantment(enchantmentinstance.enchantment, enchantmentinstance.level);
            }
        }

        return super.postHit(stack, target, attacker);
    }
}
