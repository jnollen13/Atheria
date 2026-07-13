package com.example.explomod.worldgen;

import com.example.explomod.ExploMod;
import com.example.explomod.block.custom.GlowBerryBushBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.ForkingTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.GiantTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import com.example.explomod.utill.ModTags;
import com.example.explomod.worldgen.feature.ModFeature;
import com.example.explomod.worldgen.feature.TwoBlockConfiguration;

import java.util.List;

public class ModConfiguredFeatures {

    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_RADIUM_ORE_KEY = registerKey("radium_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BLOODWOOD_KEY = registerKey("bloodwood");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WINTER_OAK_KEY = registerKey("winter_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_KEY = registerKey("dark_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> UNDER_VEG_1_KEY = registerKey("dark_oak_stack");
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_FLOWER_KEY = registerKey("red_flower");
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROOTED_VEG_1_KEY = registerKey("rooted_dark_oak_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VEG_2_KEY = registerKey("mini_bloodwood_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> VEG_3_KEY = registerKey("mini_spruce_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GRASS_KEY = registerKey("grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WEB1_KEY = registerKey("cobweb01");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WEB2_KEY = registerKey("cobweb02");
    public static final ResourceKey<ConfiguredFeature<?, ?>> S_VEG_1_KEY = registerKey("dark_oak_leaves_block");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FOOD_ORE_KEY = registerKey("food_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> GLOWBERRY_BUSH_KEY = registerKey("glowberry_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> STORMBERRY_BUSH_KEY = registerKey("stormberry_bush");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest oreReplaceables = new TagMatchTest(ModTags.Blocks.ORES);

        List<OreConfiguration.TargetBlockState> overworldRadiumOres = List.of(
                OreConfiguration.target(stoneReplaceables, ExploMod.RADIUM_ORE.get().defaultBlockState()));

        List<OreConfiguration.TargetBlockState> stoneFoodOres = List.of(
                OreConfiguration.target(stoneReplaceables, ExploMod.EXAMPLE_BLOCK.get().defaultBlockState()));

        register(context, FOOD_ORE_KEY, Feature.ORE, new OreConfiguration(stoneFoodOres, 1));

        FeatureUtils.register(context, UNDER_VEG_1_KEY, ModFeature.TWO_BLOCK.get(), new TwoBlockConfiguration(BlockStateProvider.simple(Blocks.DARK_OAK_LOG
                .defaultBlockState()),BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true))));

        FeatureUtils.register(context, RED_FLOWER_KEY, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(ExploMod.RED_FLOWER.get()
                .defaultBlockState())));

        FeatureUtils.register(context, ROOTED_VEG_1_KEY, ModFeature.TWO_BLOCK.get(), new TwoBlockConfiguration(BlockStateProvider.simple(Blocks.ROOTED_DIRT
                .defaultBlockState()),BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true))));

        FeatureUtils.register(context, VEG_2_KEY, ModFeature.MINI_TREE.get(), new TwoBlockConfiguration(BlockStateProvider.simple(ExploMod.LOG_BLOCK.get()),
                BlockStateProvider.simple(Blocks.MANGROVE_LEAVES.defaultBlockState().trySetValue(LeavesBlock.PERSISTENT, true))));

        FeatureUtils.register(context, VEG_3_KEY, ModFeature.MINI_TREE.get(), new TwoBlockConfiguration(BlockStateProvider.simple(Blocks.SPRUCE_LOG),
                BlockStateProvider.simple(Blocks.SPRUCE_LEAVES.defaultBlockState().trySetValue(LeavesBlock.PERSISTENT, true))));

        FeatureUtils.register(context, S_VEG_1_KEY, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES.defaultBlockState().setValue(LeavesBlock.PERSISTENT, true))));

        FeatureUtils.register(context, GRASS_KEY, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SHORT_GRASS)));

        FeatureUtils.register(context, WEB1_KEY, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.COBWEB)));

        FeatureUtils.register(context, WEB2_KEY, ModFeature.TWO_BLOCK.get(), new TwoBlockConfiguration(BlockStateProvider.simple(Blocks.COBWEB
                .defaultBlockState()),BlockStateProvider.simple(Blocks.COBWEB.defaultBlockState())));

        register(context, BLOODWOOD_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ExploMod.LOG_BLOCK.get()),
                new StraightTrunkPlacer(2, 4, 3),

                BlockStateProvider.simple(Blocks.MANGROVE_LEAVES),
                new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(3), 3),

                new TwoLayersFeatureSize(3, 2, 4)).build());

        register(context, STORMBERRY_BUSH_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ExploMod.STORMBERRY_BUSH.get()
                                .defaultBlockState().setValue(SweetBerryBushBlock.AGE, 2))
                        ), List.of(Blocks.GRASS_BLOCK,Blocks.DIRT,Blocks.SOUL_SOIL,Blocks.MOSS_BLOCK)));

        register(context, GLOWBERRY_BUSH_KEY, Feature.RANDOM_PATCH,
                FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ExploMod.GLOWBERRY_BUSH.get()
                                .defaultBlockState().setValue(GlowBerryBushBlock.AGE, 2).setValue(GlowBerryBushBlock.GLOWING, true))
                        ), List.of(Blocks.GRASS_BLOCK,Blocks.DIRT,Blocks.MOSS_BLOCK)));

        register(context, OVERWORLD_RADIUM_ORE_KEY, Feature.ORE, new OreConfiguration(overworldRadiumOres, 9));

        register(context, WINTER_OAK_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.OAK_LOG.defaultBlockState().getBlock()),
                new ForkingTrunkPlacer(4, 4, 3),

                BlockStateProvider.simple(ExploMod.FLOWERED_LEAVES.get()),
                new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(3), 4),

                new TwoLayersFeatureSize(1, 0, 3)).build());

        register(context, DARK_OAK_KEY, Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(Blocks.DARK_OAK_LOG.defaultBlockState().getBlock()),
                new GiantTrunkPlacer(18, 4, 3),

                BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
                new BlobFoliagePlacer(ConstantInt.of(4), ConstantInt.of(3), 4),

                new TwoLayersFeatureSize(1, 2, 3)).build());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}