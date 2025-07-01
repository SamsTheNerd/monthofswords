package com.samsthenerd.monthofswords.tooltips;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.mixins.MixinSmithingTransformRecipeAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.SmithingTransformRecipe;
import net.minecraft.util.Identifier;

import java.util.Arrays;
import java.util.List;

public record RecipeTooltipComponent(RecipeTooltipData data) implements TooltipComponent {
    @Override
    public int getHeight() {
        return 83;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return 150;
    }

    public static final Identifier CRAFTING_BACKGROUND = SwordsMod.id("textures/gui/crafting_background.png");
    public static final Identifier SMITHING_BACKGROUND = SwordsMod.id("textures/gui/smithing_background.png");

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        if(data.rec() instanceof CraftingRecipe craftRec){
            context.drawTexture(CRAFTING_BACKGROUND, x, y, 100, 0, 0, 146, 83, 256, 256);
            var ingrs = craftRec.getIngredients();

            int recWidth = 3;
            int recXOffset = 0;
            if(craftRec instanceof ShapedRecipe shapedRec){
                recWidth = Math.min(shapedRec.getWidth(), 3); // idk about nonsense
                if(recWidth == 1) recXOffset = 1;
            }

            for(int i = 0; i < ingrs.size(); i++){
                int ingrX = 18 + ((i % recWidth)+recXOffset) * 18;
                int ingrY = 15 + (i / recWidth) * 18;
                ItemStack ingrStack = Arrays.stream(ingrs.get(i).getMatchingStacks()).findFirst().orElse(ItemStack.EMPTY);
                context.drawItem(ingrStack, x+ingrX, y+ingrY);
            }

            context.drawItem(craftRec.getResult(MinecraftClient.getInstance().world.getRegistryManager()), 112+x, 33+y);

        } else if (data.rec() instanceof SmithingTransformRecipe smithRec
            && smithRec instanceof MixinSmithingTransformRecipeAccessor smithRecAcc){

            context.drawTexture(SMITHING_BACKGROUND, x, y, 100, 0, 0, 146, 83, 256, 256);
            List<Ingredient> ingrs = List.of(smithRecAcc.getTemplateIngredient(), smithRecAcc.getBaseIngredient(), smithRecAcc.getAdditionIngredient());
            for(int i = 0; i < ingrs.size(); i++){
                int ingrX = 20 + i * 18;
                int ingrY = 43 - i;
                ItemStack ingrStack = Arrays.stream(ingrs.get(i).getMatchingStacks()).findFirst().orElse(ItemStack.EMPTY);
                context.drawItem(ingrStack, x+ingrX, y+ingrY);
            }

            context.drawItem(smithRec.getResult(MinecraftClient.getInstance().world.getRegistryManager()), 110+x, 43+y);
        }
    }
}
