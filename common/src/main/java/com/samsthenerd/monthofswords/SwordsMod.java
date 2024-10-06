package com.samsthenerd.monthofswords;

import com.google.common.base.Suppliers;
import com.samsthenerd.monthofswords.items.DuelingSwordItem;
import com.samsthenerd.monthofswords.registry.*;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.registry.registries.RegistrarManager;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class SwordsMod {
    public static final String MOD_ID = "monthofswords";

    public static Identifier id(String path){
        return Identifier.of(MOD_ID, path);
    }

    public static final Supplier<RegistrarManager> MANAGER = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static void init() {
        SwordsModItems.register();
        SwordsModNetworking.commonInit();
        SwordsModLoot.init();
        SwordsModStatusEffects.init();
        SwordsModComponents.register();

        EntityEvent.LIVING_HURT.register((LivingEntity entity, DamageSource source, float amount) -> {
            ItemStack victimStack = entity.getMainHandStack();
            if(entity instanceof PlayerEntity player
                && player.isUsingItem()
                && victimStack.getItem().equals(SwordsModItems.DUELING_SWORD.get())
                && !source.isIn(DamageTypeTags.BYPASSES_SHIELD)
                && source.getAttacker() instanceof LivingEntity livingAttacker
                && !player.getItemCooldownManager().isCoolingDown(victimStack.getItem())
            ){
                victimStack.apply(SwordsModComponents.PARRY_DAMAGE, amount, prev -> amount);
                player.getItemCooldownManager().set(victimStack.getItem(), DuelingSwordItem.TOTAL_TICKS);
                victimStack.damage(1, entity, EquipmentSlot.MAINHAND);
                Vec3d attToPlayer = player.getPos().subtract(livingAttacker.getPos()).normalize();
                livingAttacker.takeKnockback(0.1, attToPlayer.x, attToPlayer.z);
                SwordsMod.LOGGER.info("block damage: " + amount);
            }
            return EventResult.pass();
        });
    }
}
