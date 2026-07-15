package com.example.explomod.network;

import com.example.explomod.ExploMod;
import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.effect.ModEffects;
import com.example.explomod.packets.GrowthPacket;
import com.example.explomod.packets.DashPacket;
import com.example.explomod.packets.FireballPacket;
import com.example.explomod.packets.SpellPacket;
import com.example.explomod.payloads.SyncManaPayload;
import com.example.explomod.registries.Spell;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ExploMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModNetWorking {

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ExploMod.MODID).versioned("1.0.0");

        // fireball
        registrar.playToServer(
                FireballPacket.TYPE,
                FireballPacket.CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) {
                        Vec3 lookDirection = player.getLookAngle();
                        if(player.getData(AtheriaDataAttachments.MANA).mana()>=5) {
                            SmallFireball fireball = new SmallFireball(
                                    player.level(),
                                    player,
                                    new Vec3(
                                            lookDirection.x,
                                            lookDirection.y,
                                            lookDirection.z)
                            );
                            fireball.setPos(player.getX() + lookDirection.x, player.getEyeY() + lookDirection.y, player.getZ() + lookDirection.z);
                            player.level().addFreshEntity(fireball);
                            player.setData(AtheriaDataAttachments.MANA, new Mana(player.getData(AtheriaDataAttachments.MANA).mana()-10));
                            fireball.addTag("ability");
                            player.awardStat(AtheriaStats.SPELLS_CAST.get(), 1);
                            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                                    player,
                                    new SyncManaPayload(player.getData(AtheriaDataAttachments.MANA).mana() - 10)
                            );
                        }else{
                            assert Minecraft.getInstance().player != null;
                            Minecraft.getInstance().player.displayClientMessage(Component.translatable("magic.explomod.nem.fireball"), true);
                        }
                        applyCooldown(player, 30);
                    }
                })
        );

        // dash
        registrar.playToServer(
                DashPacket.TYPE,
                DashPacket.CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) {
                        if(player.getData(AtheriaDataAttachments.MANA).mana()>=-12.36) {
                            Vec3 lookDirection = player.getLookAngle();
                            double speedMultiplier = 3.5;
                            if (player.onGround()) {
                                player.setDeltaMovement(lookDirection.x * speedMultiplier, 0, lookDirection.z * speedMultiplier);
                                player.hurtMarked = true;
                                applyCooldown(player, 25);
                                player.setData(AtheriaDataAttachments.MANA, new Mana(player.getData(AtheriaDataAttachments.MANA).mana()-1));
                                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                                        player,
                                        new SyncManaPayload(player.getData(AtheriaDataAttachments.MANA).mana() - 1)
                                );
                            }
                        }else{
                            assert Minecraft.getInstance().player != null;
                            Minecraft.getInstance().player.displayClientMessage(Component.translatable("magic.explomod.nem.dash"), true);
                            applyCooldown(player, 10);
                        }
                    }
                })
        );
        //airburst
        registrar.playToServer(
                GrowthPacket.TYPE,
                GrowthPacket.CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player) {
                        if(player.getData(AtheriaDataAttachments.MANA).mana()>=-0.41) {
                            BlockPos target = player.blockPosition();
                            Level level = player.level();
                            if(isAir(level,target)&&isAir(level,target.north())&&isAir(level,target.south())&&isAir(level,target.east())&&isAir(level,target.west())) {
                                level.setBlock(target, Blocks.SHORT_GRASS.defaultBlockState(), 2);
                                level.setBlock(target.east(), Blocks.SHORT_GRASS.defaultBlockState(), 2);
                                level.setBlock(target.west(), Blocks.SHORT_GRASS.defaultBlockState(), 2);
                                level.setBlock(target.south(), Blocks.SHORT_GRASS.defaultBlockState(), 2);
                                level.setBlock(target.north(), Blocks.SHORT_GRASS.defaultBlockState(), 2);
                            }
                            applyCooldown(player, 32);
                            player.setData(AtheriaDataAttachments.MANA, new Mana(player.getData(AtheriaDataAttachments.MANA).mana()-3));
                            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                                    player,
                                    new SyncManaPayload(player.getData(AtheriaDataAttachments.MANA).mana() - 3)
                            );
                        }else{
                            assert Minecraft.getInstance().player != null;
                            Minecraft.getInstance().player.displayClientMessage(Component.translatable("magic.explomod.nem.growth"), true);
                            applyCooldown(player, 15);
                        }
                    }
                })
        );

        registrar.playToServer(
                SpellPacket.TYPE,
                SpellPacket.CODEC,
                (spellPacket, iPayloadContext) -> {
                    if(iPayloadContext.player() instanceof ServerPlayer player) {
                        String sId = spellPacket.spell();
                        Spell.spell(sId).DoSpell(player.level(), player);
                    }
                }
        );

        registrar.playToClient(
                ClientboundSyncKnowledgePacket.TYPE,
                ClientboundSyncKnowledgePacket.STREAM_CODEC,
                ClientboundSyncKnowledgePacket::handle
        );
    }


    public static void applyCooldown(ServerPlayer player, int duration){
        player.addEffect(new MobEffectInstance(ModEffects.COOLDOWN, duration, 0, false, false, true));
    }
    public static boolean isAir(Level level, BlockPos pos){
        return level.getBlockState(pos) == Blocks.AIR.defaultBlockState();
    }

    @SubscribeEvent
    public static void registerNetworking(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ExploMod.MODID);
        registrar.playToClient(
                SyncManaPayload.TYPE,
                SyncManaPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    var player = Minecraft.getInstance().player;
                    if (player != null) {
                        player.setData(AtheriaDataAttachments.MANA.get(), new Mana(payload.mana()));
                    }
                })
        );
    }
}
