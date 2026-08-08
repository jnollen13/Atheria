package com.example.explomod.command;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.data.MaxMana;
import com.example.explomod.payloads.SyncManaPayload;
import com.example.explomod.payloads.SyncMaxManaPayload;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
        dispatcher.register(Commands.literal("stat").requires((p_139171) -> p_139171.hasPermission(1)).then(Commands.argument("targets", EntityArgument.entities())
                .then(Commands.argument("stats", ResourceArgument.resource(context, Registries.CUSTOM_STAT))
                        .executes(p_270840 -> getStat(
                                p_270840.getSource(),
                                EntityArgument.getEntities(p_270840, "targets"),
                                ResourceArgument.getResource(p_270840, "stats", Registries.CUSTOM_STAT)
                        )))));
        dispatcher.register((Commands.literal("mana")
                .requires((p_139171) -> p_139171.hasPermission(2)))
                .then(Commands.argument("targets", EntityArgument.entities())
                    .then(Commands.literal("set")
                        .then(Commands.literal("max")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(0.01f, 1494.1296f))
                                        .executes(p_270841 -> setMana(p_270841.getSource(), true,
                                                EntityArgument.getEntities(p_270841, "targets"),
                                                FloatArgumentType.getFloat(p_270841, "amount")))))
                        .then(Commands.literal("current")
                                .then(Commands.argument("amount", FloatArgumentType.floatArg(-113.12481632f, 1000.248163264128f))
                                        .executes(p_270841 -> setMana(p_270841.getSource(), false,
                                                EntityArgument.getEntities(p_270841, "targets"),
                                                FloatArgumentType.getFloat(p_270841, "amount"))))))
                    .then(Commands.literal("get")
                            .then(Commands.literal("current")
                                    .executes(stack -> getMana(stack.getSource(), false, EntityArgument.getEntities(stack, "targets"))))
                            .then(Commands.literal("max")
                                    .executes(stack -> getMana(stack.getSource(), true, EntityArgument.getEntities(stack, "targets"))))
                    )));
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

    private static int getStat(CommandSourceStack sourceStack, Collection<? extends Entity> entities, Holder.Reference<ResourceLocation> reference){
        entities.forEach(entity -> {
            if(entity instanceof LocalPlayer player){
                player.getStats();
            }
        });
        return 1;
    }

    private static int setMana(CommandSourceStack sourceStack, boolean max, Collection<? extends Entity> entities, float newMana){
        entities.forEach(entity -> {
            boolean living = true;
            if(entity instanceof LivingEntity livingEntity){
                if(max){
                    livingEntity.setData(AtheriaDataAttachments.MAX_MANA, new MaxMana(newMana));
                    if(livingEntity instanceof ServerPlayer serverPlayer){
                        PacketDistributor.sendToPlayer(serverPlayer, new SyncMaxManaPayload(newMana));
                    }
                }else{
                    livingEntity.setData(AtheriaDataAttachments.MANA, new Mana(newMana));
                    if(livingEntity instanceof ServerPlayer serverPlayer){
                        PacketDistributor.sendToPlayer(serverPlayer, new SyncManaPayload(newMana));
                    }
                }
            }else{
                living = false;
            }
            if(sourceStack.getEntity() instanceof ServerPlayer source){
                if(living){
                    if(max){
                        source.sendSystemMessage(Component.translatable("commands.explomod.mana.sucsess.max.set", entity.getName(), newMana));
                    }else{
                        source.sendSystemMessage(Component.translatable("commands.explomod.mana.sucsess.current.set", entity.getName(), newMana));
                    }
                }else {
                    source.sendSystemMessage(Component.translatable("commands.explomod.mana.fail", entity.getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
            }
        });
        return 1;
    }

    private static int getMana(CommandSourceStack sourceStack, boolean max, Collection<? extends Entity> entities){
        AtomicInteger livingMobs = new AtomicInteger();
        AtomicInteger nonLivingMobs= new AtomicInteger();
        AtomicInteger totalTargets= new AtomicInteger();
        entities.forEach(entity -> {
            float num = 0;
            if(entity instanceof LivingEntity livingEntity){
                if(max){
                    num = livingEntity.getData(AtheriaDataAttachments.MAX_MANA).maxMana();
                    if(sourceStack.getEntity() instanceof ServerPlayer source){
                        //source.sendSystemMessage(((MutableComponent) livingEntity.getName()).append(Component.translatable("max.get")).append(Component.literal(num + "")));
                        source.sendSystemMessage(Component.translatable("commands.explomod.mana.sucsess.max.get", livingEntity.getName(), livingEntity.getData(AtheriaDataAttachments.MAX_MANA).maxMana()));
                    }
                }else{
                    num = livingEntity.getData(AtheriaDataAttachments.MANA).mana();
                    if(sourceStack.getEntity() instanceof ServerPlayer source){
                        source.sendSystemMessage(Component.translatable("commands.explomod.mana.sucsess.current.get", livingEntity.getName(), livingEntity.getData(AtheriaDataAttachments.MANA).mana()));
                    }
                }
                livingMobs.addAndGet(1);
            }else{
                if(sourceStack.getEntity() instanceof ServerPlayer source){
                    source.sendSystemMessage(Component.translatable("commands.explomod.mana.fail", entity.getName()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                }
                nonLivingMobs.addAndGet(1);
            }
            totalTargets.addAndGet(1);
        });
        if(sourceStack.getEntity() instanceof ServerPlayer source){
            source.sendSystemMessage(Component.literal(livingMobs+"").append(Component.translatable("living.targets")).withStyle(ChatFormatting.GREEN));
            source.sendSystemMessage(Component.literal(nonLivingMobs+"").append(Component.translatable("non.living.targets")).withStyle(ChatFormatting.GREEN));
            source.sendSystemMessage(Component.literal(totalTargets+"").append(Component.translatable("total.targets")).withStyle(ChatFormatting.GREEN));
        }
        return 1;
    }
}
