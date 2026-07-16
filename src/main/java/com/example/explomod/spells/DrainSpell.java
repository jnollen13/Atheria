package com.example.explomod.spells;

import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.Mana;
import com.example.explomod.payloads.SyncManaPayload;
import com.example.explomod.registries.Spell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DrainSpell extends Spell {

    private final boolean healthOmana;

    public DrainSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, int manaUsage, int minimum, boolean mana) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
        healthOmana = mana;
    }

    public DrainSpell(@Nullable String name, @NotNull Item saveTo, int cooldownDuration, float manaUsage, int minimum, boolean mana) {
        super(name, saveTo, cooldownDuration, manaUsage, minimum);
        healthOmana = mana;
    }

    @Override
    protected void DoIntegratedSpell(Level level, LivingEntity caster, LivingEntity target) {
        if(!level.isClientSide()) {
            if (this.healthOmana) {
                float targetMana = target.getData(AtheriaDataAttachments.MANA).mana();
                float casterMana = caster.getData(AtheriaDataAttachments.MANA).mana();
                float maxMana = 100f;
                float manaNeeded = maxMana - casterMana;
                if (manaNeeded >= 0) {
                    int randomDivider = RandomSource.create().nextIntBetweenInclusive(1, 15);
                    float apply = manaNeeded / randomDivider;
                    caster.setData(AtheriaDataAttachments.MANA, new Mana(casterMana + apply));
                    target.setData(AtheriaDataAttachments.MANA, new Mana(targetMana - apply));
                    if (target instanceof ServerPlayer serverPlayer) {
                        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                                serverPlayer,
                                new SyncManaPayload(targetMana - apply)
                        );
                    }
                    if (caster instanceof ServerPlayer serverPlayer) {
                        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                                serverPlayer,
                                new SyncManaPayload(casterMana + apply)
                        );
                    }
                }
            } else {
                float targetHealth = target.getHealth();
                float casterHealth = caster.getHealth();
                float maxCasterHealth = caster.getMaxHealth();
                float healNeeded = maxCasterHealth - casterHealth;
                if (healNeeded != 0) {
                    int randomDivider = RandomSource.create().nextIntBetweenInclusive(1, 15);
                    float apply;
                    if ((targetHealth - healNeeded) >= 0) {
                        apply = healNeeded / randomDivider;
                    } else {
                        apply = targetHealth / randomDivider;
                    }
                    caster.heal(apply);
                    target.hurt(level.damageSources().source(DamageTypes.MAGIC, caster), apply);
                }
            }
        }
    }
}
