package com.example.explomod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

import java.util.*;

public class TestCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((Commands.literal("kill")
                .requires((p_139171_) -> p_139171_.hasPermission(2)))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.argument("damageType", ResourceArgument.resource(context, Registries.DAMAGE_TYPE))
                                .executes(
                                        p_270840_ -> kill(
                                                p_270840_.getSource(),
                                                EntityArgument.getEntities(p_270840_, "targets"),
                                                new DamageSource(ResourceArgument.getResource(p_270840_, "damageType", Registries.DAMAGE_TYPE))
                                        )
                                )
                        )
                )
        );
    }

    private static int kill(CommandSourceStack source, Collection<? extends Entity> targets, DamageSource damageType) {
        for(Entity entity : targets) {
            entity.hurt(damageType, 9648548725745349348563849.0f);
        }
        if (targets.size() == 1) {
            source.sendSuccess(() -> Component.translatable("commands.kill.success.single", new Object[]{((Entity)targets.iterator().next()).getDisplayName()}), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.kill.success.multiple", new Object[]{targets.size()}), true);
        }

        return targets.size();
    }

}
