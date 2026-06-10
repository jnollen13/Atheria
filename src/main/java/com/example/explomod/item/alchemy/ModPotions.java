package com.example.explomod.item.alchemy;

import com.example.explomod.ExploMod;
import com.example.explomod.effect.ModEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(BuiltInRegistries.POTION, ExploMod.MODID);

    public static final Holder<Potion> ROCKET_POTION = POTIONS.register("rocket_potion", () -> new Potion(new MobEffectInstance(ModEffects.ROCKET, 3600)));
    public static final Holder<Potion> SPIDER_POTION = POTIONS.register("spider_potion", () -> new Potion(new MobEffectInstance(ModEffects.SLIMEY_EFFECT, 3600)));
    public static final Holder<Potion> SAFE_POTION = POTIONS.register("safe_potion", () -> new Potion(new MobEffectInstance(ModEffects.SAFE_EFFECT, 3600)));
    public static final Holder<Potion> CREEPER_POTION = POTIONS.register("creeper_potion", () -> new Potion(new MobEffectInstance(ModEffects.BOOM_EFFECT, 3600)));
    public static final Holder<Potion> SHORT_POTION = POTIONS.register("short_potion", () -> new Potion(new MobEffectInstance(ModEffects.SHORT, 3605), new MobEffectInstance(ModEffects.HEALTH_DECREASE, 3600), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3582)));
    public static final Holder<Potion> STRONG_SHORT_POTION = POTIONS.register("strong_short_potion", () -> new Potion(new MobEffectInstance(ModEffects.SHORT, 3605, 1), new MobEffectInstance(ModEffects.HEALTH_DECREASE, 3600, 1), new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 3582, 1)));

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
