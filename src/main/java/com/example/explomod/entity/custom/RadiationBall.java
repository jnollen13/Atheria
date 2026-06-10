package com.example.explomod.entity.custom;

import com.example.explomod.entity.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.SimpleExplosionDamageCalculator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public class RadiationBall extends AbstractWindCharge {
    private static final ExplosionDamageCalculator EXPLOSION_DAMAGE_CALCULATOR;
    private static final float RADIUS = 1.2F;
    private int noDeflectTicks = 5;

    public RadiationBall(EntityType<? extends AbstractWindCharge> entityType, Level level) {
        super(entityType, level);
    }

    public RadiationBall(Player player, Level level, double x, double y, double z) {
        super(EntityType.WIND_CHARGE, level, player, x, y, z);
    }

    public RadiationBall(Level level, Entity owner, double y, double z, Vec3 movement) {
        super(EntityType.WIND_CHARGE, level, owner, z, y, z);
    }

    public void tick() {
        super.tick();
        if (this.noDeflectTicks > 0) {
            --this.noDeflectTicks;
        }

    }

    public boolean deflect(@NotNull ProjectileDeflection deflection, @Nullable Entity entity, @Nullable Entity owner, boolean deflectedByPlayer) {
        return this.noDeflectTicks <= 0 && super.deflect(deflection, entity, owner, deflectedByPlayer);
    }

    protected void explode(Vec3 pos) {
        this.level().explode(this, null, EXPLOSION_DAMAGE_CALCULATOR, pos.x(), pos.y(), pos.z(), 3.2F, true, Level.ExplosionInteraction.TRIGGER, ParticleTypes.GUST_EMITTER_SMALL, ParticleTypes.EXPLOSION_EMITTER, SoundEvents.WIND_CHARGE_BURST);
    }

    static {
        EXPLOSION_DAMAGE_CALCULATOR = new SimpleExplosionDamageCalculator(true, true, Optional.of(10.22F), BuiltInRegistries.BLOCK.getTag(BlockTags.BANNERS).map(Function.identity()));
    }
}