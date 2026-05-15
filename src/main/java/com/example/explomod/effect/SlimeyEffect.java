package com.example.explomod.effect;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

import net.minecraft.world.entity.LivingEntity;

public class RocketEffect extends MobEffect {

    public RocketEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(LivingEntity livingEntity, int amplifier) {
        if(LivingEntity.getSlotForHand(InteractionHand.OFF_HAND).isArmor()){

        }

        return true;
    }
}
