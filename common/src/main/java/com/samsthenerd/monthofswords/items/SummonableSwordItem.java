package com.samsthenerd.monthofswords.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import com.samsthenerd.monthofswords.registry.SwordsModDataAttachments;
import com.samsthenerd.monthofswords.registry.SwordsModItems;
import com.samsthenerd.monthofswords.xplat.SwordsModXPlat;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;

import java.util.UUID;

public class SummonableSwordItem extends SwordtemberItem implements SwordActionHaverServer {

    public static final ToolMaterial SUMMONABLE_MATERIAL = new ClassyToolMaterial(100, 5f, 3f,
        BlockTags.INCORRECT_FOR_DIAMOND_TOOL, 15, () -> Ingredient.EMPTY);

    public SummonableSwordItem(Item.Settings itemSettings) {
        super(SUMMONABLE_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(SUMMONABLE_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if(world.isClient()) return; // nothing on client
        var swordSscd = stack.get(SwordsModComponents.SUMMON_SWORD_DATA);
        if(swordSscd == null) return; // if the sword doesn't have stuff then it's prob just from creative mode or something and we ignore it
        if(!swordSscd.player.equals(entity.getUuid())){ // if the sword has the wrong owner then we trash it.
            stack.decrement(1);
            return;
        }
        // we have the right owner, now just check that it's latest so we can trash old swords.
        var optSD = SwordsModXPlat.getInstance().getEntityTarget(entity).getAttached(SwordsModDataAttachments.SUMMON_SWORD_DATA_ATTACHMENT_TYPE);
        if(optSD.isEmpty()) return; // owner has no data? that's probably wrong, ignore it.
        var attachSscd = optSD.get().stack().get(SwordsModComponents.SUMMON_SWORD_DATA);
        if(attachSscd == null) return; // owner has data but it's malformed? also probably wrong, ignore.
        // if it's not the latest sword remove it.
        if(!attachSscd.summonId().equals(swordSscd.summonId)){
            stack.decrement(1);
            return;
        }
    }

    public static void trySummonSword(PlayerEntity player){

        var optSD = SwordsModXPlat.getInstance().getEntityTarget(player).modifyAttached(SwordsModDataAttachments.SUMMON_SWORD_DATA_ATTACHMENT_TYPE,
            optSDOld -> optSDOld.map(SummonSwordData::withFreshUuid));
        if(optSD.isEmpty()) return;
        ItemStack stack = optSD.get().stack();
        if(stack.isEmpty()) return;
        player.giveItemStack(stack.copy());
    }

    @Override
    public boolean doSwordAction(PlayerEntity player, ItemStack swordStack) {
        SwordsModXPlat.getInstance().getEntityTarget(player)
            .modifyAttached(SwordsModDataAttachments.SUMMON_SWORD_DATA_ATTACHMENT_TYPE, optSD -> optSD.map(sd -> sd.saveSwordInfo(swordStack)));
        swordStack.setCount(0);
        return true;
    }

    public record SummonSwordData(ItemStack stack){
        public static SummonSwordData getFreshData(PlayerEntity player){
            UUID summonId = UUID.randomUUID();
            ItemStack sword = new ItemStack(SwordsModItems.SUMMONED_SWORD.get());
            sword.set(SwordsModComponents.SUMMON_SWORD_DATA, new SummonSwordComponentData(player.getUuid(), summonId));
            return new SummonSwordData(sword);
        }

        public SummonSwordData withFreshUuid(){
            SummonSwordComponentData sscd = stack.get(SwordsModComponents.SUMMON_SWORD_DATA);
            if(sscd == null) return this;
            var sscdNew = new SummonSwordComponentData(sscd.player, UUID.randomUUID());
            ItemStack newStack = stack.copy();
            newStack.set(SwordsModComponents.SUMMON_SWORD_DATA, sscdNew);
            return new SummonSwordData(newStack);
        }

        public SummonSwordData saveSwordInfo(ItemStack newSword){
            var newerSword = newSword.copy();
            newerSword.setDamage(0);
            newerSword.set(DataComponentTypes.REPAIR_COST, 0);
            SummonSwordComponentData sscd = stack.get(SwordsModComponents.SUMMON_SWORD_DATA);
//            if(sscd == null) return this;
//            var sscdNew = new SummonSwordComponentData(sscd.player, UUID.randomUUID());
            newerSword.set(SwordsModComponents.SUMMON_SWORD_DATA, sscd);
            return new SummonSwordData(newerSword);
        }

        public static SummonSwordData getBlankData(){
            return new SummonSwordData(ItemStack.EMPTY);
        }
    }

    public record SummonSwordComponentData(UUID player, UUID summonId){
        public static final Codec<SummonSwordComponentData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Uuids.CODEC.fieldOf("playerId").forGetter(SummonSwordComponentData::player),
                Uuids.CODEC.fieldOf("summonId").forGetter(SummonSwordComponentData::summonId)
            ).apply(instance, SummonSwordComponentData::new)
        );

        public static final PacketCodec<? super RegistryByteBuf, SummonSwordComponentData> PACKET_CODEC = PacketCodec.tuple(
            Uuids.PACKET_CODEC, SummonSwordComponentData::player,
            Uuids.PACKET_CODEC, SummonSwordComponentData::summonId,
            SummonSwordComponentData::new
        );
    }
}
