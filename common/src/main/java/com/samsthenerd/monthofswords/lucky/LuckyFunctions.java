package com.samsthenerd.monthofswords.lucky;

import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.lucky.DataLuckyFunction.DataCommandLuckyFunction;
import com.samsthenerd.monthofswords.lucky.LuckyFunction.LuckyModifier;
import com.samsthenerd.monthofswords.lucky.LuckyFunction.Rarity;

public class LuckyFunctions {
    public static void register(){
//        LuckyHandler.addLuckyFunction(new CodeLuckyFunction((target, attacker) -> {
//            ItemEntity itemEnt = new ItemEntity(attacker.getWorld(), attacker.getPos().getX(), attacker.getPos().getY(), attacker.getPos().getZ(),
//                new ItemStack(Items.GOLD_INGOT));
//            attacker.getWorld().spawnEntity(itemEnt);
//            return 1;
//        }, Rarity.COMMON, LuckyModifier.NEUTRAL));

//        LuckyHandler.addLuckyFunction(new DataIdLuckyFunction(SwordsMod.id("test"), Rarity.COMMON, LuckyModifier.NEUTRAL, 10));

        LuckyHandler.addLuckyFunction(new DataCommandLuckyFunction("give @s stick", SwordsMod.id("test"),
            Rarity.COMMON.baseWeight, LuckyModifier.NEUTRAL, 10));
    }
}
