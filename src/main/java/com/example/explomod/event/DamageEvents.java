package com.example.explomod.event;


import com.example.explomod.Config;
import com.example.explomod.ExploMod;
import com.example.explomod.utill.ModTags;
import com.example.explomod.worldgen.AtheriaLevelUtill;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = ExploMod.MODID, bus = EventBusSubscriber.Bus.GAME)
public class DamageEvents {

    @SubscribeEvent
    public static void onEntityDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if(Config.SERVER.fallVoidDark.get()) {
            // Ensures this logic runs strictly server-side
            if (level.isClientSide()) return;
            // Check if the entity is taking damage from falling into the void
            if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) {
                ServerLevel currentLevel = (ServerLevel) level;
                // Define Origin and Target Dimension ResourceKeys
                ResourceKey<Level> originDim = AtheriaLevelUtill.destinationDimension(); // source dimension
                ResourceKey<Level> targetDim = AtheriaLevelUtill.fallDimension(); // target dimension
                if (currentLevel.dimension() == originDim) {
                    // Cancel the void damage entirely
                    event.setCanceled(true);
                    // Fetches the destination server world instance
                    ServerLevel destinationWorld = currentLevel.getServer().getLevel(targetDim);
                    if (destinationWorld != null) {
                        // Configure the entry position in the new dimension
                        // matches the X and Z coords, but set Y 4 blocks below the build limit
                        double targetY = destinationWorld.getMaxBuildHeight() - 4;
                        Vec3 spawnPos = new Vec3(entity.getX(), targetY, entity.getZ());
                        // Maintain current falling/movement momentum
                        Vec3 currentVelocity = entity.getDeltaMovement();
                        // make the dimension transition
                        DimensionTransition transition = new DimensionTransition(
                                destinationWorld,
                                spawnPos,
                                currentVelocity,
                                entity.getYRot(),
                                entity.getXRot(),
                                DimensionTransition.DO_NOTHING // Normal teleport transition behavior
                        );
                        // Execute the dimensional cross-over smoothly
                        entity.changeDimension(transition);
                    }
                }
            }
        }
    }
}
