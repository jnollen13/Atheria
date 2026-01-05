package com.example.explomod;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
import sound.ModSounds;
import com.example.explomod.villager.ModVillagers;

import java.util.List;
import java.util.function.Supplier;

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

    // Creates a new Block with the id "explomod:example_block", combining the namespace and path
    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).destroyTime(35f).explosionResistance(999f).friction(0.85f).requiresCorrectToolForDrops().sound(SoundType.STONE));
    // Creates a new BlockItem with the id "explomod:example_block", combining the namespace and path           () -> new DropExperienceBlock(UniformInt.of(2, 4),
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredBlock<Block> LOG_BLOCK = BLOCKS.registerSimpleBlock("log", BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).jumpFactor(2f).sound(SoundType.WOOD));
    public static final DeferredItem<BlockItem> LOG_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("log", LOG_BLOCK);
    // Creates a new food item with the id "explomod:example_id", nutrition 1 and saturation 2
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(5).saturationModifier(8f).build()));
    public static final DeferredItem<Item> WEAPON_ITEM = ITEMS.registerSimpleItem("weapon", new Item.Properties().stacksTo(1).durability(200).fireResistant());
    public static final DeferredItem<Item> COPPER_PICKAXE = ITEMS.registerSimpleItem("copper_pickaxe", new Item.Properties());
    public static final DeferredBlock<Block> XRAY_BLOCK = BLOCKS.registerSimpleBlock("xrayer", BlockBehaviour.Properties.of().instabreak().air());
    public static final DeferredItem<BlockItem> XRAY_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("xrayer", XRAY_BLOCK);
/*    public static final DeferredBlock<StairBlock> LOG_STAIRS = BLOCKS.registerSimpleBlock("log_stairs", () -> new StairBlock(ExploMod.LOG_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.of().strength(2f).requiresCorrectToolForDrops()));*/
    public static final DeferredBlock<Block> REDTRAV = BLOCKS.registerSimpleBlock("redtrav", BlockBehaviour.Properties.of().sound(SoundType.WET_GRASS).instabreak().jumpFactor(3f).ignitedByLava().speedFactor(7f));
    public static final DeferredItem<BlockItem> REDTRAV_ITEM = ITEMS.registerSimpleBlockItem("redtrav", REDTRAV);



    // Creates a creative tab with the id "explomod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.explomod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> EXAMPLE_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(EXAMPLE_BLOCK_ITEM.get());
                output.accept(LOG_BLOCK_ITEM.get());
                output.accept(WEAPON_ITEM.get());
                output.accept(XRAY_BLOCK_ITEM.get());
                output.accept(COPPER_PICKAXE.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExploMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModVillagers.register(modEventBus);
        ModSounds.register(modEventBus);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExploMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
    @SubscribeEvent
    public void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.MASON) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD_BLOCK, 8),
                    new ItemStack(ExploMod.EXAMPLE_BLOCK.get(), 1), 3, 25, 25.35f));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 3),
                    new ItemStack(ExploMod.WEAPON_ITEM.get(), 1), 1, 1, 1.1f));
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
                    new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ExploMod.LOG_BLOCK_ITEM.get(), 1), 1, 1, 8.87f));

            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 64),
                    new ItemStack(Items.BAT_SPAWN_EGG, 1), 1, 5, 35.85f));
        }if(event.getType() == VillagerProfession.CARTOGRAPHER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 8),
                    new ItemStack(ExploMod.REDTRAV_ITEM.get(), 1), 1, 1, 8.87f));

            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 14),
                    new ItemStack(ExploMod.XRAY_BLOCK_ITEM.get(), 1), 1, 5, 35.85f));
        }
    }
}
