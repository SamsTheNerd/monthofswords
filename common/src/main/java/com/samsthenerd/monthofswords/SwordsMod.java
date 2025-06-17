package com.samsthenerd.monthofswords;

import com.google.common.base.Suppliers;
import com.samsthenerd.monthofswords.lucky.LuckyFunctions;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.GameRules.BooleanRule;
import net.minecraft.world.GameRules.Category;
import net.minecraft.world.GameRules.Key;
import org.jetbrains.annotations.Nullable;
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
        SwordsModAttributes.init();
        SwordsModItems.register();
        SwordsModEntities.register();
        SwordsModNetworking.commonInit();
        SwordsModLoot.init();
        SwordsModStatusEffects.init();
        SwordsModComponents.register();
        SwordsModDataLoaders.init();
        LuckyFunctions.register();

        EntityEvent.LIVING_HURT.register((LivingEntity entity, DamageSource source, float amount) -> {
            ItemStack victimStack = entity.getMainHandStack();
            if(entity instanceof PlayerEntity player
                && player.isUsingItem()
                && victimStack.getItem().equals(SwordsModItems.DUELING_SWORD.get())
                && !source.isIn(DamageTypeTags.BYPASSES_SHIELD)
                && !player.getItemCooldownManager().isCoolingDown(victimStack.getItem())
            ){
                player.getItemCooldownManager().set(victimStack.getItem(), 15);
                victimStack.damage(1, entity, EquipmentSlot.MAINHAND);
            }
            return EventResult.pass();
        });
    }

    public static Key<BooleanRule> DESTRUCTIVE_ADVENTURE_SWORDS = GameRules.register(
      "monthOfSwordsDestructiveAdventureMode", Category.PLAYER, BooleanRule.create(false)
    );

    public static Key<BooleanRule> SUPER_SUPER_SAFE_SWORDS = GameRules.register(
      "monthOfSwordsNoDestructionEVER", Category.PLAYER, BooleanRule.create(false)
    );

    public static boolean canBeDestructive(PlayerEntity player, @Nullable BlockPos pos){
        if(player.getWorld().getGameRules().getBoolean(SUPER_SUPER_SAFE_SWORDS)) return false;
        if(player.getWorld().getGameRules().getBoolean(DESTRUCTIVE_ADVENTURE_SWORDS)) return true;
        if(pos != null && !player.canModifyAt(player.getWorld(), pos)) return false;
        return player.canModifyBlocks();
    }
}
