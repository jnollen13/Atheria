package com.example.explomod.event;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.ModEntities;
import com.example.explomod.entity.client.*;
import com.example.explomod.entity.custom.*;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = ExploMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents{
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GeckoModel.LAYER_LOCATION, GeckoModel::createBodyLayer);
        event.registerLayerDefinition(StrangeArrowModel.LAYER_LOCATION, StrangeArrowModel::createBodyLayer);
        event.registerLayerDefinition(PhantomLizardModel.LAYER_LOCATION, PhantomLizardModel::createBodyLayer);
        event.registerLayerDefinition(TraderModel.LAYER_LOCATION, TraderModel::createBodyLayer);
        event.registerLayerDefinition(GrassGolemModel.LAYER_LOCATION, GrassGolemModel::createBodyLayer);
        event.registerLayerDefinition(GlowBatModel.LAYER_LOCATION, GlowBatModel::createBodyLayer);
        event.registerLayerDefinition(BossGrassGolemModel.LAYER_LOCATION, BossGrassGolemModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GECKO.get(), GeckoEntity.createAttributes().build());
        event.put(ModEntities.LEGGED_PHANTOM.get(), PhantomLizard.createAttributes().build());
        event.put(ModEntities.TRADER.get(), TraderEntity.createAttributes().build());
        event.put(ModEntities.GRASS_GOLEM.get(), GrassGolem.createAttributes().build());
        event.put(ModEntities.GLOW_BAT.get(), GlowBat.createAttributes().build());
        event.put(ModEntities.BOSS_GRASS_GOLEM.get(), BossGrassGolem.createAttributes().build());
    }


    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.GECKO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.GRASS_GOLEM.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.TRADER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AbstractVillager::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.GLOW_BAT.get(), SpawnPlacementTypes.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AmbientCreature::checkMobSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}