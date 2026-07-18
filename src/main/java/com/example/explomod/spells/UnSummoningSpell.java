package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class UnSummoningSpell extends Spell {
    public UnSummoningSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, int minimum) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
    }

    public UnSummoningSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage) {
        super(name, saveTo, cooldownDuration, manaUsage);
    }

    @Override
    public void DoTargetedSpell(Level level, LivingEntity target) {
        particles(level, target);
        if(target.getTags().contains("summoned")){
            Collection<ItemEntity> items = target.captureDrops();
            level.addParticle(ParticleTypes.POOF, true, target.getX(), target.getY(), target.getZ(), 0, 0, 0);
            target.remove(Entity.RemovalReason.DISCARDED);
            assert items != null;
            items.forEach(level::addFreshEntity);
        }
    }

    public void particles(Level level, LivingEntity entity){
        for (int r = 0; r < 121; r++) {
            double rand1 = RandomSource.create().nextDouble();
            double rand2 = RandomSource.create().nextDouble();
            level.addParticle(ParticleTypes.WHITE_SMOKE, true, entity.getX()+rand1, entity.getY() + 1, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.POOF, true, entity.getX()+rand1, entity.getY() + 1, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.POOF, true, entity.getX()+rand2, entity.getY() + 1, entity.getZ()+rand1, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.ASH, true, entity.getX()+rand1, entity.getY() + 1, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.ANGRY_VILLAGER, true, entity.getX(), entity.getY() + 1.09, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.CLOUD, true, entity.getX()+rand1, entity.getY() + 1, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.SMOKE, true, entity.getX()+rand1, entity.getY() + 1, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, true, entity.getX()+rand1, entity.getY() + 1.07, entity.getZ()+rand2, 0.0, 0, 0.0);
            level.addParticle(ParticleTypes.LARGE_SMOKE, true, entity.getX()+rand1, entity.getY() + 1, entity.getZ()+rand2, 0.0, 0, 0.0);
        }
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        particles(level, entity);
    }
}
