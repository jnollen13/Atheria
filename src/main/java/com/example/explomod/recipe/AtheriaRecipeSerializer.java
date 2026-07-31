package com.example.explomod.recipe;

import com.example.explomod.ExploMod;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface AtheriaRecipeSerializer<T extends Recipe<?>> {
    DeferredRegister<RecipeSerializer<?>> RECIPES =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, ExploMod.MODID);

    Holder<RecipeSerializer<?>> KINGS_CHALICE = RECIPES.register("crafting_special_kings_chalice", () -> new SimpleCraftingRecipeSerializer<>(KingsChaliceRecipe::new));


    static void register(IEventBus eventBus) {
        RECIPES.register(eventBus);
    }
}
