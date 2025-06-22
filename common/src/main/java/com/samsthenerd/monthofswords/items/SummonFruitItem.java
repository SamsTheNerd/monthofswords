package com.samsthenerd.monthofswords.items;

import com.samsthenerd.monthofswords.items.SummonableSwordItem.SummonSwordData;
import com.samsthenerd.monthofswords.registry.SwordsModDataAttachments;
import com.samsthenerd.monthofswords.xplat.SwordsModXPlat;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class SummonFruitItem extends Item {

    public final boolean summonOrBanish;

    public SummonFruitItem(Settings settings, boolean summonOrBanish) {
        super(settings);
        this.summonOrBanish = summonOrBanish;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if(user instanceof ServerPlayerEntity sPlayer){
            SwordsModXPlat.getInstance().getEntityTarget(user).modifyAttached(SwordsModDataAttachments.SUMMON_SWORD_DATA_ATTACHMENT_TYPE,
                optSD -> {
                    if(optSD.isEmpty() && summonOrBanish){
                        return Optional.of(SummonSwordData.getFreshData(sPlayer));
                    } else if (!summonOrBanish){
                        // reset it?
                        return Optional.of(SummonSwordData.getFreshData(sPlayer));
                    }
                    return optSD;
                });
        }
        return super.finishUsing(stack, world, user);
    }
}
