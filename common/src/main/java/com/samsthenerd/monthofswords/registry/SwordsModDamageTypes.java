package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class SwordsModDamageTypes {
    public static final RegistryKey<DamageType> CURSE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE,
            Identifier.of(SwordsMod.MOD_ID, "curse"));

    public static DamageSource getCurseDamage(LivingEntity attacker){
        return new DamageSource(
                attacker.getWorld().getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(CURSE_DAMAGE),
                attacker);
    }

    public static DamageSource getCurseDamage(World world){
        return new DamageSource(
                world.getRegistryManager()
                        .get(RegistryKeys.DAMAGE_TYPE)
                        .entryOf(CURSE_DAMAGE)
            );
    }
}
