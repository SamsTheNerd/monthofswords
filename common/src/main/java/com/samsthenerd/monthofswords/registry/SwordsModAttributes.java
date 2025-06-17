package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute.Category;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.Supplier;

public class SwordsModAttributes {

    public static DeferredRegister<EntityAttribute> ATTRIBUTES_REGISTRY = DeferredRegister.create(SwordsMod.MOD_ID, RegistryKeys.ATTRIBUTE);

    public static RegistryEntry<EntityAttribute> ENDERMAN_FRIENDLY = register("enderman_friendly",
        () -> new ClampedEntityAttribute(
            "attribute.name.monthofswords.enderman_friendly",
            0, 0, 1
        ).setCategory(Category.POSITIVE).setTracked(true)
    );

    public static RegistryEntry<EntityAttribute> register(String id, Supplier<EntityAttribute> attribute) {
        return ATTRIBUTES_REGISTRY.register(SwordsMod.id(id), attribute);
    }

    private static boolean didRegister = false;

    public static void init(){
        if(!didRegister) ATTRIBUTES_REGISTRY.register();
        didRegister = true;
    }
}
