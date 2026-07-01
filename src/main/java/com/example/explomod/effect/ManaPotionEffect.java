package com.example.explomod.effect;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.payloads.SyncManaPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ManaPotionEffect extends InstantenousMobEffect {
    private final boolean isHarm;

    public ManaPotionEffect(MobEffectCategory category, int color, boolean isHarm) {
        super(category, color);
        this.isHarm = isHarm;
    }

    public boolean applyEffectTick(@NotNull LivingEntity livingEntity, int amplifier) {
        if (!this.isHarm) {
            if(livingEntity.getData(AtheriaDataAttachments.MANA).mana()<100) {
                livingEntity.setData(AtheriaDataAttachments.MANA, new Mana(livingEntity.getData(AtheriaDataAttachments.MANA).mana() + (3 * ((float) amplifier / 2)) + 0.5f));
                if (livingEntity instanceof ServerPlayer player) {
                    net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                            player,
                            new SyncManaPayload(player.getData(AtheriaDataAttachments.MANA).mana() + (3 * ((float) amplifier / 2)) + 0.5f)
                    );
                }
            }
        } else {
            livingEntity.setData(AtheriaDataAttachments.MANA, new Mana((float) (livingEntity.getData(AtheriaDataAttachments.MANA).mana()-1.5*amplifier)));

        }
        return true;
    }

    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {
        if (!this.isHarm) {
            if(livingEntity.getData(AtheriaDataAttachments.MANA).mana()<100) {
                livingEntity.setData(AtheriaDataAttachments.MANA, new Mana(livingEntity.getData(AtheriaDataAttachments.MANA).mana() + (3 * ((float) amplifier / 2)) + 0.5f));
            }
        } else {
            livingEntity.setData(AtheriaDataAttachments.MANA, new Mana((float) (livingEntity.getData(AtheriaDataAttachments.MANA).mana()-1.5*amplifier)));
        }

    }
}