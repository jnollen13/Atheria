package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import net.minecraft.core.particles.ParticleTypes;
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
        if(target.getTags().contains("summoned")){
            Collection<ItemEntity> items = target.captureDrops();
            level.addParticle(ParticleTypes.POOF, true, target.getX(), target.getY(), target.getZ(), 0, 0, 0);
            target.remove(Entity.RemovalReason.DISCARDED);
            assert items != null;
            items.forEach(level::addFreshEntity);
        }
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        level.addParticle(ParticleTypes.POOF, true, entity.getX(), entity.getY(), entity.getZ(), 0.0, 0, 0.0);
        level.addParticle(ParticleTypes.POOF, true, entity.getX(), entity.getY(), entity.getZ(), 0.0, 0, 0.0);
    }
}
