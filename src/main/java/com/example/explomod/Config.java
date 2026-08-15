package com.example.explomod;

import java.util.List;

import com.example.explomod.datagen.AtheriaDiemsions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

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
        public final ModConfigSpec.ConfigValue<Boolean> fallVoidDark;
        public final ModConfigSpec.ConfigValue<String> fallDarkDimension;
        public final ModConfigSpec.ConfigValue<Boolean> despawn_cave_lizards;
        public final ModConfigSpec.ConfigValue<Integer> despawn_cave_lizards_ticks;
        public final ModConfigSpec.ConfigValue<Boolean> cave_wizards_cast_spells;
        public final ModConfigSpec.ConfigValue<Integer> lightning_cube_max_targets;


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
            fallVoidDark = builder
                    .comment("should anything that falls into the void in the dark fall into a new dimension")
                    .translation("dark_fall")
                    .define("entities change dimensions in dark void", false);
            fallDarkDimension = builder
                    .comment("Sets the ID of the dimension that falling out of the dark will bring mobs to")
                    .translation("dark_fall_dimension")
                    .define("Sets the dimension that you fall into from the dark", Level.NETHER.location().toString());
            builder.pop();
            builder.push("villagers");
            builder.pop();
            builder.push("spells");
            cave_wizards_cast_spells = builder
                    .comment("can cave wizards cast spells")
                    .translation("caveWizardSpells")
                    .define("caveWizardSpells", true);
            lightning_cube_max_targets = builder
                    .comment("maxium amount of targets that can be hit with lightning cube")
                    .translation("lightningCubeMaxTargets")
                    .define("lightningCubeMaxTargets", 143);
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
            builder.push("performance");
            despawn_cave_lizards = builder
                    .comment("despawns cave lizards after the specified amount of time")
                    .translation("despawn_cave_lizards")
                    .define("despawn cave lizards", false);

            despawn_cave_lizards_ticks = builder
                    .comment("sets the amount of time that it takes for cave lizards to despawn")
                    .translation("despawn_cave_lizards_time")
                    .define("despawn cave lizards after ticks", 1200);
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
