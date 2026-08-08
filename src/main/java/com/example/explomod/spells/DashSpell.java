package com.example.explomod.spells;

import com.example.explomod.particle.ModParticles;
import com.example.explomod.registries.Spell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DashSpell extends Spell {
    public DashSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, int minimum) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
    }

    public DashSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        Vec3 angle = entity.getLookAngle();
        entity.setDeltaMovement(angle.x *0.75, angle.y *0.25, angle.z *0.75);
        entity.hurtMarked=true;
        for (int i = 0; i < 31; i++) {
            double randx = RandomSource.create().nextDouble();
            double randy = RandomSource.create().nextDouble();
            double randy2 = RandomSource.create().nextDouble();
            double randz = RandomSource.create().nextDouble();
            if(RandomSource.create().nextIntBetweenInclusive(1, 10)==2||RandomSource.create().nextIntBetweenInclusive(0, 25)==1){
                level.addParticle(ParticleTypes.ELECTRIC_SPARK, entity.getX() + randx, entity.getY() + randy2 + randy, entity.getZ() + randz, 0, 0, 0);
            }else {
                level.addParticle(ModParticles.RUSH.get(), entity.getX() + randx, entity.getY() + randy2 + randy, entity.getZ() + randz, 0, -1, 0);
            }
        }
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 58, 1, true, true, false));
    }
}
