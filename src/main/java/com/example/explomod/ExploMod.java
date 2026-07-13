package com.example.explomod;

import com.example.explomod.block.custom.*;
import com.example.explomod.block.custom.crates.CrateBlock;
import com.example.explomod.block.custom.crates.EggBasketBlock;
import com.example.explomod.block.custom.crates.SugarCaneCrateBlock;
import com.example.explomod.block.custom.crates.template.CrateData;
import com.example.explomod.block.custom.crates.template.CrateList;
import com.example.explomod.command.AtheriaCommands;
import com.example.explomod.component.AtheriaDataComponents;
import com.example.explomod.component.SavedSpells;
import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.effect.ModEffects;
import com.example.explomod.entity.ModEntities;
import com.example.explomod.entity.client.*;
import com.example.explomod.loot.ModLootModifiers;
import com.example.explomod.particle.ModParticles;
import com.example.explomod.particle.SafteyParticle;
import com.example.explomod.registries.Spell;
import com.example.explomod.registries.SpellRegistries;
import com.example.explomod.stats.AtheriaStats;
import com.example.explomod.triggers.AtheriaTriggers;
import com.example.explomod.utill.ClientProxy;
import com.example.explomod.worldgen.ModBiomes;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.example.explomod.item.alchemy.ModPotions;
import com.example.explomod.item.custom.*;
import com.example.explomod.item.custom.Throwable;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.example.explomod.sound.ModSounds;
import com.example.explomod.villager.ModVillagers;
import com.example.explomod.utill.ModItemProperties;
import com.example.explomod.worldgen.feature.ModFeature;
import com.example.explomod.worldgen.tree.ModTreeGrower;

import java.util.List;

import static net.minecraft.world.item.Items.POPPY;
import static net.minecraft.world.item.Items.registerBlock;
import static net.neoforged.fml.loading.FMLEnvironment.dist;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(ExploMod.MODID)
public class ExploMod {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "explomod";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "explomod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "explomod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "explomod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    //registries(item and block)
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).destroyTime(35f).explosionResistance(999f).friction(0.85f).requiresCorrectToolForDrops().sound(SoundType.STONE));
    //remember () -> new DropExperienceBlock(UniformInt.of(2, 4),
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredBlock<Block> LOG_BLOCK = BLOCKS.registerSimpleBlock("log", BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).jumpFactor(2f).explosionResistance(5f).destroyTime(1).sound(SoundType.WOOD).pushReaction(PushReaction.PUSH_ONLY));
    public static final DeferredItem<BlockItem> LOG_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("log", LOG_BLOCK);
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(5).saturationModifier(8f).effect(() -> new MobEffectInstance(MobEffects.POISON, 500), 0.456789f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 424, 9), 0.756f).build()));
    public static final DeferredItem<Item> WEAPON_ITEM = ITEMS.registerSimpleItem("weapon", new Item.Properties().stacksTo(1).durability(200).attributes(SwordItem.createAttributes(Tiers.STONE, 4, -3.2F)));
    public static final DeferredItem<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new PickaxeItem(ModToolTiers.COPPER, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.COPPER, 1.0F, -2.8F))));
    public static final DeferredBlock<Block> XRAY_BLOCK = BLOCKS.registerSimpleBlock("xrayer", BlockBehaviour.Properties.of().instabreak().speedFactor(0.9f).noOcclusion());
    public static final DeferredItem<BlockItem> XRAY_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("xrayer", XRAY_BLOCK);
    public static final DeferredBlock<Block> REDTRAV = BLOCKS.registerSimpleBlock("redtrav", BlockBehaviour.Properties.of().sound(SoundType.WET_GRASS).instabreak().jumpFactor(2f).ignitedByLava().speedFactor(2.5f));
    public static final DeferredItem<BlockItem> REDTRAV_ITEM = ITEMS.registerSimpleBlockItem("redtrav", REDTRAV);
    public static final DeferredItem<Item> NICE_STICK = ITEMS.register("nice_stick", () -> new NiceStick(new Item.Properties().setNoRepair().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0f).usingConvertsTo(Items.STICK).build())));
    public static final DeferredItem<Item> HEART_FOOD = ITEMS.registerSimpleItem("food", new Item.Properties().stacksTo(16).fireResistant().food(new FoodProperties.Builder()
            .nutrition(19).fast().effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 324), 0.5f).saturationModifier(20f).usingConvertsTo(Items.BONE).alwaysEdible().build()));
    public static final DeferredItem<Item> NEW = ITEMS.registerSimpleItem("placeholder", new Item.Properties().stacksTo(5));
    public static final DeferredItem<Item> LEGENDS_FROST_MUSIC_DISC = ITEMS.registerSimpleItem("lfrost_music_disc", new Item.Properties().jukeboxPlayable(JukeboxSongs.CAT).stacksTo(1).rarity(Rarity.RARE));
    public static final DeferredItem<Item> POPSICLE_FOOD = ITEMS.registerSimpleItem("ice_food",
            new Item.Properties().rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(0).saturationModifier(1f).usingConvertsTo(Items.STICK)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 224, 2), 0.9f)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 204), 0.71f)
                    .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 163), 0.51f)
                    .effect(() -> new MobEffectInstance(MobEffects.WIND_CHARGED, 6149, 9), 0.1f).build()));
