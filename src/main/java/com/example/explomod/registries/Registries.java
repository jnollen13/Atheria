package com.example.explomod.registries;

import com.example.explomod.ExploMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class Registries {

    public static final ResourceKey<Registry<ResourceLocation>> HIDDEN_STAT = createRegistryKey("hidden_stat");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String name) {
        return ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("z", name));
    }
}
