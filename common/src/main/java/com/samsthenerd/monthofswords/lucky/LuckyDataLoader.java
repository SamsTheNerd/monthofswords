package com.samsthenerd.monthofswords.lucky;

import com.google.gson.*;
import com.samsthenerd.monthofswords.SwordsMod;
import com.samsthenerd.monthofswords.lucky.DataLuckyFunction.DataCommandLuckyFunction;
import com.samsthenerd.monthofswords.lucky.DataLuckyFunction.DataIdLuckyFunction;
import com.samsthenerd.monthofswords.lucky.LuckyFunction.LuckyModifier;
import com.samsthenerd.monthofswords.lucky.LuckyFunction.Rarity;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

public class LuckyDataLoader extends JsonDataLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public LuckyDataLoader() {
        super(GSON, "luckyswordfunctions");
    }

    private void loadDataFunction(Identifier id, JsonObject funcObj){
        SwordsMod.LOGGER.info("loading data function [" + id + "]: " + funcObj);
        int rarityWeight = Optional.ofNullable(funcObj.getAsJsonPrimitive("rarity"))
            .map(r -> r.isString()
                ? Rarity.valueOf(r.getAsString().toUpperCase()).baseWeight
                : r.getAsInt()
            )
            .orElseThrow(() -> new RuntimeException("No rarity give in lucky function " + id));

        LuckyModifier luckyMod = Optional.ofNullable(funcObj.get("luckymodifier"))
            .map(JsonElement::getAsString)
            .map(String::toUpperCase)
            .map(LuckyModifier::valueOf)
            .orElse(LuckyModifier.NEUTRAL);

        int itemDamage = Optional.ofNullable(funcObj.get("itemDamage")).map(JsonElement::getAsInt).orElse(1);

        Optional<LuckyFunction> maybeIdLF = Optional.ofNullable(funcObj.get("function"))
            .map(JsonElement::getAsString)
            .map(Identifier::of)
            .map(funcId -> new DataIdLuckyFunction(funcId, rarityWeight, luckyMod, itemDamage));

        Optional<LuckyFunction> maybeCommandLF = Optional.ofNullable(funcObj.get("command"))
            .map(JsonElement::getAsString)
            .map(cmd -> new DataCommandLuckyFunction(cmd, id, rarityWeight, luckyMod, itemDamage));

        if(maybeIdLF.isEmpty() && maybeCommandLF.isEmpty()){
            throw new RuntimeException("No function or command field for lucky function");
        }

        LuckyFunction luckyFunc = maybeIdLF.orElseGet(maybeCommandLF::get);

        LuckyHandler.addLuckyFunction(luckyFunc);
    }

    protected void apply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler) {
        for(Entry<Identifier, JsonElement> entry : map.entrySet()) {
            JsonElement contents = entry.getValue();
            if(contents.isJsonArray()){
                JsonArray fArrays = contents.getAsJsonArray();
                for(int i = 0; i < fArrays.size(); i++){
                    String iStr = Integer.toString(i); // cringe don't worry about it
                    JsonObject funcObj = fArrays.get(i).getAsJsonObject();
                    loadDataFunction(entry.getKey().withPath(p -> p + iStr), funcObj);
                }
            } else if(contents.isJsonObject()){
                loadDataFunction(entry.getKey(), contents.getAsJsonObject());
            }
        }
    }
}
