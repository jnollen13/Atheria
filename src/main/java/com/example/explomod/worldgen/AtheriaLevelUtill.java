package com.example.explomod.worldgen;

import com.example.explomod.Config;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class AtheriaLevelUtill {
    public static ResourceKey<Level> destinationDimension() {
        return ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(Config.SERVER.portal_destination_dimension_ID.get()));
    }

    public static ResourceKey<Level> returnDimension() {
        return ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(Config.SERVER.portal_return_dimension_ID.get()));
    }

    public static ResourceKey<Level> fallDimension() {
        return ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse("minecraft:the_nether"));
    }
}
