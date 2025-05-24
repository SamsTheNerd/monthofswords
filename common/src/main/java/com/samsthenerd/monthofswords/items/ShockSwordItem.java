package com.samsthenerd.monthofswords.items;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.UnaryOperator;

public class ShockSwordItem extends SwordtemberItem {
    public static final ToolMaterial COPPER_SHOCK_MATERIAL = new ClassyToolMaterial(300, 5f, 1.5f,
        BlockTags.INCORRECT_FOR_IRON_TOOL, 18, () -> Ingredient.ofItems(Items.COPPER_INGOT));

    public ShockSwordItem(Item.Settings itemSettings) {
        super(COPPER_SHOCK_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(COPPER_SHOCK_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if((world instanceof ServerWorld sWorld && !user.getItemCooldownManager().isCoolingDown(this))) {
            var hit = user.raycast(48, 0, true);
            if(hit.getType() != Type.MISS){
                LightningEntity lightning = EntityType.LIGHTNING_BOLT.spawn(sWorld,
                    new BlockPos((int)hit.getPos().x, (int)hit.getPos().y, (int)hit.getPos().z), SpawnReason.TRIGGERED);
                if(user instanceof ServerPlayerEntity sPlayer){
                    lightning.setChanneler(sPlayer);
                }
                user.getItemCooldownManager().set(this, 20*15);
            }
        }
        return super.use(world, user, hand);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        return target.getWorld().isThundering() ? 2f : 0;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.getWorld().playSound(null, target.getBlockPos(), SoundEvents.ITEM_TRIDENT_THUNDER.value(),
            SoundCategory.PLAYERS, 10f, attacker.getRandom().nextFloat() * 0.4f + 0.8f);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0xFC9982);
    }
}

