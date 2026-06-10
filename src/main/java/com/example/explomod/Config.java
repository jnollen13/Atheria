package com.example.explomod;

import java.util.List;

import com.example.explomod.datagen.AtheriaDiemsions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    public static final ModConfigSpec.BooleanValue BUTTON = BUILDER
            .comment("a button for testing experimental items & block (intended for dev use only)")
            .worldRestart()
            .define("buttonthing", false);

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // a list of strings that are treated as resource locations for items
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("explomod:table"), () -> "", Config::validateItemName);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    public static class Server {
        public final ModConfigSpec.ConfigValue<String> portal_destination_dimension_ID;
        public final ModConfigSpec.ConfigValue<String> portal_return_dimension_ID;

        public Server(ModConfigSpec.Builder builder) {
            builder.push("Modpack");
            portal_destination_dimension_ID = builder
                    .comment("Sets the ID of the dimension that the Aether Portal will send the player to")
                    .translation("config.aether.server.modpack.portal_destination_dimension_ID")
                    .define("Sets portal destination dimension", AtheriaDiemsions.AETHER_LEVEL.location().toString());
            portal_return_dimension_ID = builder
                    .comment("Sets the ID of the dimension that the Aether Portal will return the player to")
                    .translation("config.aether.server.modpack.portal_return_dimension_ID")
                    .define("Sets portal return dimension", Level.OVERWORLD.location().toString());
            builder.pop();
        }
    }

    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;

    static {
        final Pair<Server, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
    }
}
