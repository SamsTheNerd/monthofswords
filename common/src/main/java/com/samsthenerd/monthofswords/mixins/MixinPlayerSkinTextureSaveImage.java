package com.samsthenerd.monthofswords.mixins;

import com.samsthenerd.monthofswords.render.PlayerSkinImageDuck;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.PlayerSkinTexture;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.IntUnaryOperator;

@Mixin(PlayerSkinTexture.class)
public class MixinPlayerSkinTextureSaveImage implements PlayerSkinImageDuck {

    @Unique
    private NativeImage monthofswords$image = null;

    @Inject(
        method="uploadTexture",
        at=@At("HEAD")
    )
    private void monthofswords$storePlayerSkinImage(NativeImage image, CallbackInfo ci){
        if(this.monthofswords$image == null) this.monthofswords$image = image.applyToCopy(IntUnaryOperator.identity());
    }

    @Override
    public @Nullable NativeImage mos$getSkinNativeImage() {
        return monthofswords$image;
    }
}
