package com.example.explomod.item.custom;

import com.example.explomod.ExploMod;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;
import com.example.explomod.utill.ModTags;

public class ModToolTiers {
    public static final Tier PHANTOM = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_PHANTOM_TOOL,
            600, 4.0f, 0f, 22, () -> Ingredient.of(ExploMod.PHANTOM_INGOT));
    public static final Tier COPPER = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL,
            300, 5.0f, 2.5f, 15, () -> Ingredient.of(Items.COPPER_INGOT));
    public static final Tier RAPIER = new SimpleTier(ModTags.Blocks.INCORRECT_FOR_COPPER_TOOL,
            400, 4.0f, 3.5f, 18, () -> Ingredient.of(Items.IRON_INGOT));
}
