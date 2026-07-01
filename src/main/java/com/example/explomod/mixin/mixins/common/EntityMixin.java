package com.example.explomod.mixin.mixins.common;

import com.example.explomod.Config;
import com.example.explomod.worldgen.AtheriaLevelUtill;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Entity.class)
public class EntityMixin {
    /**
     * Handles entities falling out of the Dark. If an entity is not a player, vehicle, or tracked item, it is removed.
     *
     */
    @Inject(at = @At(value = "TAIL"), method = "tick()V")
    private void travel(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        Level level = entity.level();
        if (level instanceof ServerLevel serverLevel) {
            if (false) {
                if (serverLevel.dimension() == AtheriaLevelUtill.destinationDimension()) {
                    if (entity.getY() <= -32 && !entity.isPassenger()) {
                        if (entity instanceof Player || entity.isVehicle() || (entity instanceof Saddleable) && ((Saddleable) entity).isSaddled()) { // Checks if an entity is a player or a vehicle of a player.
                            entityFell(entity);
                        } else if (entity instanceof Projectile projectile && projectile.getOwner() instanceof Player) {
                            entityFell(projectile);
                        } else if (entity instanceof ItemEntity itemEntity) {
                            entityFell(itemEntity);
                        }
                    }
                }
            }
        }
    }

    @Unique
    @Nullable
    private static Entity entityFell(Entity entity) {
        Level serverLevel = entity.level();
        MinecraftServer minecraftserver = serverLevel.getServer();
        if (minecraftserver != null) {
            ServerLevel destination = minecraftserver.getLevel(AtheriaLevelUtill.fallDimension());
            if (destination != null && AtheriaLevelUtill.fallDimension() != AtheriaLevelUtill.destinationDimension()) {
                serverLevel.getProfiler().push("dark_fall");
                entity.setPortalCooldown();
                double vehicleOffset = 0.0;
                if (entity.getVehicle() != null) {
                    vehicleOffset = entity.getVehicle().getBbHeight();
                }
                DimensionTransition transition = new DimensionTransition(destination, new Vec3(entity.getX(), destination.getMaxBuildHeight()+64, entity.getZ()), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(), false, DimensionTransition.PLAY_PORTAL_SOUND);
                Entity target = entity.changeDimension(transition);
                serverLevel.getProfiler().pop();
                return target;
            }
        }
        return null;
    }
}
