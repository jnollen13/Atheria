package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireballSpell extends Spell {

    public FireballSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
    }
}
