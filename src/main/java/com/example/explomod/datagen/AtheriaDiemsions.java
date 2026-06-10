package datagen;

import com.example.explomod.ExploMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

import java.util.OptionalLong;

public class AtheriaDiemsions {
    private final static ResourceLocation AETHER_LEVEL_ID = ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "dark");

    // DimensionType - Specifies the logic and settings for a dimension.
    public static final ResourceKey<DimensionType> DARK_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, AETHER_LEVEL_ID);
    // Level - The dimension during runtime.
    public static final ResourceKey<Level> AETHER_LEVEL = ResourceKey.create(Registries.DIMENSION, AETHER_LEVEL_ID);
    // LevelStem - The dimension during lifecycle start and datagen.
    public static final ResourceKey<LevelStem> AETHER_LEVEL_STEM = ResourceKey.create(Registries.LEVEL_STEM, AETHER_LEVEL_ID);

    public static void bootstrapDimensionType(BootstrapContext<DimensionType> context) {
        context.register(DARK_DIMENSION_TYPE, new DimensionType(
                OptionalLong.empty(),
                false,
                false,
                false,
                true,
                32.0,
                true,
                true,
                -64,
                384,
                384,
                BlockTags.INFINIBURN_OVERWORLD,
                ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "dark"),
                0.0F,
                new DimensionType.MonsterSettings(true, true, UniformInt.of(0, 7), 0)));
    }

    public static void bootstrapLevelStem(BootstrapContext<LevelStem> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        BiomeSource source = AtheriaBiomeBuilders.buildAetherBiomeSource(biomes);
        NoiseBasedChunkGenerator aetherChunkGen = new NoiseBasedChunkGenerator(source, noiseSettings.getOrThrow(AtheriaNoiseSettings.SKYLANDS));
        context.register(AETHER_LEVEL_STEM, new LevelStem(dimensionTypes.getOrThrow(AtheriaDiemsions.DARK_DIMENSION_TYPE), aetherChunkGen));
    }
}
