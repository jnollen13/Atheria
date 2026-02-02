package com.example.explomod;

import com.example.explomod.block.custom.LavaHolderBlock;
import com.example.explomod.block.custom.NewLampBlock;
import com.example.explomod.block.custom.StoolBlock;
import com.example.explomod.block.custom.TableBlock;
import com.example.explomod.loot.ModLootModifiers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
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
import sound.ModSounds;
import com.example.explomod.villager.ModVillagers;

import java.util.List;

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

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE).destroyTime(35f).explosionResistance(999f).friction(0.85f).requiresCorrectToolForDrops().sound(SoundType.STONE));
    //() -> new DropExperienceBlock(UniformInt.of(2, 4),
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("example_block", EXAMPLE_BLOCK);
    public static final DeferredBlock<Block> LOG_BLOCK = BLOCKS.registerSimpleBlock("log", BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).jumpFactor(3.141526f).sound(SoundType.WOOD).pushReaction(PushReaction.PUSH_ONLY));
    public static final DeferredItem<BlockItem> LOG_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("log", LOG_BLOCK);
    public static final DeferredItem<Item> EXAMPLE_ITEM = ITEMS.registerSimpleItem("example_item", new Item.Properties().food(new FoodProperties.Builder()
            .alwaysEdible().nutrition(5).saturationModifier(8f).effect(() -> new MobEffectInstance(MobEffects.POISON, 500), 0.456789f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 424, 9), 0.756f).build()));
    public static final DeferredItem<Item> WEAPON_ITEM = ITEMS.registerSimpleItem("weapon", new Item.Properties().stacksTo(1).durability(200).attributes(SwordItem.createAttributes(Tiers.STONE, 4, -3.2F)));
    public static final DeferredItem<Item> COPPER_PICKAXE = ITEMS.registerSimpleItem("copper_pickaxe", new Item.Properties().stacksTo(4).rarity(Rarity.COMMON));
    public static final DeferredBlock<Block> XRAY_BLOCK = BLOCKS.registerSimpleBlock("xrayer", BlockBehaviour.Properties.of().instabreak().speedFactor(0.9f).noOcclusion());
    public static final DeferredItem<BlockItem> XRAY_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("xrayer", XRAY_BLOCK);
    public static final DeferredBlock<Block> REDTRAV = BLOCKS.registerSimpleBlock("redtrav", BlockBehaviour.Properties.of().sound(SoundType.WET_GRASS).instabreak().jumpFactor(2f).ignitedByLava().speedFactor(2.5f));
    public static final DeferredItem<BlockItem> REDTRAV_ITEM = ITEMS.registerSimpleBlockItem("redtrav", REDTRAV);
    public static final DeferredItem<Item> NICE_STICK = ITEMS.registerSimpleItem("nice_stick", new Item.Properties().setNoRepair().food(new FoodProperties.Builder().nutrition(0).saturationModifier(0f).usingConvertsTo(Items.STICK).build()));
    public static final DeferredItem<Item> HEART_FOOD = ITEMS.registerSimpleItem("food", new Item.Properties().stacksTo(16).fireResistant().food(new FoodProperties.Builder()
            .nutrition(19).fast().effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 324), 0.5f).saturationModifier(20f).usingConvertsTo(Items.BONE).alwaysEdible().build()));
    public static final DeferredItem<Item> NEW = ITEMS.registerSimpleItem("placeholder", new Item.Properties().stacksTo(5));
    public static final DeferredItem<Item> LEGENDS_FROST_MUSIC_DISC = ITEMS.registerSimpleItem("lfrost_music_disc", new Item.Properties().jukeboxPlayable(JukeboxSongs.CAT).stacksTo(1).rarity(Rarity.RARE));
    public static final DeferredItem<Item> POPSICLE_FOOD = ITEMS.registerSimpleItem("ice_food",
            new Item.Properties().rarity(Rarity.COMMON).food(new FoodProperties.Builder().alwaysEdible().nutrition(0).saturationModifier(1f).usingConvertsTo(Items.STICK)
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
            "new_sword", new Item.Properties().stacksTo(1).rarity(Rarity.EPIC).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 10, -3.6F)));
    public static final DeferredBlock<Block> STOOL = BLOCKS.register("stool", () -> new StoolBlock(
            BlockBehaviour.Properties.of()
            .noOcclusion().mapColor(MapColor.WOOD).ignitedByLava().strength(6f)));
    public static final DeferredItem<BlockItem> STOOL_ITEM = ITEMS.registerSimpleBlockItem("stool", STOOL);
    public static final DeferredBlock<Block>  TABLE = BLOCKS.register("table", () -> new TableBlock(BlockBehaviour.Properties.of().ignitedByLava().noOcclusion().sound(SoundType.WOOD).strength(5f).explosionResistance(8f).pushReaction(PushReaction.PUSH_ONLY)));
    public static final DeferredItem<BlockItem> TABLE_ITEM = ITEMS.registerSimpleBlockItem("table", TABLE, new Item.Properties().stacksTo(32));
    public static final DeferredItem<Item> FUEL = ITEMS.register("fuel", () -> new Item(new Item.Properties().rarity(Rarity.COMMON).stacksTo(32)));
    public static final DeferredBlock<Block> NEW_LAMP = BLOCKS.register("lamp", () -> new NewLampBlock(Block.Properties.of().explosionResistance(2f).strength(10f).sound(ModSounds.MAGIC_BLOCK_SOUNDS)
                    .lightLevel(state -> state.getValue(NewLampBlock.CLICKED) ? 15 : 0)));
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





