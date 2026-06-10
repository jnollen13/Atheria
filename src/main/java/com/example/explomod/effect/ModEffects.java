package com.example.explomod.effect;

import com.example.explomod.ExploMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModEffects{
    public static final DeferredRegister<MobEffect> MOD_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, ExploMod.MODID);

    public static final Holder<MobEffect> SLIMEY_EFFECT = MOD_EFFECTS.register("slimey",
            () -> new SlimeyEffect(MobEffectCategory.NEUTRAL, 0x36fbab));
    public static final Holder<MobEffect> ROCKET = MOD_EFFECTS.register("rocket_boost", () -> new RocketEffect(MobEffectCategory.NEUTRAL, 16646020).addAttributeModifier(Attributes.JUMP_STRENGTH, ResourceLocation.withDefaultNamespace("effect.rocket_boost"), 41.3F, AttributeModifier.Operation.ADD_VALUE));
    public static final Holder<MobEffect> SHORT = MOD_EFFECTS.register("shortness", () -> new ShortEffect(MobEffectCategory.NEUTRAL, 1646020).addAttributeModifier(Attributes.SCALE, ResourceLocation.withDefaultNamespace("effect.short"), (double) -0.25F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> HEALTH_DECREASE = MOD_EFFECTS.register("health_decrease", () -> new HealthDeduction(MobEffectCategory.HARMFUL, 1646020).addAttributeModifier(Attributes.MAX_HEALTH, ResourceLocation.withDefaultNamespace("effect.healthd"), -0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
    public static final Holder<MobEffect> BOOM_EFFECT = MOD_EFFECTS.register("boom",
            () -> new ExplosionEffect(MobEffectCategory.HARMFUL, 0x17fdea));
    public static final Holder<MobEffect> SAFE_EFFECT = MOD_EFFECTS.register("safe",
            () -> new SafeEffect(MobEffectCategory.BENEFICIAL, 0x93fdea));

    public static void register(IEventBus eventBus) {
        MOD_EFFECTS.register(eventBus);
    }
}
