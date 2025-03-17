package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.utils.LivingEntDuck;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.function.UnaryOperator;

public class WovenSwordItem extends SwordtemberItem {

    public WovenSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.DIAMOND, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 5, -2.4f))
        );
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier() {
        return (style) -> style.withColor(0xFCCBE6);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.getWorld().playSound(null, attacker.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
            SoundCategory.PLAYERS, 100f, attacker.getRandom().nextFloat() * 0.4f + 0.8f);
        return super.postHit(stack, target, attacker);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if(user.getItemCooldownManager().isCoolingDown(this)) return TypedActionResult.pass(stack);
        if(user instanceof ServerPlayerEntity sPlayer){
            world.playSound(null, sPlayer.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
                SoundCategory.PLAYERS, 100f, user.getRandom().nextFloat() * 0.4f + 0.8f);
            Vec3d vel = user.getRotationVector();
            user.addVelocity(vel);
            user.velocityModified = true;
            user.getItemCooldownManager().set(this, 1000);
            ((LivingEntDuck)user).makeDashTransgenderly();
        }
        return TypedActionResult.success(stack, true);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        if(damageSource.getAttacker() instanceof LivingEntDuck attacker){
            return attacker.isDashingTransgenderly() ? 4 : 0;
        }
        return 0;
    }

    public static final Vector3f TRANS_BLUE_COLOR = new Vector3f(80f/256, 190f/256, 241f/256);
    public static final Vector3f TRANS_PINK_COLOR = new Vector3f(252f/256, 203f/256, 230f/256);

    public static void makeTransParticles(ServerPlayerEntity player){
        for(int i = 0; i < 10; i++){
            player.getServerWorld().spawnParticles(
                new DustParticleEffect(player.getRandom().nextBoolean() ? TRANS_BLUE_COLOR : TRANS_PINK_COLOR, 1),
                player.getX()+player.getRandom().nextDouble(),
                player.getY()+player.getRandom().nextDouble(),
                player.getZ()+player.getRandom().nextDouble(),
                1,
                0, 0, 0, 0);
        }
    }
}