//replace item on use causes error
    // Creates a creative tab with the id "explomod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.explomod.new")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS)
            .icon(() -> NEW.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(EXAMPLE_BLOCK_ITEM.get());
                output.accept(TABLE_ITEM.get());
                output.accept(LOG_BLOCK_ITEM.get());
                output.accept(STOOL_ITEM.get());
                output.accept(GREEN_POPSICLE.get());
                output.accept(WEAPON_ITEM.get());
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
                output.accept(BLUE_POPSICLE.get());
                output.accept(HEART_FOOD.get());
            }).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> FOOD_TAB = CREATIVE_MODE_TABS.register("food_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.explomod.custom")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.FUNCTIONAL_BLOCKS)
            .icon(() -> EXAMPLE_BLOCK_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(EXAMPLE_BLOCK_ITEM.get());
                output.accept(LOG_BLOCK_ITEM.get());
                output.accept(REDTRAV_ITEM.get());
                output.accept(HEART_FOOD.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public ExploMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);
        ModVillagers.register(modEventBus);
        ModSounds.register(modEventBus);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        ModLootModifiers.register(modEventBus);

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

        if (Config.BUTTON.getAsBoolean()) {
            LOGGER.info("The button is on!");
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(POPSICLE_FOOD);
            event.accept(YELLOW_POPSICLE);
            event.accept(BLUE_POPSICLE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        ChatLog.codec(6);
        if(Config.ITEM_STRINGS.get().contains("minecraft:iron_ingot")) {
            Minecraft.getInstance().gui.getChat().addMessage(Component.keybind("Player Joined"));
            MinecraftServer server = event.getServer(); // Or ServerLifecycleHooks.getCurrentServer()
            if (server != null) {
                int playerCount = server.getPlayerList().getPlayerCount() + 1;
                List<ServerPlayer> players = server.getPlayerList().getPlayers();
                System.out.println("Players online: " + playerCount);
                if (playerCount == 1) {
                    if (Config.ITEM_STRINGS.get().contains("explomod:kit")) {
                        Minecraft.getInstance().gui.getChat().addMessage(Component.translatable("text.single"));
                    } else {
                        Minecraft.getInstance().gui.getChat().addMessage(Component.nullToEmpty("Total players: " + playerCount));
                    }
                } else {
                    Minecraft.getInstance().gui.getChat().addMessage(Component.nullToEmpty("Total players: " + playerCount));
                }
            }
        }
    }

    @SubscribeEvent
    public void OnPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 34));
            Minecraft.getInstance().gui.getChat().addRecentChat("Welcome");
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Player player && !event.getLevel().isClientSide()) {
            if(!player.getPersistentData().getBoolean("started_with_items")) {
                if(Config.ITEM_STRINGS.get().contains("explomod:sground")){
            player.getInventory().add(new ItemStack(Items.STICK, 1));
                player.sendSystemMessage(Component.translatable("message.joining.text"));
            player.getPersistentData().putBoolean("started_with_items", true);}
        }
    }
}
    @SubscribeEvent
    public void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.MASON) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD_BLOCK, 8),
                    new ItemStack(ExploMod.EXAMPLE_BLOCK.get(), 1), 3, 5, 2.35f));
            trades.get(2).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 1),
                    new ItemStack(ExploMod.WEAPON_ITEM.get(), 1), 1, 1, 1.5f));
            if(Config.ITEM_STRINGS.get().contains("explomod:stoned")){
                trades.get(1).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 1),
                        new ItemStack(ExploMod.GROUND_ITEM.get(), 1), 1, 1, 1.5f));
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
                        new ItemCost(Items.EMERALD, 8),
                        new ItemStack(ExploMod.REDTRAV_ITEM.get(), 1), 1, 1, 8.87f));
            } else{
                trades.get(3).add((entity, randomSource) -> new MerchantOffer(
                        new ItemCost(Items.EMERALD, 18),
                        new ItemStack(ExploMod.REDTRAV_ITEM.get(), 1), 1, 1, 8.87f));
            }

            trades.get(5).add((entity, randomSource) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 14),
                    new ItemStack(ExploMod.NICE_STICK.get(), 1), 1, 5, 35.85f));
        }
    }
    @SubscribeEvent
    public void addWanderingTraders(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 8),
                new ItemStack(ExploMod.XRAY_BLOCK_ITEM.get(), 1), 3, 3, 5.81f));
        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                new ItemStack(Items.SUGAR, 6), 14, 1, 0.81f));
        genericTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 10),
                new ItemStack(ExploMod.LEGENDS_FROST_MUSIC_DISC.get(), 1), 1, 1, 1.81f));

        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(ExploMod.EXAMPLE_ITEM.get(), 1),
                new ItemStack(Items.EMERALD, 12), 3, 3, 1.81f));
        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 9),
                new ItemStack(ExploMod.POPSICLE_FOOD.get(), 1), 2, 3, 1.81f));
        rareTrades.add((entity, randomSource) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 1),
                new ItemStack(Items.ARROW, 3), 3, 3, 0.81f));
    }
}
