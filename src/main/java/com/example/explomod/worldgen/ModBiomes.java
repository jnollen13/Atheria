package com.example.explomod.worldgen;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(Registries.BIOME, ExploMod.MODID);

    public static final TagKey<Biome> SPIDER_NESTS = BIOMES.createTagKey(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "spider_nests"));

    public static void register(IEventBus eventBus) {
        BIOMES.register(eventBus);
    }

    public static final ResourceKey<Biome> SPIDER_NESTS2 = ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "spider_nets")
    );
}
