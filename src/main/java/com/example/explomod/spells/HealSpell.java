package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HealSpell extends Spell {
    private final float heal;
    public HealSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, int minimum, float healAmount) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
        this.heal=healAmount;
    }

    public HealSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, float healAmount) {
        super(name, saveTo, cooldownDuration, manaUsage);
        this.heal=healAmount;
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        entity.makeSound(SoundEvents.AMETHYST_BLOCK_CHIME);
        if(entity.isInvertedHealAndHarm()){
            entity.hurt(level.damageSources().magic(), heal);
        }else{
            entity.heal(this.heal);
        }
    }

    @Override
    public void DoTargetedSpell(Level level, LivingEntity target) {
        target.makeSound(SoundEvents.AMETHYST_BLOCK_CHIME);
        if(target.isInvertedHealAndHarm()){
            target.hurt(level.damageSources().magic(), heal);
        }else{
            target.heal(this.heal);
        }
    }

    @Override
    protected void DoIntegratedSpell(Level level, LivingEntity caster, LivingEntity target) {
        DoSpell(level, caster);
        DoTargetedSpell(level, target);
    }
}
