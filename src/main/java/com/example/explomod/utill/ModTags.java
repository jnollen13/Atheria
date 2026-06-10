package utill;

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
        public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = createTag("incorrect_for_copper_tool");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
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
