package com.example.explomod.event;

import com.example.explomod.ExploMod;
import com.example.explomod.item.alchemy.ModPotions;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = ExploMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void onBrewingRecipeRegister(RegisterBrewingRecipesEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();
        builder.addMix(Potions.LEAPING, ExploMod.FERMENTED_GLOWSTONE.asItem(), ModPotions.ROCKET_POTION);
        builder.addMix(Potions.AWKWARD, Items.STRING, ModPotions.SPIDER_POTION);
        builder.addMix(Potions.AWKWARD, ExploMod.FERMENTED_GLOWSTONE.asItem(), ModPotions.SHORT_POTION);
        builder.addMix(Potions.AWKWARD, Items.CREEPER_HEAD, ModPotions.CREEPER_POTION);
    }
}