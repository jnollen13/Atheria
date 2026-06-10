package com.example.explomod.datagen;

import com.example.explomod.ExploMod;
import com.example.explomod.loot.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ExploMod.MODID);
    }

    @Override
    protected void start() {
        this.add("nice_stick_to_deepslate_diamond_ore",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BIRCH_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()}, ExploMod.NICE_STICK.get()));

        this.add("longsword_from_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()}, ExploMod.NEW_SWORD.get()));

        this.add("popsicle_from_stray",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/stray")).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()}, ExploMod.POPSICLE_FOOD.get()));

        this.add("fuel_from_dungeon_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/dungeon")).build(),
                        LootItemRandomChanceCondition.randomChance(0.85f).build()}, ExploMod.FUEL.get()));

        this.add("fuel_from_village_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/village/village_toolsmith")).build(),
                        LootItemRandomChanceCondition.randomChance(0.85f).build()}, ExploMod.FUEL.get()));

            this.add("stool_from_enderman",
                             new AddItemModifier(new LootItemCondition[] {
        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/enderman")).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build()}, ExploMod.STOOL_ITEM.get()));
    }
}
