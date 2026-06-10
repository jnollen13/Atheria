package worldgen;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.ModEntities;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import utill.ModTags;

import java.util.List;


public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> ADD_RADIUM_ORE = registerKey("add_radium_ore");
    public static final ResourceKey<BiomeModifier> ADD_FOOD_ORE = registerKey("add_food_ore");
    public static final ResourceKey<BiomeModifier> ADD_TREE_BLOODWOOD = registerKey("add_tree_bloodwood");
    public static final ResourceKey<BiomeModifier> ADD_TREE_WINTER_OAK = registerKey("add_tree_winter_oak");
    public static final ResourceKey<BiomeModifier> ADD_STORMBERRY_BUSH = registerKey("add_stormberry_bush");
    public static final ResourceKey<BiomeModifier> SPAWN_GRASS_GOLEM = registerKey("spawn_grass_golem");
    public static final ResourceKey<BiomeModifier> SPAWN_TRADER = registerKey("spawn_trader");


    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        // CF -> PF -> BM
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(ADD_RADIUM_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_FOREST),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.RADIUM_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_FOOD_ORE, new BiomeModifiers.AddFeaturesBiomeModifier(
                biomes.getOrThrow(ModTags.Biomes.IS_KNOWN),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.FOOD_ORE_PLACED_KEY)),
                GenerationStep.Decoration.UNDERGROUND_ORES));

        context.register(ADD_TREE_BLOODWOOD, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.MANGROVE_SWAMP)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.BLOODWOOD_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_TREE_WINTER_OAK, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.FLOWER_FOREST), biomes.getOrThrow(Biomes.SNOWY_TAIGA)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.WINTER_OAK_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(ADD_STORMBERRY_BUSH, new BiomeModifiers.AddFeaturesBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.DARK_FOREST), biomes.getOrThrow(Biomes.LUSH_CAVES)),
                HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.STORMBERRY_BUSH_PLACED_KEY)),
                GenerationStep.Decoration.VEGETAL_DECORATION));

        context.register(SPAWN_GRASS_GOLEM, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.LUSH_CAVES), biomes.getOrThrow(Biomes.PLAINS)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.GRASS_GOLEM.get(), 1, 1, 5))));

        context.register(SPAWN_TRADER, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.MUSHROOM_FIELDS), biomes.getOrThrow(Biomes.NETHER_WASTES), biomes.getOrThrow(Biomes.FLOWER_FOREST), biomes.getOrThrow(Biomes.WINDSWEPT_GRAVELLY_HILLS)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.TRADER.get(), 1, 1, 1))));
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
    }
}