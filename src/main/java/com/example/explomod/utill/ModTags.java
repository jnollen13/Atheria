package com.example.explomod.utill;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class ModTags {

    public static class Blocks {
        public static final TagKey<Block> NEEDS_PHANTOM_TOOL = createTag("needs_phantom_tool");
        public static final TagKey<Block> INCORRECT_FOR_PHANTOM_TOOL = createTag("incorrect_for_phantom_tool");
        public static final TagKey<Block> NEEDS_COPPER_TOOL = createTag("needs_copper_tool");
        public static final TagKey<Block> ORES = createTag("ores");
        public static final TagKey<Block> OVERWORLD_ORES = createTag("overworld_ores");
        public static final TagKey<Block> NETHER_ORES = createTag("nether_ores");
        public static final TagKey<Block> AETHER_ORES = createTag("aether_ores");//for aether compatibility
        public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = createTag("incorrect_for_copper_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
        }
    }

    public final class cTags {
        public static String tag = "c";
        public static class Blocks {
            public static final TagKey<Block> EXTRA_STONE_ORES = createTag("extra_stone_ores");
            public static final TagKey<Block> NULL = createTag("null");

            private static TagKey<Block> createTag(String name) {
                return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ModTags.cTags.tag, name));
            }
        }
        public static class Biomes {
            public static final TagKey<Biome> NULL = createTag("null");
            public static final TagKey<Biome> IS_STONE = createTag("is_stone");

            private static TagKey<Biome> createTag(String name) {
                return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(cTags.tag, name));
            }
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> IS_DARK = createTag("is_dark");
        public static final TagKey<Biome> IS_KNOWN = createTag("is_known");

        private static TagKey<Biome> createTag(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
        }
    }

    public static class Entities{
        public static final TagKey<EntityType<?>> PLANT = createTag("plant");
        public static final TagKey<EntityType<?>> DRAGON = createTag("dragon");

        private static TagKey<EntityType<?>> createTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> TRANSFORMABLE_ITEMS = createTag("transformable_items");

        public static final TagKey<Item> FORGE_PHANTOM = createTag("forge_phantom");

        private static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
        }
    }


}
