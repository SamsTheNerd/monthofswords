package com.samsthenerd.monthofswords.lucky;

import com.mojang.serialization.Codec;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.StringIdentifiable;

import java.util.function.BiFunction;

public interface LuckyFunction {
    // should be >= 0
    int getWeight(double luck);

    // returns how much durability should be taken
    int attack(LivingEntity target, LivingEntity attacker);

    boolean fromDataDriven();

    enum Rarity implements StringIdentifiable{
        COMMON("common", 100),
        UNCOMMON("uncommon", 50),
        RARE("rare", 15),
        EPIC("epic", 5),
        WOWZERS("wowzers", 1);

        public final String name; // for codec
        public final int baseWeight;
        Rarity(String name, int baseWeight){
            this.baseWeight = baseWeight;
            this.name = name;
        }

        public String asString(){
            return this.name;
        }

        public static final Codec<Rarity> CODEC = StringIdentifiable.createCodec(Rarity::values);
    }

    enum LuckyModifier implements StringIdentifiable {
        // prob need a better name for these -- it should maybe be more like, luck sensitivity ?
        VERYGOOD("verygood", (luck, baseWeight) -> (int)(0.4 * luck * baseWeight) + baseWeight),
        GOOD("good", (luck, baseWeight) -> (int)(0.15 * luck * baseWeight) + baseWeight),
        NEUTRAL("neutral", (luck, baseWeight) -> baseWeight),
        BAD("bad", (luck, baseWeight) -> (int)(-0.15 * luck * baseWeight) + baseWeight),
        VERYBAD("verybad", (luck, baseWeight) -> (int)(-0.4 * luck * baseWeight) + baseWeight);

        public final String name;
        private final BiFunction<Double, Integer, Integer> luckModFunc;
        LuckyModifier(String name, BiFunction<Double, Integer, Integer> luckModFunc){
            this.name = name;
            this.luckModFunc = luckModFunc;
        }

        public String asString(){
            return this.name;
        }

        public int modify(int baseWeight, double luck){
            return luckModFunc.apply(luck, baseWeight);
        }

        public static final Codec<LuckyModifier> CODEC = StringIdentifiable.createCodec(LuckyModifier::values);
    }

    record CodeLuckyFunction(BiFunction<LivingEntity, LivingEntity, Integer> attackFunc,
                                    int rarityWeight, LuckyModifier modifier) implements LuckyFunction{

        // should be >= 0
        @Override
        public int getWeight(double luck){
            return modifier().modify(rarityWeight(), luck);
        }

        // returns how much durability should be taken
        @Override
        public int attack(LivingEntity target, LivingEntity attacker){
            return attackFunc.apply(target, attacker);
        }

        @Override
        public boolean fromDataDriven(){
            return false;
        }
    }
}
