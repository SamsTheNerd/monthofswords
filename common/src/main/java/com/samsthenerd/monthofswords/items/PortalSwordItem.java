package com.samsthenerd.monthofswords.items;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Style;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

import java.util.function.UnaryOperator;

public class PortalSwordItem extends SwordtemberItem implements SwordActionHaverServer {
    public static final ToolMaterial OBSIDIAN_MATERIAL = new ClassyToolMaterial(750, 7f, 2.5f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 16, () -> Ingredient.ofItems(Items.OBSIDIAN));

    public PortalSwordItem(Item.Settings itemSettings) {
        super(OBSIDIAN_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(OBSIDIAN_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public UnaryOperator<Style> getSwordTooltipStyleModifier(){
        return (style) -> style.withColor(0xaf4fe3);
    }

    @Override
    public boolean doSwordAction(PlayerEntity player, ItemStack swordStack){
        if(!(player.getWorld() instanceof ServerWorld sWorld)) return false;
        TeleportTarget teleportTarget = createTeleportTarget(sWorld, player, player.getBlockPos());
        if (teleportTarget != null) {
            ServerWorld otherWorld = teleportTarget.world();
            if (sWorld.getServer().isWorldAllowed(otherWorld)
                && (otherWorld.getRegistryKey() == sWorld.getRegistryKey()
                || player.canTeleportBetween(sWorld, otherWorld))) {
                player.teleportTo(teleportTarget);
            }
        }
        return true;
    }

    @Nullable
    public static TeleportTarget createTeleportTarget(ServerWorld initialWorld, Entity entity, BlockPos pos) {
        RegistryKey<World> registryKey = initialWorld.getRegistryKey() == World.NETHER ? World.OVERWORLD : World.NETHER;
        ServerWorld otherWorld = initialWorld.getServer().getWorld(registryKey);
        if (otherWorld == null) { return null; }
        if(!(entity instanceof ServerPlayerEntity player)) return null;
        boolean isNether = otherWorld.getRegistryKey() == World.NETHER;
        WorldBorder worldBorder = otherWorld.getWorldBorder();
        double coordScale = DimensionType.getCoordinateScaleFactor(initialWorld.getDimension(), otherWorld.getDimension());
        BlockPos scaledPos = worldBorder.clamp(entity.getX() * coordScale,
            Math.clamp(entity.getY(), otherWorld.getBottomY()+1, otherWorld.getTopY()-1),
            entity.getZ() * coordScale);

        TeleportTarget.PostDimensionTransition postDimensionTransition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET
            .then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET).then(entTeleported -> {
                // try to make it safer if possible.
                BlockState baseState = otherWorld.getBlockState(scaledPos);
                if(baseState.shouldSuffocate(otherWorld, scaledPos)
                    && otherWorld.canPlayerModifyAt(player, scaledPos)
                    && baseState.getBlock().getHardness() > 0){
                    otherWorld.breakBlock(scaledPos, true, player);
                }
                // if you're scaled up that's on you.
                BlockPos headPos = scaledPos.offset(Direction.UP);
                BlockState headState = otherWorld.getBlockState(headPos);
                if(headState.shouldSuffocate(otherWorld, headPos)
                    && otherWorld.canPlayerModifyAt(player, headPos)
                    && headState.getBlock().getHardness() > 0){
                    otherWorld.breakBlock(headPos, true, player);
                }
                // place a block under you if needed.
                BlockPos underPos = scaledPos.offset(Direction.DOWN);
                if(otherWorld.getBlockState(underPos).isAir() && otherWorld.canPlayerModifyAt(player, underPos)){
                    otherWorld.setBlockState(underPos, isNether ? Blocks.NETHERRACK.getDefaultState() : Blocks.DIRT.getDefaultState());
                }
                // any other safety stuff ? maybe fire resistance ?
            });
        return new TeleportTarget(otherWorld, scaledPos.toCenterPos(), entity.getVelocity(), entity.getYaw(), entity.getPitch(), postDimensionTransition);
    }
}
