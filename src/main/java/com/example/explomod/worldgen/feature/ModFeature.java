package com.example.explomod.worldgen.feature;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public abstract class ModFeature<FC extends FeatureConfiguration>{
    public static final DeferredRegister<Feature<?>> FEATURE =
            DeferredRegister.create(BuiltInRegistries.FEATURE, ExploMod.MODID);

    public static final DeferredHolder<Feature<?>, Feature<TwoBlockConfiguration>> TWO_BLOCK = FEATURE.register("two_block", () -> new TwoBlockFeature(TwoBlockConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, Feature<TwoBlockConfiguration>> MINI_TREE = FEATURE.register("mini_tree", () -> new SmallTreeFeature(TwoBlockConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURE.register(eventBus);
    }
}
