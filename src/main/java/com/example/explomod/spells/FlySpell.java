package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlySpell extends Spell {

    public FlySpell(@Nullable String name, @NotNull Item saveTo) {
        super(name, saveTo);
    }

    public FlySpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration) {
        super(name, saveTo, cooldownDuration);
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        entity.setDeltaMovement(0, 13.16, 0);
        entity.hurtMarked = true;
        entity.fallDistance = 0.0f;
        entity.makeSound(SoundEvents.VILLAGER_HURT);
    }
}