public static final DeferredItem<Item> YELLOW_POPSICLE = ITEMS.registerSimpleItem("ypopsicle", new Item.Properties().rarity(Rarity.UNCOMMON).food(new FoodProperties.Builder()
        .alwaysEdible().nutrition(2).saturationModifier(4f).usingConvertsTo(Items.STICK)
        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 224), 0.78f)
        .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 999), 0.96f)
        .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 568), 0.78f)
        .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 294), 0.8f)
        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 224), 0.9f)
        .effect(() -> new MobEffectInstance(MobEffects.GLOWING, 124), 0.32f)
        .effect(() -> new MobEffectInstance(MobEffects.JUMP, 224), 0.4f).build()));
    public static final DeferredItem<Item> BLUE_POPSICLE = ITEMS.registerSimpleItem("bpopsicle",
            new Item.Properties().rarity(Rarity.COMMON).food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).usingConvertsTo(Items.STICK)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 254), 0.81f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 104), 0.6f)
                    .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 163), 0.31f)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 163), 0.04f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 649), 0.13f)
                    .effect(() -> new MobEffectInstance(MobEffects.SLOW_FALLING, 9), 0.01f).build()));
    public static final DeferredItem<Item> GREEN_POPSICLE = ITEMS.registerSimpleItem("gpopsicle",
            new Item.Properties().rarity(Rarity.COMMON).food(new FoodProperties.Builder().alwaysEdible().nutrition(1).saturationModifier(2f).usingConvertsTo(Items.STICK)
                    .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 204, 1), 0.71f)
                    .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 64), 0.31f)
                    .effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 163), 0.11f)
                    .effect(() -> new MobEffectInstance(MobEffects.JUMP, 393), 0.84f)
                    .effect(() -> new MobEffectInstance(MobEffects.UNLUCK, 9193), 0.0001f)
                    .effect(() -> new MobEffectInstance(MobEffects.CONDUIT_POWER, 793), 0.001f)
                    .effect(() -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 149), 0.13f)
                    .effect(() -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 897), 0.001f).build()));
    public static final DeferredBlock<Block> TANK_BLOCK = BLOCKS.registerSimpleBlock("tank", BlockBehaviour.Properties.of().instabreak().noOcclusion().lightLevel(p_187433_ -> 11).pushReaction(PushReaction.NORMAL).noCollission());
    public static final DeferredItem<BlockItem> TANK_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("tank", TANK_BLOCK);
    public static final DeferredItem<Item> NEW_SWORD = ITEMS.registerSimpleItem(
            "new_sword", new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.IRON, 10, -3.6F)));
    public static final DeferredBlock<Block> STOOL = BLOCKS.register("stool", () -> new StoolBlock(
            BlockBehaviour.Properties.of().noOcclusion().mapColor(MapColor.WOOD).ignitedByLava().strength(6f)));
    public static final DeferredItem<BlockItem> STOOL_ITEM = ITEMS.registerSimpleBlockItem("stool", STOOL);
    public static final DeferredBlock<Block>  TABLE = BLOCKS.register("table", () -> new TableBlock(BlockBehaviour.Properties.of().ignitedByLava().noOcclusion().sound(SoundType.WOOD).strength(5f).explosionResistance(8f).pushReaction(PushReaction.PUSH_ONLY)));
    public static final DeferredItem<BlockItem> TABLE_ITEM = ITEMS.registerSimpleBlockItem("table", TABLE, new Item.Properties().stacksTo(32));
    public static final DeferredItem<Item> FUEL = ITEMS.register("fuel", () -> new Item(new Item.Properties().rarity(Rarity.COMMON).stacksTo(32)));
    public static final DeferredBlock<Block> NEW_LAMP = BLOCKS.register("lamp", () -> new NewLampBlock(Block.Properties.of().explosionResistance(2f).strength(10f).sound(ModSounds.MAGIC_BLOCK_SOUNDS)
                    .lightLevel(state -> state.getValue(NewLampBlock.CLICKED) ? 12 : 0)));
    public static final DeferredItem<BlockItem> LAMP_ITEM = ITEMS.registerSimpleBlockItem("lamp", NEW_LAMP);
    public static final DeferredBlock<Block> STONED = BLOCKS.registerSimpleBlock("stoned", BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(9f).sound(SoundType.GRAVEL).explosionResistance(1f)
            .jumpFactor(0.75f).pushReaction(PushReaction.NORMAL));
    public static final DeferredItem<BlockItem> STONED_ITEM = ITEMS.registerSimpleBlockItem("stoned", STONED);
    public static final DeferredBlock<Block> KIT = BLOCKS.registerSimpleBlock("kit", BlockBehaviour.Properties.of().sound(SoundType.WOOD).noOcclusion().strength(2f).pushReaction(PushReaction.PUSH_ONLY).speedFactor(0.8f).noTerrainParticles().mapColor(MapColor.WOOD));
    public static final DeferredItem<BlockItem> KIT_ITEM = ITEMS.registerSimpleBlockItem("kit", KIT);
    public static final DeferredBlock<Block> GROUND = BLOCKS.registerSimpleBlock("sground", BlockBehaviour.Properties.of().strength(13f).explosionResistance(32f).mapColor(MapColor.STONE).noOcclusion());
    public static final DeferredItem<BlockItem> GROUND_ITEM = ITEMS.registerSimpleBlockItem("sground", GROUND, new Item.Properties().rarity(Rarity.COMMON).stacksTo(99).setNoRepair());
    public static final DeferredBlock<Block> LAVA_HOLDER = BLOCKS.register("lavaholder", () -> new LavaHolderBlock(Block.Properties.of().noOcclusion().instabreak()));
    public static final DeferredItem<BlockItem> LAVA_ITEM = ITEMS.registerSimpleBlockItem("lavaholder", LAVA_HOLDER);
    public static final DeferredItem<Item> BOW = ITEMS.register("newbow", () -> new BowItem(new Item.Properties().durability(150)));
    public static final DeferredItem<Item> DAGGER = ITEMS.register("dagger", () -> new SwordItem(Tiers.GOLD, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 1, -2.1F)).durability(171)));
    public static final DeferredItem<Item> THROWABLE = ITEMS.register("throwable", () -> new ThrownItem(new Item.Properties().rarity(Rarity.EPIC).durability(250).attributes(TridentItem.createAttributes()).component(DataComponents.TOOL, TridentItem.createToolProperties())));
    public static final DeferredItem<Item> PHANTOM_HELM = ITEMS.register("phantom_helm", () -> new ArmorItem(ModArmorMaterials.PHANTOM, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(55))));
    public static final DeferredItem<Item> FERMENTED_GLOWSTONE = ITEMS.registerSimpleItem("fermented_glowstone", new Item.Properties().rarity(Rarity.COMMON).stacksTo(64).fireResistant());
    public static final DeferredItem<Item> PHANTOM_CHESTPLATE = ITEMS.register("phantom_chestplate", () -> new ArmorItem(ModArmorMaterials.PHANTOM, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(155))));
    public static final DeferredItem<Item> PHANTOM_BOOTS = ITEMS.register("phantom_boots", () -> new ArmorItem(ModArmorMaterials.PHANTOM, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(85))));
    public static final DeferredItem<Item> PHANTOM_LEGGINGS = ITEMS.register("phantom_leggings", () -> new ArmorItem(ModArmorMaterials.PHANTOM, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(115))));
    public static final DeferredItem<Item> PHANTOM_INGOT = ITEMS.register("phantom_ingot", () -> new Item(new Item.Properties().stacksTo(32).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> PHANTOM_SWORD = ITEMS.register("phantom_sword", () -> new SwordItem(ModToolTiers.PHANTOM, new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 2, -2.3F)).durability(175)));
    public static final DeferredItem<Item> PHANTOM_AXE = ITEMS.register("phantom_axe", () -> new AxeItem(ModToolTiers.PHANTOM, new Item.Properties().attributes(AxeItem.createAttributes(ModToolTiers.PHANTOM, 9.5F, -2.9F)).durability(100)));
    public static final DeferredItem<Item> PHANTOM_HOE = ITEMS.register("phantom_hoe", () -> new HoeItem(ModToolTiers.PHANTOM, new Item.Properties().attributes(HoeItem.createAttributes(ModToolTiers.PHANTOM, 2.5F, -3.2F))));
    public static final DeferredBlock<Block> PHANTOM_BLOCK = BLOCKS.register("phantom_block", () -> new PhantomBlock(BlockBehaviour.Properties.of().strength(15f).explosionResistance(5f).lightLevel(p_187433_ -> 6).noOcclusion().noTerrainParticles()));
    public static final DeferredItem<BlockItem> PHANTOM_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("phantom_block", PHANTOM_BLOCK);
    public static final DeferredBlock<DoorBlock> BLOODWOOD_DOOR = BLOCKS.register("bloodwood_door",
            () -> new DoorBlock(BlockSetType.ACACIA, BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops().explosionResistance(4f).noOcclusion().ignitedByLava()));
    public static final DeferredItem<BlockItem> BLOODWOOD_DOOR_ITEM = ITEMS.registerSimpleBlockItem("bloodwood_door", BLOODWOOD_DOOR);
    public static final DeferredItem<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GECKO, 16579584, 0xffac00,
                    new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> LPHANTOM_SPAWN_EGG = ITEMS.register("cave_phantom_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.LEGGED_PHANTOM, 0x38bfaf, 0x78bfaf,
                    new Item.Properties().stacksTo(64)));
    public static final DeferredBlock<ButtonBlock> BLOODWOOD_BUTTON = BLOCKS.register("bloodwood_button", () -> new ButtonBlock(BlockSetType.ACACIA, 1, BlockBehaviour.Properties.of().strength(1f).requiresCorrectToolForDrops().explosionResistance(1f).noOcclusion().ignitedByLava()));
    public static final DeferredItem<BlockItem> BLOODWOOD_BUTTON_ITEM = ITEMS.registerSimpleBlockItem("bloodwood_button", BLOODWOOD_BUTTON);
    public static final DeferredItem<Item> TRADER_SPAWN_EGG = ITEMS.register("trader_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TRADER,0x78bfaf , 0,
                    new Item.Properties().stacksTo(64)));
    public static final DeferredBlock<Block> INSTANT_BOOM = BLOCKS.register("instant_boom", () -> new BoomBlock(BlockBehaviour.Properties.of().noOcclusion().destroyTime(1f).ignitedByLava().noLootTable()));
    public static final DeferredItem<BlockItem> INSTANTBOOM_ITEM = ITEMS.registerSimpleBlockItem("instant_boom", INSTANT_BOOM);
    public static final DeferredItem<Item> RADIATION_STAFF = ITEMS.register("tnt_staff", () -> new RadiationStaffItem(new Item.Properties().rarity(Rarity.RARE).stacksTo(1).durability(100)));
    public static final DeferredItem<Item> RADIUM_INGOT = ITEMS.registerSimpleItem("radium_ingot");
    public static final DeferredBlock<Block> RADIUM_ORE = BLOCKS.register("radium_ore", () -> new DropExperienceBlock(UniformInt.of(1,2),BlockBehaviour.Properties.of().explosionResistance(7f).strength(6f)));
    public static final DeferredItem<BlockItem> RADIUM_ORE_ITEM = ITEMS.registerSimpleBlockItem("radium_ore", RADIUM_ORE);
    public static final DeferredItem<Item> STRANGE_ARROW = ITEMS.register("mysterious_arrow", () -> new Throwable(new Item.Properties().durability(85)));
    public static final DeferredItem<Item> RAW_RADIUM = ITEMS.registerSimpleItem("raw_radium");
    public static final DeferredItem<Item> PHANTOM_SHOVEL = ITEMS.register("phantom_shovel", () -> new ShovelItem(ModToolTiers.PHANTOM, new Item.Properties().attributes(ShovelItem.createAttributes(ModToolTiers.PHANTOM, 1.5F, -3.0F))));
    public static final DeferredBlock<Block> DARK_PORTAL = BLOCKS.register("dark_portal", () -> new DarkPortalBlock(BlockBehaviour.Properties.of().noLootTable().noOcclusion().instabreak().noCollission().randomTicks().lightLevel(p_187433_ -> 15)));
    public static final DeferredItem<BlockItem> PORTAL_ITEM = ITEMS.registerSimpleBlockItem("dark_portal", DARK_PORTAL);
    public static final DeferredItem<Item> MELON_JUICE_HALF_EMPTY = ITEMS.register("melon_juice_half", ()-> new DrinkBottle(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(2).saturationModifier(4)
            .usingConvertsTo(Items.GLASS_BOTTLE).build())));
    public static final DeferredItem<Item> FERMENTED_MELON_JUICE_HALF = ITEMS.register("fermented_melon_juice_half", ()-> new DrinkBottle(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(1).saturationModifier(5)
            .usingConvertsTo(Items.GLASS_BOTTLE).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 593), 0.5f).effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 593), 0.5f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1250), 0.65f).build())));
    public static final DeferredItem<Item> MELON_JUICE = ITEMS.register("melon_juice", ()-> new DrinkBottle(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(2).saturationModifier(3)
            .usingConvertsTo(ExploMod.MELON_JUICE_HALF_EMPTY.get()).build())));
    public static final DeferredItem<Item> FERMENTED_MELON_JUICE = ITEMS.register("fermented_melon_juice", ()-> new DrinkBottle(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(1).saturationModifier(4)
            .usingConvertsTo(ExploMod.FERMENTED_MELON_JUICE_HALF.get()).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5000), 0.5f).effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 3000), 0.5f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 1000), 0.35f).build())));
    public static final DeferredItem<Item> STORM_BERRY_ROLL = ITEMS.register("stormberry_roll", () -> new StormBerryRoll(new Item.Properties().rarity(Rarity.RARE).food(new FoodProperties.Builder().nutrition(5).saturationModifier(1).build()).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
    public static final DeferredItem<Item> STORM_BERRY = ITEMS.register("storm_berry", () -> new Item(new Item.Properties().rarity(Rarity.COMMON).food(new FoodProperties.Builder().nutrition(1).effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300), 0.5f).effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 600), 0.5f)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 3000), 0.999f).saturationModifier(0f).build())));
    public static final DeferredItem<Item> STORMBERRY_COOKIE = ITEMS.register("stormberry_cookie", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.15f).build())));
    public static final DeferredItem<Item> STORMBERRY_PIE = ITEMS.register("stormberry_pie", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.3f).build())));
    public static final DeferredItem<Item> APPLE_PIE = ITEMS.register("apple_pie", () -> new ShieldDisabler(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.3f).build())));
    public static final DeferredBlock<Block> STORMBERRY_CAKE = BLOCKS.register("stormberry_cake", () -> new CakeBlock(BlockBehaviour.Properties.of().forceSolidOn().strength(0.5F).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));
    public static final DeferredItem<BlockItem> STORM_BERRY_CAKE = ITEMS.registerSimpleBlockItem("stormberry_cake", STORMBERRY_CAKE);
    public static final DeferredItem<Item> MAGIC_RADIATION_STAFF = ITEMS.register("radiation_staff", () -> new MagicRadiationStaffItem(new Item.Properties().rarity(Rarity.RARE).durability(50)));
    public static final DeferredItem<Item> PHANTOM_PICKAXE = ITEMS.register("phantom_pickaxe", () -> new PickaxeItem(ModToolTiers.PHANTOM, new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolTiers.PHANTOM, 1.0F, -2.8F))));
    public static final DeferredItem<Item> RED_SNOWBALL = ITEMS.register("red_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> WHITE_SNOWBALL = ITEMS.register("white_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> GREEN_SNOWBALL = ITEMS.register("green_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> LIGHT_BLUE_SNOWBALL = ITEMS.register("light_blue_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> BLUE_SNOWBALL = ITEMS.register("blue_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> YELLOW_SNOWBALL = ITEMS.register("yellow_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> ORANGE_SNOWBALL = ITEMS.register("orange_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> BLACK_SNOWBALL = ITEMS.register("black_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredItem<Item> BROWN_SNOWBALL = ITEMS.register("brown_snowball", () -> new SnowballItem(new Item.Properties().stacksTo(16)));
    public static final DeferredBlock<Block> STORMBERRY_BUSH = BLOCKS.register("stormberry_bush", () -> new StormBerryBushBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)));
    public static final DeferredItem<Item> RAPIER_OF_NINE_LIVES = ITEMS.register("rapier_of_nine_lives", () -> new RapierOfNineLivesItem(ModToolTiers.RAPIER, new Item.Properties().attributes(SwordItem.createAttributes(ModToolTiers.RAPIER, 2, -2.3F)).rarity(Rarity.RARE)));
    public static final DeferredBlock<Block> BLOODWOOD_SAPLING = BLOCKS.register("bloodwood_sapling",
            () -> new SaplingBlock(ModTreeGrower.BLOODWOOD, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
    public static final DeferredItem<BlockItem> BLOOD_WOOD_SAPLING = ITEMS.registerSimpleBlockItem("bloodwood_sapling", BLOODWOOD_SAPLING);
    public static final DeferredItem<BlockItem> STORM_BERRY_BUSH = ITEMS.registerSimpleBlockItem("stormberry_bush", STORMBERRY_BUSH);
    public static final DeferredItem<Item> RADIUM_FIRESTARTER = ITEMS.register("radium_firestarter", () -> new FlintAndSteelItem(new Item.Properties().durability(500)));
    public static final DeferredItem<Item> RADIUM_NUGGET = ITEMS.registerSimpleItem("radium_nugget");
    public static final DeferredBlock<Block> FLOWERED_LEAVES = BLOCKS.register("flowering_oak_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.of().ignitedByLava().destroyTime(0.5f).noOcclusion().randomTicks()));
    public static final DeferredItem<BlockItem> FLOWERING_OAK_LEAVES = ITEMS.registerSimpleBlockItem("flowering_oak_leaves", FLOWERED_LEAVES);
    public static final DeferredBlock<Block> WINTER_OAK_SAPLING = BLOCKS.register("winter_oak_sapling", () -> new SaplingBlock(ModTreeGrower.WINTER_OAK, BlockBehaviour.Properties.of().instabreak().noCollission()));
    public static final DeferredItem<BlockItem> WINTEROAK_SAPLING = ITEMS.registerSimpleBlockItem("winter_oak_sapling", WINTER_OAK_SAPLING);
    public static final DeferredItem<Item> GRASS_GOLEM_SPAWN_EGG = ITEMS.register("grass_golem_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GRASS_GOLEM,0x78bfaf , 894731,
                    new Item.Properties().stacksTo(64)));
    public static final DeferredItem<Item> HEALTH_INCREASER = ITEMS.register("health_boost", () -> new HeartGiverItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1).fireResistant()));
    public static final DeferredBlock<Block> DENSE_GRASS = BLOCKS.registerSimpleBlock("dense_grass");
    public static final DeferredItem<BlockItem> DENSEGRASS = ITEMS.registerSimpleBlockItem("dense_grass", DENSE_GRASS);
    public static final DeferredItem<Item> DASH_SWORD = ITEMS.register("dash_sword", () -> new DashSwordItem(Tiers.IRON, new Item.Properties().durability(15).attributes(SwordItem.createAttributes(Tiers.IRON, 3, -2.4F))));
    public static final DeferredItem<Item> HARBINGER = ITEMS.register("harbinger", () -> new SightedCrossBow(new Item.Properties().durability(320).rarity(Rarity.UNCOMMON)));
    public static final DeferredBlock<Block> AIR = BLOCKS.register("glowing_air", () -> new TimeOutAirBlock(BlockBehaviour.Properties.of()
            .replaceable().randomTicks().noCollission().lightLevel(p_187433_ -> 11).noLootTable().air()));
    public static final DeferredBlock<Block> GLOWBERRY_BUSH = BLOCKS.register("glowberry_bush", () -> new GlowBerryBushBlock(Items.GLOW_BERRIES, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).randomTicks().noCollission().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)
            .lightLevel(state -> state.getValue(GlowBerryBushBlock.GLOWING)?12:1)));
    public static final DeferredBlock<Block> CRATE = BLOCKS.register("crate", () -> new EggBasketBlock(BlockBehaviour.Properties.of().noOcclusion().destroyTime(2f)));
    public static final DeferredItem<BlockItem> CRATE_ITEM = ITEMS.registerSimpleBlockItem("crate", CRATE);
    public static final DeferredBlock<Block> EMPTY_CRATE = BLOCKS.register("empty_crate", () -> new CrateBlock(BlockBehaviour.Properties.of().noOcclusion().destroyTime(1.5f)));
    public static final DeferredItem<BlockItem> EMPTY_CRATE_ITEM = ITEMS.registerSimpleBlockItem("empty_crate", EMPTY_CRATE);
    public static final DeferredBlock<Block> CANE_CRATE = BLOCKS.register("sugar_cane_crate", () -> new SugarCaneCrateBlock(BlockBehaviour.Properties.of().noOcclusion().destroyTime(1.5f)));
    public static final DeferredItem<BlockItem> CANE_CRATE_ITEM = ITEMS.registerSimpleBlockItem("sugar_cane_crate", CANE_CRATE);
    public static final DeferredItem<Item> LOCATION_SAVER = ITEMS.register("location_saver", () -> new LocationSaverItem(new Item.Properties().stacksTo(1).setNoRepair().rarity(Rarity.UNCOMMON)));
    public static final DeferredItem<Item> DARK_PORTAL_CREATOR = ITEMS.register("dark_portal_placer", () -> new DarkPortalItem(new Item.Properties().fireResistant().stacksTo(1).rarity(Rarity.EPIC).setNoRepair()));
    public static final DeferredBlock<Block> MOSSY_CHISELED_STONE_BRICKS = BLOCKS.register("mossy_chiseled_stone_bricks", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.55F, 6.0F)));
    public static final DeferredItem<BlockItem> MOSSY_CHISELED_STONE_BRICKS_ITEM = ITEMS.registerSimpleBlockItem("mossy_chiseled_stone_bricks", MOSSY_CHISELED_STONE_BRICKS);
    public static final DeferredBlock<Block> INFESTED_MOSSY_CHISELED_STONE_BRICKS = BLOCKS.register("infested_mossy_chiseled_stone_bricks", () -> new InfestedBlock(MOSSY_CHISELED_STONE_BRICKS.get(), BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).strength(1.5F, 6.0F)));
    public static final DeferredItem<BlockItem> INFESTED_MOSSY_CHISELED_STONE_BRICKS_ITEM = ITEMS.registerSimpleBlockItem("infested_mossy_chiseled_stone_bricks", INFESTED_MOSSY_CHISELED_STONE_BRICKS);
    public static final DeferredBlock<Block> INFESTED_DIORITE = BLOCKS.register("infested_diorite", () -> new InfestedBlock(Blocks.DIORITE, BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).strength(1.5F, 6.0F)));
    public static final DeferredItem<BlockItem> INFESTED_DIORITE_ITEM = ITEMS.registerSimpleBlockItem("infested_diorite", INFESTED_DIORITE);
    public static final DeferredBlock<SlabBlock> CHISELED_STONE_BRICKS_SLAB = BLOCKS.register("chiseled_stone_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(1.5f).requiresCorrectToolForDrops().explosionResistance(6f).noOcclusion()));
    public static final DeferredItem<BlockItem> CHISELED_STONE_BRICKS_SLAB_ITEM = ITEMS.registerSimpleBlockItem("chiseled_stone_bricks_slab", CHISELED_STONE_BRICKS_SLAB);
    public static final DeferredBlock<StairBlock> CHISELED_STONE_BRICKS_STAIRS = BLOCKS.register("chiseled_stone_bricks_stairs",
            () -> legacyStair(Blocks.CHISELED_STONE_BRICKS));
    public static final DeferredItem<BlockItem> CHISELED_STONE_BRICKS_STAIRS_ITEM = ITEMS.registerSimpleBlockItem("chiseled_stone_bricks_stairs", CHISELED_STONE_BRICKS_STAIRS);
    public static final DeferredBlock<WallBlock> CHISELED_STONE_BRICKS_WALL = BLOCKS.register("chiseled_stone_bricks_wall",
            () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_STONE_BRICKS).forceSolidOn()));
    public static final DeferredItem<BlockItem> CHISELED_STONE_BRICKS_WALL_ITEM = ITEMS.registerSimpleBlockItem("chiseled_stone_bricks_wall", CHISELED_STONE_BRICKS_WALL);
    public static final DeferredBlock<Block> RED_FLOWER = BLOCKS.register("red_flower", () -> new FlowerBlock(MobEffects.GLOWING, 5.0F, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).noOcclusion()));
    public static final DeferredItem<BlockItem> RED_FLOWER_ITEM = ITEMS.registerSimpleBlockItem("red_flower", RED_FLOWER);
    public static final DeferredBlock<SlabBlock> MOSSY_CHISELED_STONE_BRICKS_SLAB = BLOCKS.register("mossy_chiseled_stone_bricks_slab",
            () -> new SlabBlock(BlockBehaviour.Properties.of().strength(1.75f).requiresCorrectToolForDrops().explosionResistance(6f).noOcclusion()));
    public static final DeferredItem<BlockItem> MOSSY_CHISELED_STONE_BRICKS_SLAB_ITEM = ITEMS.registerSimpleBlockItem("mossy_chiseled_stone_bricks_slab", MOSSY_CHISELED_STONE_BRICKS_SLAB);


    // spells
    public static final DeferredItem<Item> FIREBALL_SPELL = ITEMS.register("fireball_spell", () -> new SpellItem(new Item.Properties().setNoRepair().rarity(Rarity.EPIC).durability(1).stacksTo(1).fireResistant()
            .component(AtheriaDataComponents.SPELL, new SavedSpells(SpellRegistries.FIREBALL_SPELL))));
    public static final DeferredItem<Item> DASH_SPELL = ITEMS.register("dash_spell", () -> new SpellItem(new Item.Properties().setNoRepair().rarity(Rarity.UNCOMMON).durability(1).stacksTo(1).fireResistant()
            .component(AtheriaDataComponents.SPELL, new SavedSpells(SpellRegistries.DASH_SPELL))));
    public static final DeferredItem<Item> AIRBURST_SPELL = ITEMS.register("growth_spell", () -> new SpellItem(new Item.Properties().setNoRepair().rarity(Rarity.RARE).durability(1).stacksTo(1).fireResistant()
            .component(AtheriaDataComponents.SPELL, new SavedSpells(SpellRegistries.AIRBURST_SPELL))));
    public static final DeferredItem<Item> SPELL_TOME = ITEMS.register("tome", ()-> new SpellItem(new Item.Properties().stacksTo(1).setNoRepair().durability(10).fireResistant().rarity(Rarity.RARE)
            .component(AtheriaDataComponents.SPELL, new SavedSpells(SpellRegistries.NULLSPELL.getDelegate()))));

    //creative tabs
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.explomod.new")) //The language key
            .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
            .icon(() -> NEW.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
                output.accept(MOSSY_CHISELED_STONE_BRICKS_ITEM.get());
                output.accept(EXAMPLE_BLOCK_ITEM.get());
                output.accept(INSTANTBOOM_ITEM.get());
                output.accept(LOCATION_SAVER.get());
                output.accept(PORTAL_ITEM.get());
                output.accept(CANE_CRATE_ITEM.get());
                output.accept(FERMENTED_GLOWSTONE.get());
                output.accept(EMPTY_CRATE_ITEM.get());
                output.accept(DASH_SWORD.get());
                output.accept(CRATE_ITEM.get());
                output.accept(HARBINGER.get());
                output.accept(INFESTED_MOSSY_CHISELED_STONE_BRICKS_ITEM.get());
                output.accept(CHISELED_STONE_BRICKS_SLAB_ITEM.get());
                output.accept(CHISELED_STONE_BRICKS_STAIRS_ITEM.get());
                output.accept(CHISELED_STONE_BRICKS_WALL_ITEM.get());
                output.accept(MOSSY_CHISELED_STONE_BRICKS_SLAB_ITEM.get());
                output.accept(WINTEROAK_SAPLING.get());
                output.accept(FLOWERING_OAK_LEAVES.get());
                output.accept(GRASS_GOLEM_SPAWN_EGG.get());
                output.accept(RAPIER_OF_NINE_LIVES.get());
                output.accept(DENSEGRASS.get());
                output.accept(RADIUM_NUGGET.get());
                output.accept(BLOOD_WOOD_SAPLING.get());
                output.accept(STORM_BERRY_BUSH.get());
                output.accept(STORMBERRY_COOKIE.get());
                output.accept(MAGIC_RADIATION_STAFF.get());
                output.accept(STORMBERRY_PIE.get());
                output.accept(APPLE_PIE.get());
                output.accept(STORM_BERRY_ROLL.get());
                output.accept(FERMENTED_MELON_JUICE.get());
                output.accept(MELON_JUICE.get());
                output.accept(TRADER_SPAWN_EGG.get());
                output.accept(PHANTOM_HELM.get());
                output.accept(PHANTOM_INGOT.get());
                output.accept(STRANGE_ARROW.get());
                output.accept(LPHANTOM_SPAWN_EGG.get());
                output.accept(GECKO_SPAWN_EGG.get());
                output.accept(BLOODWOOD_BUTTON_ITEM.get());
                output.accept(STORM_BERRY.get());
                output.accept(BLOODWOOD_DOOR_ITEM.get());
                output.accept(RADIATION_STAFF.get());
                output.accept(PHANTOM_HOE.get());
                output.accept(TABLE_ITEM.get());
                output.accept(LOG_BLOCK_ITEM.get());
                output.accept(PHANTOM_BLOCK_ITEM.get());
                output.accept(PHANTOM_SHOVEL.get());
                output.accept(DAGGER.get());
                output.accept(STOOL_ITEM.get());
                output.accept(PHANTOM_AXE.get());
                output.accept(PHANTOM_BOOTS.get());
                output.accept(PHANTOM_SWORD.get());
                output.accept(PHANTOM_LEGGINGS.get());
                output.accept(PHANTOM_PICKAXE.get());
                output.accept(THROWABLE.get());
                output.accept(PHANTOM_CHESTPLATE.get());
                output.accept(GREEN_POPSICLE.get());
                output.accept(BOW.get());
                output.accept(WEAPON_ITEM.get());
                output.accept(RADIUM_FIRESTARTER.get());
                output.accept(FUEL.get());
                output.accept(LAMP_ITEM.get());
                output.accept(REDTRAV_ITEM.get());
                output.accept(LEGENDS_FROST_MUSIC_DISC.get());
                output.accept(LAVA_ITEM.get());
                output.accept(XRAY_BLOCK_ITEM.get());
                output.accept(COPPER_PICKAXE.get());
                output.accept(TANK_BLOCK_ITEM.get());
                output.accept(NICE_STICK.get());
                output.accept(NEW_SWORD.get());
                output.accept(YELLOW_POPSICLE.get());
                output.accept(POPSICLE_FOOD.get());
                output.accept(STONED_ITEM.get());
                output.accept(DAGGER.get());
                output.accept(BLUE_POPSICLE.get());
                output.accept(HEART_FOOD.get());
                output.accept(INFESTED_DIORITE_ITEM.get());
                output.accept(RED_SNOWBALL);
                output.accept(GREEN_SNOWBALL);
                output.accept(BLUE_SNOWBALL);
                output.accept(BROWN_SNOWBALL);
                output.accept(WHITE_SNOWBALL, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY);
                output.accept(LIGHT_BLUE_SNOWBALL);
                output.accept(YELLOW_SNOWBALL);
                output.accept(ORANGE_SNOWBALL);
                output.accept(RED_FLOWER_ITEM.get());
            }).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RADIUM_TAB = CREATIVE_MODE_TABS.register("radium_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.explomod.radium")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> RADIATION_STAFF.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(RADIATION_STAFF.get());
                output.accept(MAGIC_RADIATION_STAFF.get());
                output.accept(RADIUM_INGOT.get());
                output.accept(RADIUM_ORE_ITEM.get());
                output.accept(RAW_RADIUM.get());
                output.accept(RADIUM_NUGGET.get());
                output.accept(RADIUM_FIRESTARTER.get());
            }).build());
    //dagger creative tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAGIC_TAB = CREATIVE_MODE_TABS.register("magic_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.explomod.magic"))
            .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
            .icon(() -> DASH_SPELL.get().getDefaultInstance())
            .displayItems((parameters, output) -> parameters.holders().lookup(SpellRegistries.CUSTOM_THINGS.getRegistryKey()).ifPresent((p_337908_) -> {
                output.accept(DASH_SPELL, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                output.accept(FIREBALL_SPELL, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                output.accept(AIRBURST_SPELL, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
                output.accept(SPELL_TOME, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY);
            })).build());


    // use git add before git commit -m "whatever the commit message is"
    // after commit use git push

    // mod eventbus
    public ExploMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        ModLootModifiers.register(modEventBus);
        ModParticles.register(modEventBus);
        ModEffects.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModSounds.register(modEventBus);
        ModEntities.register(modEventBus);
        SpellRegistries.register(modEventBus);
        ModFeature.register(modEventBus);
        ModPotions.register(modEventBus);
        AtheriaDataComponents.register(modEventBus);
        AtheriaStats.register(modEventBus);
        AtheriaTriggers.register(modEventBus);
        AtheriaDataAttachments.register(modEventBus);
        ModBiomes.register(modEventBus);
        //ModStructureType.register(modEventBus); structures added through json files
        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExploMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the com.example.explomod.item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        if (dist == Dist.CLIENT) {
            ExploModClient.clientInit(modEventBus);
        }
        this.eventSetup(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }

    public void eventSetup(IEventBus neoBus) {
        IEventBus bus = NeoForge.EVENT_BUS;

        bus.addListener(AtheriaCommands::registerCommands);
        bus.addListener(ClientProxy::registerCommands);
    }

    // Add the example block com.example.explomod.item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(POPSICLE_FOOD);
            event.accept(YELLOW_POPSICLE);
            event.accept(BLUE_POPSICLE);
            event.accept(GREEN_POPSICLE);
            event.accept(STORMBERRY_COOKIE);
            event.accept(STORM_BERRY_ROLL);
            event.accept(STORMBERRY_PIE);
            event.accept(STORM_BERRY_CAKE);
            event.accept(MELON_JUICE);
            event.accept(FERMENTED_MELON_JUICE);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(RED_SNOWBALL);
            event.accept(WHITE_SNOWBALL);
            event.accept(GREEN_SNOWBALL);
            event.accept(BLUE_SNOWBALL);
            event.accept(LIGHT_BLUE_SNOWBALL);
            event.accept(YELLOW_SNOWBALL);
            event.accept(ORANGE_SNOWBALL);
            event.accept(BLACK_SNOWBALL);
            event.accept(BROWN_SNOWBALL);
        }
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(GECKO_SPAWN_EGG);
            event.accept(TRADER_SPAWN_EGG);
            event.accept(GRASS_GOLEM_SPAWN_EGG);
            event.accept(LPHANTOM_SPAWN_EGG);
        }
        if (event.getTabKey()==CreativeModeTabs.BUILDING_BLOCKS){
            event.accept(MOSSY_CHISELED_STONE_BRICKS_ITEM);
            event.accept(MOSSY_CHISELED_STONE_BRICKS_SLAB_ITEM);
            event.accept(CHISELED_STONE_BRICKS_SLAB_ITEM);
            event.accept(CHISELED_STONE_BRICKS_STAIRS_ITEM);
            event.accept(CHISELED_STONE_BRICKS_WALL_ITEM);
        }
        if (event.getTabKey()==CreativeModeTabs.FUNCTIONAL_BLOCKS){
            event.accept(INFESTED_DIORITE_ITEM);
            event.accept(INFESTED_MOSSY_CHISELED_STONE_BRICKS_ITEM);
        }
        if (event.getTabKey()==CreativeModeTabs.NATURAL_BLOCKS){
            event.accept(RED_FLOWER_ITEM.asItem());
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @SubscribeEvent
    public void OnPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 22));
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinLevelEvent event) {
    }

    @SubscribeEvent
    public void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.MASON) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD_BLOCK, 38),
                    new ItemStack(ExploMod.EXAMPLE_BLOCK.get(), 1), 2, 5, -0.25f));
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ExploMod.WEAPON_ITEM.get(), 1), 1, 1, 0.5f));
            if(Config.ITEM_STRINGS.get().contains("explomod:stoned")){
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 1),
                        new ItemStack(ExploMod.GROUND_ITEM.get(), 32), 1, 1, 0.5f));
            } else {
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 1),
                        new ItemStack(ExploMod.GROUND_ITEM.get(), 16), 1, 1, 0.5f));
            }
        }if(event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(Items.STICK, 1), 1000, 1, 8.87f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 2),
                    new ItemStack(ExploMod.WEAPON_ITEM.get(), 1), 1, 5, 35.85f));
        }if(event.getType() == VillagerProfession.CLERIC) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 17),
                    new ItemStack(ExploMod.LOG_BLOCK_ITEM.get(), 1), 1, 1, 8.87f));
            if (Config.BUTTON.getAsBoolean()) {
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 32),
                        new ItemStack(Items.BAT_SPAWN_EGG, 1), 1, 5, 35.85f));
                trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 32),
                        new ItemStack(ExploMod.KIT_ITEM.get(), 1), 1, 5, 35.85f));
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 32),
                        new ItemStack(ExploMod.GROUND_ITEM.get(), 1), 1, 5, 35.85f));
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 32),
                        new ItemStack(ExploMod.NEW.get(), 1), 1, 5, 35.85f));
                trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 32),
                        new ItemStack(ExploMod.STONED_ITEM.get(), 1), 1, 5, 35.85f));
            }else{
                trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 64),
                        new ItemStack(Items.BAT_SPAWN_EGG, 1), 1, 5, 35.85f));
            }
        }if(event.getType() == VillagerProfession.CARTOGRAPHER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            if (Config.BUTTON.getAsBoolean()) {
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 18),
                        new ItemStack(ExploMod.REDTRAV_ITEM.get(), 2), 1, 1, 8.87f));
            } else{
                trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 11),
                        new ItemStack(ExploMod.REDTRAV_ITEM.get(), 1), 1, 1, 8.87f));
            }

            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 14),
                    new ItemStack(ExploMod.NICE_STICK.get(), 1), 1, 5, 35.85f));
        }
        // Vendor Trades
        if(event.getType()==ModVillagers.MODSELLER.value()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // random creators
            int r8 = RandomSource.create().nextIntBetweenInclusive(0,8);
            int r6 = RandomSource.create().nextIntBetweenInclusive(0,6);
            int r4 = RandomSource.create().nextIntBetweenInclusive(0,4);
            boolean rb = RandomSource.create().nextBoolean();

            // level 1 trades
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD, 2),
                    new ItemStack(ExploMod.EMPTY_CRATE_ITEM.asItem(), 1), 5, 1, 0.17f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD,3),
                    new ItemStack(ExploMod.STORMBERRY_COOKIE.get(), 4), 7, 1,0.08f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.SPRUCE_PLANKS, 16),
                    new ItemStack(Items.EMERALD, 1), 2, 2, 0.35f));

            trades.get(1).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.SUGAR, 16+r8),
                    new ItemStack(Items.EMERALD,1), 4, 2,0.1f));

            if(rb){trades.get(1).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.SUGAR_CANE, 14 + r6),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.1f));
            }else{trades.get(1).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EGG, 14 + r4),
                    new ItemStack(Items.EMERALD, 1), 4, 2, 0.1f));}

            // level 2 trades
            if(rb){trades.get(2).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ExploMod.CANE_CRATE_ITEM.get(), 1), 1, 2, 0.075f));
            }else{trades.get(2).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ExploMod.CRATE_ITEM.get(), 1), 1, 2, 0.075f));}

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD, 1),
                    new ItemStack(Items.PAPER, 8), 8, 2, 0.125f));

            trades.get(2).add((entity, randomSource) -> new MerchantOffer(new ItemCost(ExploMod.FERMENTED_GLOWSTONE, 4),
                    new ItemStack(Items.EMERALD, 1), 4, 3, 0.1f));

            // level 3 trades
            trades.get(3).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD, 6),
                    new ItemStack(ExploMod.PHANTOM_INGOT.get(), 1), 5, 3, 0.15f));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD, 4),
                    new ItemStack(ExploMod.POPSICLE_FOOD.get(), 1), 15, 3, 0.1f));

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(new ItemCost(ExploMod.EXAMPLE_BLOCK_ITEM.asItem(),1),
                    new ItemStack(Items.EMERALD, 4), 10, 4, 0.035f));

            // level 4 trades
            trades.get(4).add((entity, randomSource) -> new MerchantOffer(new ItemCost(ExploMod.STORM_BERRY.get(),12+r6),
                    new ItemStack(Items.EMERALD, 1), 10, 5, 0.101255f));

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD,1+r4),
                    new ItemStack(ExploMod.STORM_BERRY_ROLL.get(), 1), 5, 4, 0.1105f));

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD,1+r4),
                    new ItemStack(ExploMod.STORMBERRY_PIE.get(), 1), 5, 4, 0.1105f));

            trades.get(4).add((entity, randomSource) -> new MerchantOffer(new ItemCost(Items.EMERALD,1+r4),
                    new ItemStack(ExploMod.STORM_BERRY_CAKE.get(), 1), 5, 4, 0.1105f));
        }

    }
    @SubscribeEvent
    public void addWanderingTraders(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 8),
                new ItemStack(ExploMod.XRAY_BLOCK_ITEM.get(), 1), 3, 3, 0.05f));
        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                new ItemStack(Items.SUGAR, 6), 14, 1, 0.81f));
        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 10),
                new ItemStack(ExploMod.LEGENDS_FROST_MUSIC_DISC.get(), 1), 1, 1, 0.1f));
        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD,4),
                new ItemStack(ExploMod.RADIUM_INGOT.get(), 1), 5, 1, 0.1f));

        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(ExploMod.EXAMPLE_ITEM.get(), 1),
                new ItemStack(Items.EMERALD, 12), 3, 3, 0.01f));
        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 9),
                new ItemStack(ExploMod.POPSICLE_FOOD.get(), 1), 2, 3, 0.21f));
        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                new ItemStack(Items.ARROW, 3), 3, 3, 0.11f));
    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ModItemProperties.addCustomItemProperties();
            EntityRenderers.register(ModEntities.GECKO.get(), GeckoRenderer::new);
            EntityRenderers.register(ModEntities.GRASS_GOLEM.get(), GrassGolemRenderer::new);
            EntityRenderers.register(ModEntities.LEGGED_PHANTOM.get(), PhantomLizardRenderer::new);
            EntityRenderers.register(ModEntities.TRADER.get(), TraderRenderer::new);
            EntityRenderers.register(ModEntities.STRANGE_ARROW.get(), StrangeArrowRenderer::new);
            EntityRenderers.register(ModEntities.BOSS_GRASS_GOLEM.get(), BossGrassGolemRenderer::new);
            EntityRenderers.register(ModEntities.GLOW_BAT.get(), GlowBatRenderer::new);
        }

        @SubscribeEvent
        public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ModParticles.SAFETY_PARTICLES.get(), SafteyParticle.Provider::new);
        }
    }

    private static StairBlock legacyStair(Block baseBlock) {
        return new StairBlock(baseBlock.defaultBlockState(), BlockBehaviour.Properties.ofLegacyCopy(baseBlock));
    }
}
