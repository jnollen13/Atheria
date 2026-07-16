package com.example.explomod.event;

import com.example.explomod.ExploMod;
import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.payloads.SyncManaPayload;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = ExploMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class StatUpdateEvent {

    @SubscribeEvent
    public static void blueMana(net.neoforged.neoforge.event.ServerChatEvent event){
        String message = event.getMessage().toString();
        if(message.equals("Mana")){
            event.setMessage(Component.literal("Mana").withStyle(ChatFormatting.AQUA));
            event.getPlayer().sendSystemMessage(Component.literal("MANA").withStyle(ChatFormatting.BLUE), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (!player.level().isClientSide()) {
            executeServerLogic(player);

        } else {
            executeClientLogic(player);
        }
        if(!player.level().isClientSide){
            if (player instanceof ServerPlayer serverPlayer) {
                Mana mana = serverPlayer.getData(AtheriaDataAttachments.MANA);
                if(mana.mana()<0){
                    player.displayClientMessage(Component.literal("OOM"), true);
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 3));
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1, 2));
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1, 1, false, true));
                    serverPlayer.addEffect(new MobEffectInstance(MobEffects.HUNGER, 1, 0, true, false, false));
                } else if (mana.mana()>101.2f) {
                    player.displayClientMessage(Component.translatable("message.explomod.over"), true);
                    serverPlayer.setData(AtheriaDataAttachments.MANA, new Mana(100f));
                }
            }
        }
        if(!player.level().isClientSide){
            if (player instanceof ServerPlayer serverPlayer) {
            Mana stats = serverPlayer.getData(AtheriaDataAttachments.MANA.get());
            if(stats.mana()<100.0f) {
                serverPlayer.setData(AtheriaDataAttachments.MANA, new Mana(stats.mana() + (0.1f/50)));
            }
                net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                        serverPlayer,
                        new SyncManaPayload(stats.mana() + (0.1f/50))
                );
            }
        }
    }

    /*
    @SubscribeEvent
    public static void onMobTick(EntityTickEvent.Post event){
        Entity entity = event.getEntity();
        if(entity instanceof LivingEntity mob){
            Mana mana = mob.getData(AtheriaDataAttachments.MANA);
            if (!(mob instanceof Player)) {
                if (mana.mana() < 0) {
                    mob.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 1, 3));
                    mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 1, 2));
                    mob.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1, 1, false, true));
                    mob.addEffect(new MobEffectInstance(MobEffects.HUNGER, 1, 0, true, false, false));
                } else if (mana.mana() > 101.2f) {
                    mob.setData(AtheriaDataAttachments.MANA, new Mana(100f));
                }
                if (mana.mana() < 100.0f) {
                    mob.setData(AtheriaDataAttachments.MANA, new Mana(mana.mana() + (0.1f / 50)));
                }
            }
        }
    }
    */

    private static void executeServerLogic(Player p) {
        Item item = Items.CRAFTING_TABLE;
        LocalPlayer player = Minecraft.getInstance().player;
        if(player!=null) {
            int j = player.getStats().getValue(AtheriaStats.HIDDEN_STATS.get().get(item));

            player.getStats().setValue(p, getHiddenStats(item), j);
        }
    }

    private static void executeClientLogic(Player player) {
        if(player.isHolding(Items.CRAFTING_TABLE)) {
            if(Minecraft.getInstance().player!=null) {

            }
        }
    }

    public static Stat<?> getHiddenStats(Item i){
        return AtheriaStats.HIDDEN_STATS.get().get(i);
    }
}
