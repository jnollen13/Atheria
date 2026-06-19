package com.example.explomod.utill;

import com.example.explomod.command.RandCommandWeb;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

public class ClientProxy {

    public static void registerCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(RandCommandWeb.create(
                Commands::literal,
                Commands::argument,
                (source, component) -> source.sendSuccess(() -> component, true),
                CommandSourceStack::sendFailure));
    }

}
