package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.entities.LeafAttackEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Style;
import net.minecraft.util.math.Vec3d;

import java.util.function.UnaryOperator;

public class ForestSwordItem extends SwordtemberItem implements SwordLeftClickHaverServer{

    public ForestSwordItem(Item.Settings itemSettings) {
        super(ToolMaterials.DIAMOND, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(ToolMaterials.DIAMOND, 3, -2f))
        );
    }

    @Override
    public void doSwordLCAction(PlayerEntity player, ItemStack swordStack) {
        if(player.getItemCooldownManager().isCoolingDown(this)) return;
        var fireball = new LeafAttackEntity(player.getWorld());
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_CHERRY_LEAVES_PLACE,
            SoundCategory.PLAYERS, 1.5f, player.getRandom().nextFloat() * 0.2f + .2f);
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_BREEZE_LAND,
            SoundCategory.PLAYERS, 1f, player.getRandom().nextFloat() * 0.1f + 0.7f);
        player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
            SoundCategory.PLAYERS, 3f, player.getRandom().nextFloat() * 0.3f + 1.2f);
        fireball.setOwner(player);
        fireball.setVelocity(player.getRotationVector().multiply(3));
        Vec3d lookVec = player.getRotationVector();
        fireball.setPosition(player.getPos().add(lookVec.x, 1.4, lookVec.z));
        player.getWorld().spawnEntity(fireball);
        player.getItemCooldownManager().set(this, 15);
        swordStack.damage(1, player, EquipmentSlot.MAINHAND);
    }

    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0xf7b9dc);
    }
}
