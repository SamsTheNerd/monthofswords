package com.samsthenerd.monthofswords.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.samsthenerd.monthofswords.render.EchoMidVC;
import com.samsthenerd.monthofswords.render.GhostlyProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
@Debug(export=true)
public class MixinLivingEntityRendererVC {
//    @WrapMethod(
//        method = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
//    )
//    private void monthofswords$renderLivEnt(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Operation<Void> original){
//        VertexConsumerProvider vcp = GhostlyProvider.getGhostlyProvider(livingEntity)
//            .map(gp -> (VertexConsumerProvider) (layer) -> new EchoMidVC(vertexConsumerProvider.getBuffer(layer), gp))
//            .orElse(vertexConsumerProvider);
//        original.call(livingEntity, f, g, matrixStack, vcp, i);
//    }

    @ModifyVariable(
        method = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        at=@At("HEAD")
    )
    private VertexConsumerProvider monthofswords$renderLivEnt(VertexConsumerProvider vertexConsumerProvider, LivingEntity livEnt){
//        , LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, Operation<Void> original){
        VertexConsumerProvider vcp = GhostlyProvider.getGhostlyProvider(livEnt)
            .map(gp -> (VertexConsumerProvider) (layer) -> new EchoMidVC(vertexConsumerProvider.getBuffer(layer), gp))
            .orElse(vertexConsumerProvider);
        return vcp;
//        original.call(livingEntity, f, g, matrixStack, vcp, i);
    }

    @ModifyReturnValue(
        method="getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;",
        at=@At("RETURN"))
    private RenderLayer monthofswords$makeRenderLayerTranslucent(RenderLayer originalLayer, LivingEntity entity,
                                                                 boolean showBody, boolean translucent, boolean showOutline){
        var thisRenderer = (LivingEntityRenderer)(Object) this;
        Identifier id = thisRenderer.getTexture(entity);
        return GhostlyProvider.getGhostlyProvider(entity).map(gp ->
            gp.getGhostlyRenderLayer(originalLayer, entity, id, showBody, translucent, showOutline)
        ).orElse(originalLayer);
    }
}
