package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.entities.LeafAttackEntity;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class SwordsModEntities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.ENTITY_TYPE);

    public static RegistrySupplier<EntityType<LeafAttackEntity>> LEAF_ATTACK = registerEntity("leaf_attack",
        EntityType.Builder.create(
            LeafAttackEntity::new, SpawnGroup.MISC
        ));

    public static <T extends Entity> RegistrySupplier<EntityType<T>> registerEntity(String name, EntityType.Builder<T> builder) {
        Identifier entTypeID = Identifier.of(SwordsMod.MOD_ID, name);
        return ENTITY_TYPES.register(entTypeID, () -> builder.build(entTypeID.toString()));
    }

    public static void register(){
        ENTITY_TYPES.register();
    }
}
