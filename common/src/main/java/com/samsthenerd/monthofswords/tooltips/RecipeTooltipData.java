package com.samsthenerd.monthofswords.tooltips;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Recipe;

public record RecipeTooltipData(Recipe<?> rec) implements TooltipData {
    public static final Codec<RecipeTooltipData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Recipe.CODEC.fieldOf("rec").forGetter(RecipeTooltipData::rec)
            ).apply(instance, RecipeTooltipData::new)
    );
    public static final PacketCodec<RegistryByteBuf, RecipeTooltipData> PACKET_CODEC = PacketCodec.tuple(
        Recipe.PACKET_CODEC, RecipeTooltipData::rec,
        RecipeTooltipData::new
    );
}
