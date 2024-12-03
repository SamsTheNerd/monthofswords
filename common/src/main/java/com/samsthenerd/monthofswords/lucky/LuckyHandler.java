package com.samsthenerd.monthofswords.lucky;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LuckyHandler {

    private static final List<LuckyFunction> LUCKY_FUNCTIONS = new ArrayList<>();

    private static final LoadingCache<Double, IntArrayList> LUCKY_WEIGHTS_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(Duration.ofMinutes(5))
        .build(CacheLoader.from( luck -> {
            int runningWeight = 0;
            IntArrayList runningWeights = new IntArrayList();
            for(LuckyFunction lf : LUCKY_FUNCTIONS){
                runningWeight += lf.getWeight(luck);
                runningWeights.add(runningWeight);
            }
            return runningWeights;
        }));

    private static final Random random = new Random();

    public static LuckyFunction getLucky(double luck){
        IntArrayList weights = LUCKY_WEIGHTS_CACHE.getUnchecked(luck);
        int totalWeight = weights.getLast();
        int randWeight = random.nextInt(0, totalWeight);
        for(int i = 0; i < weights.size(); i++){
            if(weights.getInt(i) >= randWeight){
                return LUCKY_FUNCTIONS.get(i);
            }
        }
        return LUCKY_FUNCTIONS.getLast();
    }

    public static void addLuckyFunction(LuckyFunction luckyFunction){
        LUCKY_FUNCTIONS.add(luckyFunction);
    }

    public static void reload(){
        LUCKY_FUNCTIONS.removeIf(LuckyFunction::fromDataDriven);
        LUCKY_WEIGHTS_CACHE.invalidateAll();
    }
}
