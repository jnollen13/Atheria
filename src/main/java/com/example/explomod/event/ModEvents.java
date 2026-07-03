package com.example.explomod.event;

import com.example.explomod.AtheriaKeys;
import com.example.explomod.ExploMod;
import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.effect.ModEffects;
import com.example.explomod.item.alchemy.ModPotions;
import com.example.explomod.packets.DashPacket;
import com.example.explomod.packets.FireballPacket;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = ExploMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.LEAPING, ExploMod.FERMENTED_GLOWSTONE.asItem(), ModPotions.ROCKET_POTION);
        builder.addMix(Potions.AWKWARD, Items.STRING, ModPotions.SPIDER_POTION);
        builder.addMix(Potions.AWKWARD, ExploMod.FERMENTED_GLOWSTONE.asItem(), ModPotions.SHORT_POTION);
        builder.addMix(Potions.AWKWARD, Items.CREEPER_HEAD, ModPotions.CREEPER_POTION);
        builder.addMix(ModPotions.MANA_POTION, Items.GLOWSTONE, ModPotions.MANA_4_POTION);
        builder.addMix(ModPotions.MANA_POTION, Items.GLOWSTONE_DUST, ModPotions.MANA_2_POTION);
        builder.addMix(ModPotions.MANA_2_POTION, Items.GLOWSTONE_DUST, ModPotions.MANA_3_POTION);
        builder.addMix(ModPotions.MANA_3_POTION, Items.GLOWSTONE_DUST, ModPotions.MANA_4_POTION);
    }


    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player != null && mc.screen == null) {
            // checks if the player has a cooldown
            Holder<MobEffect> c = ModEffects.COOLDOWN.getDelegate();
            if(!mc.player.hasEffect(c)) {
                // fireball
                if(mc.player.getStats().getValue(AtheriaStats.HIDDEN_STATS.get().get(Items.FIRE_CHARGE))>0) {
                    while (AtheriaKeys.CAST_FIREBALL.consumeClick()) {
                        PacketDistributor.sendToServer(new FireballPacket());
                    }
                }
                // dash
                if(mc.player.getStats().getValue(AtheriaStats.HIDDEN_STATS.get().get(Items.LEATHER_BOOTS))>0) {
                    while ((AtheriaKeys.DASH.consumeClick())) {
                        PacketDistributor.sendToServer(new DashPacket());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiPre(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && mc.level != null && !mc.options.hideGui) {
            if (mc.player.getData(AtheriaDataAttachments.MANA).oom(mc.player)) {
                var guiGraphics = event.getGuiGraphics();
                int width = guiGraphics.guiWidth();
                int height = guiGraphics.guiHeight();
                int alphaColor = 0x99000000;
                guiGraphics.fill(0, 0, width, height, alphaColor);
            }
        }
    }
}