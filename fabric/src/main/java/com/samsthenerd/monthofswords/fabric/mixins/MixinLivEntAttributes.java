package com.samsthenerd.monthofswords.fabric.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.samsthenerd.monthofswords.registry.SwordsModAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LivingEntity.class)
public class MixinLivEntAttributes {
	@ModifyReturnValue(
			method="createLivingAttributes", at=@At("RETURN")
	)
	private static DefaultAttributeContainer.Builder monthOfSwords$addDefaultLivingAttributes(DefaultAttributeContainer.Builder builder){
		SwordsModAttributes.init();
		return builder.add(SwordsModAttributes.ENDERMAN_FRIENDLY, 0);
	}
}
