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
            .push("test_configs")
            .define("logDirtBlock", true);

    public static final ModConfigSpec.BooleanValue BUTTON = BUILDER
            .comment("a button for testing experimental items & block (intended for dev use only)")
            .worldRestart()
            .define("buttonthing", false);

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
        public final ModConfigSpec.ConfigValue<Boolean> commandHistory_enabled;
        public final ModConfigSpec.ConfigValue<Boolean> commandCrash_enabled;


        public Server(ModConfigSpec.Builder builder) {
            builder.push("modpack");
            portal_destination_dimension_ID = builder
                    .comment("Sets the ID of the dimension that the Dark Portal will send the player to")
                    .translation("dark_portal_destination")
                    .define("Sets portal destination dimension", AtheriaDiemsions.AETHER_LEVEL.location().toString());
            portal_return_dimension_ID = builder
                    .comment("Sets the ID of the dimension that the Dark Portal will return the player to")
                    .translation("dark_portal_return_dimension")
                    .define("Sets portal return dimension", Level.OVERWORLD.location().toString());
            builder.pop();
            builder.push("villagers");
            builder.pop();
            builder.push("permissions");
            commandHistory_enabled = builder
                    .comment("if players are allowed to use the command history command")
                    .translation("isCommandHistoryAllowed")
                    .define("isCommandHistoryAllowed", true);
            commandCrash_enabled = builder
                    .comment("if players are allowed to use the crash command")
                    .translation("isCommandCrashAllowed")
                    .define("isCommandCrashAllowed", false);
            builder.pop();
        }
    }

    public static class Client {
        public final ModConfigSpec.ConfigValue<Boolean> use_client_commands;

        public Client(ModConfigSpec.Builder builder) {
            builder.push("commands");
            use_client_commands = builder
                    .comment("should register commands")
                    .translation("should_use_commands")
                    .worldRestart()
                    .define("client commands", false);
            builder.pop();
        }
    }

    public static final ModConfigSpec SERVER_SPEC;
    public static final Server SERVER;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        final Pair<Server, ModConfigSpec> serverSpecPair = new ModConfigSpec.Builder().configure(Server::new);
        SERVER_SPEC = serverSpecPair.getRight();
        SERVER = serverSpecPair.getLeft();
        final Pair<Client, ModConfigSpec> clientSpecPair = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }
}
