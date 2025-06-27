package com.samsthenerd.monthofswords.items;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.samsthenerd.monthofswords.registry.SwordsModComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CrystalSwordItem extends SwordtemberItem {

    public static final ToolMaterial AMETHYST_CRYSTAL_MATERIAL = new ClassyToolMaterial(500, 5f, 2f,
        BlockTags.INCORRECT_FOR_IRON_TOOL, 16, () -> Ingredient.ofItems(Items.COPPER_INGOT));

    public CrystalSwordItem(Item.Settings itemSettings) {
        super(AMETHYST_CRYSTAL_MATERIAL, itemSettings.attributeModifiers(
            SwordItem.createAttributeModifiers(AMETHYST_CRYSTAL_MATERIAL, 3, -2.4f))
        );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        var resCompOpt = Optional.ofNullable(stack.get(SwordsModComponents.RESONANCE_DATA));
        resCompOpt.flatMap(ResonatingComponent::getDamageStateText).ifPresent(tooltip::add);
        super.appendTooltip(stack, context, tooltip, type);
    }

    @Override
    public float getBonusAttackDamage(Entity target, float baseAttackDamage, DamageSource damageSource) {
        ItemStack stack = damageSource.getWeaponStack();
        float baseDamage = super.getBonusAttackDamage(target, baseAttackDamage, damageSource);
        if(stack == null || stack.isEmpty()) return baseDamage;
        ResonatingComponent resComp = stack.get(SwordsModComponents.RESONANCE_DATA);
        if(resComp == null) return baseDamage;
        if(EntityType.getId(target.getType()).equals(resComp.entityType)) return resComp.extraDamage + baseDamage;
        return baseDamage;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        var resCompOpt = Optional.ofNullable(stack.get(SwordsModComponents.RESONANCE_DATA));
        Identifier targetType = EntityType.getId(target.getType());
        ResonatingComponent resComp = resCompOpt.map(resCompOld -> {
            float newDamage = resCompOld.extraDamage;
            if(resCompOld.lastHit.equals(target.getUuid())){
                newDamage += 0.05f;
            } else if(resCompOld.entityType.equals(targetType)){
                newDamage += 0.5f;
            } else {
                newDamage = 0.5f;
            }
            newDamage = Math.round(newDamage * 1000) / 1000f;
            return new ResonatingComponent(targetType, target.getUuid(), newDamage);
        }).orElse(new ResonatingComponent(targetType, target.getUuid(), 0.5f));
        stack.set(SwordsModComponents.RESONANCE_DATA, resComp);
        if(attacker instanceof ServerPlayerEntity sPlayer) resComp.getDamageStateText().ifPresent(t -> sPlayer.sendMessage(t, true));
        super.postDamageEntity(stack, target, attacker);
    }

    public record ResonatingComponent(Identifier entityType, UUID lastHit, float extraDamage){
        public static final Codec<ResonatingComponent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                Identifier.CODEC.fieldOf("entityType").forGetter(ResonatingComponent::entityType),
                Uuids.CODEC.fieldOf("lastHit").forGetter(ResonatingComponent::lastHit),
                Codec.FLOAT.fieldOf("extraDamage").forGetter(ResonatingComponent::extraDamage)
            ).apply(instance, ResonatingComponent::new)
        );

        public static final PacketCodec<? super RegistryByteBuf, ResonatingComponent> PACKET_CODEC = PacketCodec.tuple(
            Identifier.PACKET_CODEC, ResonatingComponent::entityType,
            Uuids.PACKET_CODEC, ResonatingComponent::lastHit,
            PacketCodecs.FLOAT, ResonatingComponent::extraDamage,
            ResonatingComponent::new
        );

        public Optional<Text> getDamageStateText(){
            return Registries.ENTITY_TYPE.getOrEmpty(entityType).map(
                entType -> entType.getName().copy().append(": " + (extraDamage == 0 ? "" : "+") + extraDamage)
                    .append(" ").append(Text.translatable("attribute.name.attack_damage"))
                    .formatted(Formatting.GREEN, Formatting.ITALIC));
        }
    }
}
