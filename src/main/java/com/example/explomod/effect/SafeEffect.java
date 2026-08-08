package com.example.explomod.effect;

import com.example.explomod.particle.ModParticles;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.windcharge.AbstractWindCharge;
import net.minecraft.world.level.Level;

public class SafeEffect extends MobEffect {
    protected SafeEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.isSleeping() || livingEntity.isOnFire()) {
            double X = livingEntity.getX();
            double Y = livingEntity.getY() + (double) (livingEntity.getBbHeight() / 2.0F);
            double Z = livingEntity.getZ();
            if(livingEntity.isSleeping()){
            livingEntity.level().explode(
                    livingEntity,
                    null,
                    AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR,
                    X,
                    Y,
                    Z,
                    16,
                    false,
                    Level.ExplosionInteraction.TRIGGER,
                    ParticleTypes.GUST_EMITTER_SMALL,
                    ModParticles.SAFETY_PARTICLES.get(),
                    SoundEvents.MUSIC_END
            );}
            if(livingEntity.isOnFire()){
                livingEntity.level().explode(
                        livingEntity,
                        null,
                        AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR,
                        X,
                        Y,
                        Z, 4,
                        false,
                        Level.ExplosionInteraction.TRIGGER,
                        ParticleTypes.EXPLOSION_EMITTER,
                        ModParticles.SAFETY_PARTICLES.get(),
                        SoundEvents.MUSIC_END
                );
            }
        }
        if(livingEntity.isCrouching()){
            livingEntity.level().explode(livingEntity, null, AbstractWindCharge.EXPLOSION_DAMAGE_CALCULATOR, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), 2, false, Level.ExplosionInteraction.TRIGGER, ParticleTypes.UNDERWATER, ModParticles.SAFETY_PARTICLES.get(), SoundEvents.MUSIC_GAME);
        }
        return true;
    }



    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
