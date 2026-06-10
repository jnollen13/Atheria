package com.example.explomod.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public class ExplosionEffect extends MobEffect {
    protected ExplosionEffect(MobEffectCategory category, int color) {
        super(category, color, ParticleTypes.EXPLOSION);
    }
    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if(livingEntity.isShiftKeyDown() || livingEntity.isSteppingCarefully() || livingEntity.isSuppressingSlidingDownLadder() || livingEntity.isSleeping() || livingEntity.isCrouching()) {
            livingEntity.level().explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), (float)3.5, Level.ExplosionInteraction.MOB);
            spawnLingeringCloud(livingEntity);
            livingEntity.setSilent(true);
            return true;

        }
        return super.applyEffectTick(livingEntity, amplifier);
    }

    private void spawnLingeringCloud(LivingEntity livingEntity) {
        Collection<MobEffectInstance> collection = livingEntity.getActiveEffects();
        if (!collection.isEmpty()) {
            AreaEffectCloud areaeffectcloud = new AreaEffectCloud(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            areaeffectcloud.setRadius(0.5F);
            areaeffectcloud.setRadiusOnUse(-0.1F);
            areaeffectcloud.setWaitTime(2);
            areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
            areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
            livingEntity.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
            livingEntity.animateHurt(9f);
            for(MobEffectInstance mobeffectinstance : collection) {
                areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
            }

            livingEntity.level().addFreshEntity(areaeffectcloud);
        }
        livingEntity.playSound(SoundEvents.CREEPER_HURT, 1.0F, 0.5F);
    }

    @Override
    public void onMobRemoved(LivingEntity livingEntity, int p_338875_, Entity.RemovalReason p_338258_) {
        if (p_338258_ == Entity.RemovalReason.KILLED && livingEntity.level() instanceof ServerLevel serverlevel) {
            livingEntity.level().explode(livingEntity, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),(float)2, true, Level.ExplosionInteraction.MOB);
            livingEntity.playSound(SoundEvents.CREEPER_PRIMED, 2.5F, 0.5F);
        }
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

}
