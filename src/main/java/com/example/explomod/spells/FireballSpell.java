package com.example.explomod.spells;

import com.example.explomod.registries.LevelScaling;
import com.example.explomod.registries.Spell;
import com.example.explomod.stats.AtheriaStats;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FireballSpell extends LevelingSpell {
    public FireballSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum, scaling, maxLevel);
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        if(entity instanceof Player player){
            player.awardStat(AtheriaStats.SPELLS_CAST.get());
        }
        Vec3 lookDirection = entity.getLookAngle();
        if(this.getLevel()<2) {
            SmallFireball smallFireball = new SmallFireball(entity.level(), entity, new Vec3(lookDirection.x, lookDirection.y, lookDirection.z));
            smallFireball.setPos(entity.getX() + lookDirection.x, entity.getEyeY() + lookDirection.y, entity.getZ() + lookDirection.z);
            entity.level().addFreshEntity(smallFireball);
        }else {
            LargeFireball bigFireball = new LargeFireball(entity.level(), entity, new Vec3(lookDirection.x, lookDirection.y, lookDirection.z), this.getLevel()-1);
            bigFireball.setPos(entity.getX() + lookDirection.x, entity.getEyeY() + lookDirection.y, entity.getZ() + lookDirection.z);
            entity.level().addFreshEntity(bigFireball);
        }
    }

    @Override
    public int cooldownLeveling(int baseCooldown) {
        return applyIntLevelingAddMultiplied(baseCooldown);
    }
}
