package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.RegistryKeys;

public class SwordsModStatusEffects {

    public static DeferredRegister<StatusEffect> STATUS_EFFECTS = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.STATUS_EFFECT);
    public static final Registrar<StatusEffect> REGISTRAR = STATUS_EFFECTS.getRegistrar();

    public static final RegistrySupplier<StatusEffect> SMOKE_BOMBED = REGISTRAR.register(SwordsMod.id("smoke_bombed"),
            () -> new StatusEffect(StatusEffectCategory.HARMFUL, 0){
                // anonymous class to access protected constructor tehe
            }.addAttributeModifier(EntityAttributes.GENERIC_FOLLOW_RANGE, SwordsMod.id("effect.smoke_bombed"), -32, EntityAttributeModifier.Operation.ADD_VALUE));


    public static void init(){
        STATUS_EFFECTS.register();
    }
}
