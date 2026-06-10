package com.example.explomod.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Wyrm extends Animal {

    protected Wyrm(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * @param itemStack 
     * @return
     */
    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return false;
    }

    /**
     * @param serverLevel 
     * @param ageableMob
     * @return
     */
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
}
