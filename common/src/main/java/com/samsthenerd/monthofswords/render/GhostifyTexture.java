package com.samsthenerd.monthofswords.render;

import com.samsthenerd.monthofswords.mixins.MixinAccessResTexLocation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;

public class GhostifyTexture {
    private static final HashMap<Identifier, Identifier> GHOSTIFIED_TEXTURES = new HashMap<>();

    public static void clearTextures(ResourceManager resMan){
        for(Identifier tex : GHOSTIFIED_TEXTURES.values()){
            MinecraftClient.getInstance().getTextureManager().destroyTexture(tex);
        }
        GHOSTIFIED_TEXTURES.clear();
    }

    public static Optional<Identifier> getGhostifiedTexture(Identifier originalTextureId){
        if(GHOSTIFIED_TEXTURES.containsKey(originalTextureId)) return Optional.of(GHOSTIFIED_TEXTURES.get(originalTextureId));
        AbstractTexture tex = MinecraftClient.getInstance().getTextureManager().getTexture(originalTextureId);
        return getImageFromTexture(tex).map(img -> {
            var newImg = new NativeImage(img.getWidth(), img.getHeight(), true);
            int minColor = 255;
            int maxColor = 0;
            for(int x = 0; x < img.getWidth(); x++){
                for(int y = 0; y < img.getHeight(); y++){
                    int color = img.getColor(x,y);
                    int alpha = Argb.getAlpha(color);
                    if(alpha == 0){
                        newImg.setColor(x,y,0);
                        continue;
                    }
                    int luminance = (int)((0.2126 * Argb.getRed(color)
                        + 0.7152 * Argb.getGreen(color)
                        + 0.0722 * Argb.getBlue(color)
                    )); // Standard relative luminance calculation, stolen from hex :p
                    minColor = Math.min(luminance, minColor);
                    maxColor = Math.max(luminance, maxColor);
                    int argb = Argb.getArgb(alpha, luminance,luminance,luminance);
                    newImg.setColor(x,y, argb);
                }
            }
            int minColorF = minColor;
            int maxColorF = maxColor;
            double r = maxColorF - minColorF;
            newImg.apply(c -> {
                int b = (0x0000FF & c);
                int bn = (int)(100 * ( b - minColorF) / r) + 155;
                return Argb.getArgb(Argb.getAlpha(c), bn, bn, bn);
            });
            Identifier newId = Identifier.of(originalTextureId.getNamespace() + "_monthofswords_ghostified",
                originalTextureId.getPath() + "_monthofswords_ghostified");
            MinecraftClient.getInstance().getTextureManager().registerTexture(newId, new NativeImageBackedTexture(newImg));
            GHOSTIFIED_TEXTURES.put(originalTextureId, newId);
            return newId;
        });
    }

    public static Optional<NativeImage> getImageFromTexture(AbstractTexture texture){
        if(texture instanceof NativeImageBackedTexture nibTex){
            return Optional.ofNullable(nibTex.getImage());
        } else if (texture instanceof PlayerSkinTexture psTex) {
            if(psTex instanceof PlayerSkinImageDuck psiDuck){
                NativeImage img = psiDuck.mos$getSkinNativeImage();
                if(img != null) return Optional.of(img);
            }
        }
        if(texture instanceof ResourceTexture resTex){
            return ResLoaderAccessor.loadNativeImage(MinecraftClient.getInstance().getResourceManager(), resTex);
        }
        return Optional.empty();
    }

    public static class ResLoaderAccessor extends ResourceTexture {

        public ResLoaderAccessor(Identifier location) {
            super(location);
        }

        public static Optional<NativeImage> loadNativeImage(ResourceManager resMan, ResourceTexture resTex){
            try{
                var texData = ResourceTexture.TextureData.load(resMan, ((MixinAccessResTexLocation) resTex).getLocation());
                return Optional.of(texData.getImage());
            } catch (IOException e){
                return Optional.empty();
            }
        }
    }
}
