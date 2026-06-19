package com.example.explomod.command;

import com.example.explomod.Config;
import com.example.explomod.ExploMod;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class RandCommandWeb {
    public static <T> LiteralArgumentBuilder<T> create(
            Function<String, LiteralArgumentBuilder<T>> literalFactory,
            ArgumentFactory<T> argumentFactory,
            BiConsumer<T, Component> sendSuccess,
            BiConsumer<T, Component> sendFailure) {
        if (Config.CLIENT.use_client_commands.get()) {
            return literalFactory.apply("atheriaClient")
                    .then(literalFactory.apply("crash").executes(context -> {
                        if(Config.SERVER.commandCrash_enabled.get()) {
                            Minecraft.getInstance().emergencySaveAndCrash(CrashReport.forThrowable(new Throwable("forced crash"), "Auto_crashed"));
                        }else{
                            serverDisabled();
                        }
                        return 1;
                    }))
                    .then(literalFactory.apply("command_history").executes(context -> {
                        if(Config.SERVER.commandHistory_enabled.get()) {
                            String history = Minecraft.getInstance().commandHistory().history().toString();
                            assert Minecraft.getInstance().player != null;
                            Minecraft.getInstance().player.sendSystemMessage(Component.literal("command history: " + history));
                        }else{
                            serverDisabled();
                        }
                        return 1;
                    }));
        }else{
            return literalFactory.apply(ExploMod.MODID+"*cmd").executes(context -> {
                assert Minecraft.getInstance().player != null;
                Minecraft.getInstance().player.sendSystemMessage(Component.translatable("explomod.configuration.commands.off"));
                        return 1;
            });
        }
        }

        public static void serverDisabled(){
            assert Minecraft.getInstance().player != null;
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("this command is disabled by the server."));
        }
}
