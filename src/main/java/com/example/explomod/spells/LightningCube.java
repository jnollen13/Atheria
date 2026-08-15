package com.example.explomod.spells;

import com.example.explomod.Config;
import com.example.explomod.registries.LevelScaling;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LightningCube extends LevelingSpell{
    public LightningCube(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum, LevelScaling scaling, int maxLevel) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum, scaling, maxLevel);
    }

    @Override
    public void DoSpell(Level level, LivingEntity entity) {
        super.DoSpell(level, entity);
        AABB area = entity.getBoundingBox().inflate(getLevel(), getLevel(), getLevel());
        List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, area);
        for (int i = 0; targets.size()-1>Config.SERVER.lightning_cube_max_targets.get(); i++) {
            targets.removeLast();
        }
        targets.remove(entity);
        targets.forEach(livingEntity -> {
            if(livingEntity!=entity){
                notify(livingEntity, this.withLevel(getLevel()));
                LightningBolt lightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                lightning.setPos(livingEntity.position());
                lightning.setDamage(applyFloatLevelingCustomLinear(1.5f, 0.5f));
                level.addFreshEntity(lightning);
            }
        });
        if(entity instanceof Player player){
            player.displayClientMessage(Component.translatable("message.explomod.spell.lighting.hit", targets.size()).withStyle(ChatFormatting.AQUA), true);
        }
    }
}
