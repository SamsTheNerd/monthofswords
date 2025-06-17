package com.samsthenerd.monthofswords.render;

import com.samsthenerd.monthofswords.render.FakeGhostPlayerManager.GhostlyPlayerEntity;
import com.samsthenerd.monthofswords.utils.ColorUtils;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public interface GhostlyProvider {

    static Optional<GhostlyProvider> getGhostlyProvider(LivingEntity livEnt){
        if(livEnt instanceof GhostlyPlayerEntity){
            return Optional.of((HueGhostlyProvider)((hd, v, t) -> ((float)(0.47f + 0.1f * hd))));
        }
//        else if(livEnt.hasStatusEffect(SwordsModStatusEffects.getEffect(SwordsModStatusEffects.DISPLACED))){
//            return Optional.of((HueGhostlyProvider)((hd, v, t) -> ((float)(0.05f + 0.1f * hd))));
//        }
        return Optional.empty();
    }

    int getGhostlyColor(int red, int green, int blue, int alpha, Vec3d lastVec, float time);

    default RenderLayer getGhostlyRenderLayer(RenderLayer originalLayer, LivingEntity entity,
                                              Identifier originalTextureId, boolean showBody, boolean translucent, boolean showOutline){
        Identifier ghostlyId = GhostifyTexture.getGhostifiedTexture(originalTextureId).orElse(originalTextureId);
        return RenderLayer.getItemEntityTranslucentCull(ghostlyId);
    }

    default int getLightmapCoords(int u, int v, Vec3d lastVec, float time){
        return LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }

    interface HueGhostlyProvider extends GhostlyProvider{

        float getGhostlyHue(double hd, Vec3d lastVec, float time);

        @Override
        default int getGhostlyColor(int red, int green, int blue, int alpha, Vec3d lastVec, float time){
            double t = MathHelper.floorMod(time, 4f);
            t = t < 2 ? t / 4 : 1-t /4;
            Vec3d d = new Vec3d(1,3,1).normalize();
            double hd = MathHelper.floorMod(lastVec.dotProduct(d) - time, 1.0);
            float hue = getGhostlyHue(hd, lastVec, time);
            int argb = ColorUtils.HSBtoRGB(hue, 1f, 1f);
            return Argb.withAlpha(128, argb);
        }
    }
}
