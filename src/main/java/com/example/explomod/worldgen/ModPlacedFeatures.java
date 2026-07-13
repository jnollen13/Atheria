package com.example.explomod.worldgen;

import com.example.explomod.ExploMod;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlacedFeatures {

    public static final ResourceKey<PlacedFeature> RADIUM_ORE_PLACED_KEY = registerKey("radium_ore_placed");
    public static final ResourceKey<PlacedFeature> FOOD_ORE_PLACED_KEY = registerKey("food_ore");
    public static final ResourceKey<PlacedFeature> BLOODWOOD_PLACED_KEY = registerKey("bloodwood_placed");
    public static final ResourceKey<PlacedFeature> LUSH_CAVES_CEILING_VEGETATION = registerKey("lush_caves_ceiling_vegetation");
    public static final ResourceKey<PlacedFeature> VEG1_PLACED = registerKey("veg1_placed");
    public static final ResourceKey<PlacedFeature> R_VEG1_PLACED = registerKey("r_veg1_placed");
    public static final ResourceKey<PlacedFeature> WEB1_PLACED = registerKey("web1_placed");
    public static final ResourceKey<PlacedFeature> WEB2_PLACED = registerKey("web2_placed");
    public static final ResourceKey<PlacedFeature> RED_FLOWER_PLACED = registerKey("red_flowers");
    public static final ResourceKey<PlacedFeature> SVEG1_CEILING_PLACED = registerKey("veg1_ceiling_placed");
    public static final ResourceKey<PlacedFeature> VEG2_PLACED = registerKey("veg2_placed");
    public static final ResourceKey<PlacedFeature> VEG3_PLACED = registerKey("veg3_placed");
    public static final ResourceKey<PlacedFeature> SVEG1_PLACED = registerKey("sveg1_placed");
    public static final ResourceKey<PlacedFeature> GRASS_PLACED = registerKey("grass_placed");
    public static final ResourceKey<PlacedFeature> VEG1B_PLACED = registerKey("veg1b_placed");
    public static final ResourceKey<PlacedFeature> GLOWBERRY_BUSH_PLACED = registerKey("glowberry_bush");
    public static final ResourceKey<PlacedFeature> DARK_OAK_PLACED_KEY = registerKey("dark_oak_placed");
    public static final ResourceKey<PlacedFeature> WINTER_OAK_PLACED_KEY = registerKey("winter_oak_placed");
    public static final ResourceKey<PlacedFeature> STORMBERRY_BUSH_PLACED_KEY = registerKey("stormberry_bush_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<ConfiguredFeature<?, ?>> holdergetter = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> holder12 = holdergetter.getOrThrow(CaveFeatures.MOSS_PATCH_CEILING);

        register(context, RADIUM_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.OVERWORLD_RADIUM_ORE_KEY),
                AetheriaOrePlacement.commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(80))));

        register(context, FOOD_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.FOOD_ORE_KEY),
                AetheriaOrePlacement.rareOrePlacement(100, HeightRangePlacement.triangle(VerticalAnchor.absolute(-128), VerticalAnchor.absolute(320))));

        register(context, BLOODWOOD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.BLOODWOOD_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.1f, 2),
                        ExploMod.BLOODWOOD_SAPLING.get()));

        register(context, WINTER_OAK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.WINTER_OAK_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.2f, 2),
                        ExploMod.WINTER_OAK_SAPLING.get()));

        PlacementUtils.register(context, VEG1_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.UNDER_VEG_1_KEY),
                CountPlacement.of(22), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.MOSS_BLOCK, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, WEB1_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.WEB1_KEY),
                CountPlacement.of(128), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.STONE,Blocks.BLACKSTONE,Blocks.DRIPSTONE_BLOCK),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, RED_FLOWER_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.RED_FLOWER_KEY),
                CountPlacement.of(64), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK,Blocks.DIRT,Blocks.MOSS_BLOCK,Blocks.PODZOL),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, WEB2_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.WEB2_KEY),
                CountPlacement.of(64), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.STONE,Blocks.BLACKSTONE,Blocks.DRIPSTONE_BLOCK),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, R_VEG1_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.ROOTED_VEG_1_KEY),
                CountPlacement.of(32), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.PODZOL, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(0)), BiomeFilter.biome());

        PlacementUtils.register(context, GRASS_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.GRASS_KEY),
                CountPlacement.of(128), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.MOSS_BLOCK, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, VEG2_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.VEG_2_KEY),
                CountPlacement.of(25), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.MOSS_BLOCK, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, VEG3_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.VEG_3_KEY),
                CountPlacement.of(30), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.COARSE_DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, SVEG1_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.S_VEG_1_KEY),
                CountPlacement.of(20), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.MOSS_BLOCK, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, SVEG1_CEILING_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.S_VEG_1_KEY),
                CountPlacement.of(28), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.matchesBlocks(Blocks.STONE, Blocks.MOSS_BLOCK, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(-1)), BiomeFilter.biome());

        PlacementUtils.register(context, GLOWBERRY_BUSH_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.GLOWBERRY_BUSH_KEY),
                CountPlacement.of(4), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.GRASS_BLOCK, Blocks.MOSS_BLOCK, Blocks.DIRT),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.vertical(ConstantInt.of(1)), BiomeFilter.biome());

        PlacementUtils.register(context, VEG1B_PLACED, configuredFeatures.getOrThrow(ModConfiguredFeatures.S_VEG_1_KEY),
                CountPlacement.of(20), InSquarePlacement.spread(), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.DARK_OAK_LEAVES),
                        BlockPredicate.ONLY_IN_AIR_PREDICATE, 12), RandomOffsetPlacement.horizontal(ConstantInt.of(1)), BiomeFilter.biome());

        register(context, DARK_OAK_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DARK_OAK_KEY),
                VegetationPlacements.treePlacement(PlacementUtils.countExtra(3, 0.01f, 2),
                        Blocks.DARK_OAK_SAPLING));

        register(context, STORMBERRY_BUSH_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.STORMBERRY_BUSH_KEY),
                List.of(RarityFilter.onAverageOnceEvery(64), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));

        PlacementUtils.register(
                context,
                LUSH_CAVES_CEILING_VEGETATION,
                holder12,
                CountPlacement.of(50),
                InSquarePlacement.spread(),
                PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
                EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 12),
                RandomOffsetPlacement.vertical(ConstantInt.of(-1)),
                BiomeFilter.biome()
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}