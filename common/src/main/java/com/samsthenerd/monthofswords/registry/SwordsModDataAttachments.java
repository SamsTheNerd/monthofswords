package com.samsthenerd.monthofswords.registry;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.items.SummonableSwordItem;
import com.samsthenerd.monthofswords.items.SummonableSwordItem.SummonSwordData;
import com.samsthenerd.monthofswords.xplat.CAttachmentType;
import com.samsthenerd.monthofswords.xplat.SwordsModXPlat;
import net.minecraft.item.ItemStack;

public class SwordsModDataAttachments {

    public static final CAttachmentType<SummonableSwordItem.SummonSwordData> SUMMON_SWORD_DATA_ATTACHMENT_TYPE =
        SwordsModXPlat.getInstance().createAttachment(SwordsMod.id("summon_sword_data"), SummonableSwordItem.SummonSwordData::getBlankData,
            builder -> builder.persistent(ItemStack.CODEC.xmap(SummonSwordData::new, SummonSwordData::stack), true));

    // just classload it :p
    public static void init(){}
}
