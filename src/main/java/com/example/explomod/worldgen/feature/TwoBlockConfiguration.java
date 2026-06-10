package com.example.explomod.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record TwoBlockConfiguration(BlockStateProvider toPlace, BlockStateProvider top) implements FeatureConfiguration {
    public static final Codec<TwoBlockConfiguration> CODEC = RecordCodecBuilder.create((p_191331_) -> p_191331_.group(BlockStateProvider.CODEC.fieldOf("to_place")
            .forGetter((p_161168_) -> p_161168_.toPlace)).and(BlockStateProvider.CODEC.fieldOf("top")
            .forGetter((p_161168_) -> p_161168_.top)).apply(p_191331_, TwoBlockConfiguration::new));
}

