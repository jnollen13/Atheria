package datagen;

import com.example.explomod.entity.ModEntities;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.HolderGetter;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.fml.common.Mod;
import worldgen.AtheriaBiomes;
import worldgen.ModPlacedFeatures;

import java.util.List;

public class AtheriaBiomeBuilders {
    public static Biome skyrootMeadowBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.STORMBERRY_BUSH_PLACED_KEY));
    }

    public static Biome skyrootGroveBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WINTER_OAK_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLOODWOOD_PLACED_KEY));
    }

    public static Biome skyrootWoodlandBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.WINTER_OAK_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLOODWOOD_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.DARK_OAK_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.STORMBERRY_BUSH_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacedFeatures.BLOODWOOD_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacedFeatures.STORMBERRY_BUSH_PLACED_KEY)
                .addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, ModPlacedFeatures.WINTER_OAK_PLACED_KEY));
    }

    public static Biome skyrootForestBiome(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        return makeDefaultBiome(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers).addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, ModPlacedFeatures.BLOODWOOD_PLACED_KEY));
    }

    public static Biome makeDefaultBiome(BiomeGenerationSettings.Builder builder) {
        return fullDefinition(
                true,
                0.8F,
                0.1F,
                new BiomeSpecialEffects.Builder()
                        .fogColor(0x93_93_bc)
                        .skyColor(0xc0_c0_ff)
                        .waterColor(0x3f_76_e4)
                        .waterFogColor(0x05_05_33)
                        .grassColorOverride(0xb1_ff_cb)
                        .foliageColorOverride(0xb1_ff_cb)
                        .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE)
                        .backgroundMusic(Musics.createGameMusic(SoundEvents.AMBIENT_NETHER_WASTES_MOOD))
                        .build(),
                new MobSpawnSettings.Builder()
                        .addMobCharge(ModEntities.GRASS_GOLEM.get(), 0.1, 0.1)
                        .addMobCharge(ModEntities.TRADER.get(), 0.5, 0.11)
                        .creatureGenerationProbability(0.25F)
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.TRADER.get(), 2, 1, 1))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.GECKO.get(), 5, 1, 5))
                        .addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.GRASS_GOLEM.get(), 12, 3, 14))
                        .build(),
                builder
                        .build(),
                Biome.TemperatureModifier.NONE
        );
    }

    public static Biome fullDefinition(boolean precipitation, float temperature, float downfall, BiomeSpecialEffects effects, MobSpawnSettings spawnSettings, BiomeGenerationSettings generationSettings, Biome.TemperatureModifier temperatureModifier) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(precipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects(effects)
                .mobSpawnSettings(spawnSettings)
                .generationSettings(generationSettings)
                .temperatureAdjustment(temperatureModifier)
                .build();
    }

    public static BiomeSource buildAetherBiomeSource(HolderGetter<Biome> biomes) {
        Climate.Parameter fullRange = Climate.Parameter.span(-1.0F, 1.0F);
        Climate.Parameter temps1 = Climate.Parameter.span(-1.0F, -0.8F);
        Climate.Parameter temps2 = Climate.Parameter.span(-0.8F, 0.0F);
        Climate.Parameter temps3 = Climate.Parameter.span(0.0F, 0.4F);
        Climate.Parameter temps4 = Climate.Parameter.span(0.4F, 0.93F);
        Climate.Parameter temps5 = Climate.Parameter.span(0.93F, 0.94F);
        Climate.Parameter temps6 = Climate.Parameter.span(0.94F, 1.0F);
        return MultiNoiseBiomeSource.createFromList(new Climate.ParameterList<>(List.of(
                // Row 1
                Pair.of(new Climate.ParameterPoint(temps1, fullRange, fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_MEADOW)),
                // Row 2
                Pair.of(new Climate.ParameterPoint(temps2, Climate.Parameter.span(-1.0F, 0.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_MEADOW)),
                Pair.of(new Climate.ParameterPoint(temps2, Climate.Parameter.span(0.0F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_FOREST)),
                // Row 3
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(-1.0F, 0.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_GROVE)),
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(0.0F, 0.8F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_FOREST)),
                Pair.of(new Climate.ParameterPoint(temps3, Climate.Parameter.span(0.8F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_GROVE)),
                // Row 4
                Pair.of(new Climate.ParameterPoint(temps4, Climate.Parameter.span(-1.0F, -0.1F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_GROVE)),
                Pair.of(new Climate.ParameterPoint(temps4, Climate.Parameter.span(-0.1F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_FOREST)),
                // Row 5
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-1.0F, -0.6F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_MEADOW)),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-0.6F, -0.3F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_GROVE)),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(-0.3F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_FOREST)),
                // Row 6
                Pair.of(new Climate.ParameterPoint(temps6, Climate.Parameter.span(-1.0F, -0.1F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_MEADOW)),
                Pair.of(new Climate.ParameterPoint(temps6, Climate.Parameter.span(-0.1F, 0.8F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_WOODLAND)),
                Pair.of(new Climate.ParameterPoint(temps5, Climate.Parameter.span(0.8F, 1.0F), fullRange, fullRange, fullRange, fullRange, 0),
                        biomes.getOrThrow(AtheriaBiomes.SKYROOT_FOREST))
        )));
    }
}
