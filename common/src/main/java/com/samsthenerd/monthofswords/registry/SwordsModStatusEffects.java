package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.EntityEffectParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper.Argb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class SwordsModStatusEffects {

    private static final Map<Identifier, StatusEffect> effectsMap = new HashMap<>();
    public static DeferredRegister<StatusEffect> STATUS_EFFECTS = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.STATUS_EFFECT);
//    public static final Registrar<StatusEffect> REGISTRAR = STATUS_EFFECTS.getRegistrar();

    public static final Identifier SMOKE_BOMBED = register(SwordsMod.id("smoke_bombed"),
            new StatusEffect(StatusEffectCategory.HARMFUL, 0){
                // anonymous class to access protected constructor tehe
            }.addAttributeModifier(EntityAttributes.GENERIC_FOLLOW_RANGE, SwordsMod.id("effect.smoke_bombed"), -32, EntityAttributeModifier.Operation.ADD_VALUE));

    public static final Identifier FRIEND_OF_BUGS = register(SwordsMod.id("friend_of_bugs"),
        new FriendOfEntityStatusEffect(ent -> ent.getType().isIn(EntityTypeTags.ARTHROPOD), 0,
            EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, Argb.withAlpha(0, 0))));

    public static final Identifier NECROMANCER = register(SwordsMod.id("necromancer"),
        new FriendOfEntityStatusEffect(ent -> ent.getType().isIn(EntityTypeTags.UNDEAD), 0));

    public static RegistryEntry<StatusEffect> getEffect(Identifier id){
        return STATUS_EFFECTS.getRegistrar().getHolder(id);
    }

    public static Identifier register(Identifier id, StatusEffect statusEffect) {
        effectsMap.put(id, statusEffect);
        return id;
    }

    public static class FriendOfEntityStatusEffect extends StatusEffect {

        public final Predicate<Entity> friendPredicate;

        public static final List<FriendOfEntityStatusEffect> ALL_FRIEND_EFFECTS = new ArrayList<>();

        public FriendOfEntityStatusEffect(Predicate<Entity> friendPredicate, int color, ParticleEffect particle){
            super(StatusEffectCategory.BENEFICIAL, color, particle);
            this.friendPredicate = friendPredicate;
            ALL_FRIEND_EFFECTS.add(this);
        }


        public FriendOfEntityStatusEffect(Predicate<Entity> friendPredicate, int color){
            this(friendPredicate, color, EntityEffectParticleEffect.create(ParticleTypes.ENTITY_EFFECT, 0));
        }
    }

    public static void init(){
        STATUS_EFFECTS.register();

        for(var entry : effectsMap.entrySet()){
            STATUS_EFFECTS.register(entry.getKey(), entry::getValue);
        }
    }
}
