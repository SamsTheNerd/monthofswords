package com.samsthenerd.monthofswords.mixins;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTransformRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SmithingTransformRecipe.class)
public interface MixinSmithingTransformRecipeAccessor {
    @Accessor("template")
    Ingredient getTemplateIngredient();

    @Accessor("base")
    Ingredient getBaseIngredient();

    @Accessor("addition")
    Ingredient getAdditionIngredient();
}
