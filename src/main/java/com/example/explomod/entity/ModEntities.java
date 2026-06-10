package com.example.explomod.entity;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ExploMod.MODID);

    public static final Supplier<EntityType<GeckoEntity>> GECKO =
            ENTITY_TYPES.register("gecko", () -> EntityType.Builder.of(GeckoEntity::new, MobCategory.CREATURE)
                    .sized(0.5f, 0.23f).nameTagOffset(5f).build("gecko"));

    public static final Supplier<EntityType<PhantomLizard>> LEGGED_PHANTOM =
            ENTITY_TYPES.register("legged_phantom", () -> EntityType.Builder.of(PhantomLizard::new, MobCategory.CREATURE)
                    .sized(1.0f, 0.35f).nameTagOffset(5f).build("legged_phantom"));

    public static final Supplier<EntityType<TraderEntity>> TRADER =
            ENTITY_TYPES.register("trader", () -> EntityType.Builder.of(TraderEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 2f).nameTagOffset(3f).build("trader"));

    public static final Supplier<EntityType<GrassGolem>> GRASS_GOLEM =
            ENTITY_TYPES.register("grass_golem", () -> EntityType.Builder.of(GrassGolem::new, MobCategory.MONSTER)
                    .sized(0.9f, 1f).canSpawnFarFromPlayer().build("grass_golem"));

    public static final Supplier<EntityType<GlowBat>> GLOW_BAT =
            ENTITY_TYPES.register("glow_bat", () -> EntityType.Builder.of(GlowBat::new, MobCategory.AMBIENT)
                    .sized(0.9f, 1f).canSpawnFarFromPlayer().build("glow_bat"));

    public static final Supplier<EntityType<BossGrassGolem>> BOSS_GRASS_GOLEM =
            ENTITY_TYPES.register("boss_grass_golem", () -> EntityType.Builder.of(BossGrassGolem::new, MobCategory.MONSTER)
                    .sized(1.8f, 3.8f).canSpawnFarFromPlayer().build("boss_grass_golem"));

    public static final Supplier<EntityType<StrangeArrowEntity>> STRANGE_ARROW =
            ENTITY_TYPES.register("mysterious_arrow", () -> EntityType.Builder.<StrangeArrowEntity>of(StrangeArrowEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.25f).build("mysterious_arrow"));

    public static final Supplier<EntityType<RadiationBall>> RADIUM_BALL =
            ENTITY_TYPES.register("radium_orb", () -> EntityType.Builder.<RadiationBall>of(RadiationBall::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).clientTrackingRange(4).updateInterval(10).noSave().build("radium_orb"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
