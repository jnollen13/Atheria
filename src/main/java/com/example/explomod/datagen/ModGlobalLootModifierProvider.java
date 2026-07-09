package com.example.explomod.datagen;

import com.example.explomod.ExploMod;
import com.example.explomod.loot.AddItemModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ExploMod.MODID);
    }

    @Override
    protected void start() {
        this.add("nice_stick_from_birch",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.BIRCH_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_oak",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.OAK_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_dark_oak",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DARK_OAK_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_jungle",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.JUNGLE_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_mangrove",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.MANGROVE_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.12f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_acacia",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ACACIA_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.22f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_other_leaves1",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.AZALEA_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_other_leaves2",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.CHERRY_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_other_leaves3",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.FLOWERING_AZALEA_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()}, ExploMod.NICE_STICK.get()));

        this.add("nice_stick_from_other_leaves4",
                new AddItemModifier(new LootItemCondition[] {
                        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.MANGROVE_LEAVES).build(),
                        LootItemRandomChanceCondition.randomChance(0.075f).build()}, ExploMod.NICE_STICK.get()));

        this.add("longsword_from_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.05f).build()}, ExploMod.NEW_SWORD.get()));

        this.add("jungle_dash",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.25f).build()}, ExploMod.DASH_SWORD.get()));

        this.add("melon_juice_plains",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/village/village_plains_house")).build(),
                        LootItemRandomChanceCondition.randomChance(0.2f).build()}, ExploMod.MELON_JUICE.get()));

        this.add("fermented_melon_juice_jungle_temple",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/jungle_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0274f).build()}, ExploMod.FERMENTED_MELON_JUICE.get()));

        this.add("dash_sword_trial_chamber",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("equipment/trial_chamber_melee")).build(),
                        LootItemRandomChanceCondition.randomChance(0.047362f).build()}, ExploMod.DASH_SWORD.get()));

        this.add("dark_portal_creator_mansion",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/woodland_mansion")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0008374f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("dark_portal_creator_end",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/end_city_treasure")).build(),
                        LootItemRandomChanceCondition.randomChance(0.00132794f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("dark_portal_creator_ancient_city",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/ancient_city")).build(),
                        LootItemRandomChanceCondition.randomChance(0.061995373194f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("dark_portal_creator_spawn_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/spawn_bonus_chest")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0000000018f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("dark_portal_creator_dagger_temple",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.fromNamespaceAndPath("daggermod","chests/dagger_temple")).build(),
                        LootItemRandomChanceCondition.randomChance(0.000004018f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("dark_portal_creator_igloo",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/igloo_chest")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0000108f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("dark_portal_creator_simple_dungeon",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0049163822f).build()}, ExploMod.DARK_PORTAL_CREATOR.get()));

        this.add("popsicle_snowy_village",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/village/village_snowy_house")).build(),
                        LootItemRandomChanceCondition.randomChance(0.327926f).build()}, ExploMod.POPSICLE_FOOD.get()));

        this.add("popsicle_from_stray",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/stray")).build(),
                        LootItemRandomChanceCondition.randomChance(0.15f).build()}, ExploMod.POPSICLE_FOOD.get()));

        this.add("fuel_from_dungeon_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/simple_dungeon")).build(),
                        LootItemRandomChanceCondition.randomChance(0.85f).build()}, ExploMod.FUEL.get()));

        this.add("fuel_from_village_chest",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/village/village_toolsmith")).build(),
                        LootItemRandomChanceCondition.randomChance(0.85f).build()}, ExploMod.FUEL.get()));

        this.add("phantom_sword_toolsmith",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("chests/village/village_toolsmith")).build(),
                        LootItemRandomChanceCondition.randomChance(0.01f).build()}, ExploMod.PHANTOM_SWORD.get()));

            this.add("stool_from_enderman",
                             new AddItemModifier(new LootItemCondition[] {
        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("entities/enderman")).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build()}, ExploMod.STOOL_ITEM.get()));

        this.add("harbinger_ranged_trial_chamber",
                new AddItemModifier(new LootItemCondition[] {
                        new LootTableIdCondition.Builder(ResourceLocation.withDefaultNamespace("equipment/trial_chamber_ranged")).build(),
                        LootItemRandomChanceCondition.randomChance(0.0075f).build()}, ExploMod.HARBINGER.get()));
    }
}
