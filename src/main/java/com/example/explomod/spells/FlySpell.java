package com.example.explomod.spells;

import com.example.explomod.registries.Spell;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
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
    public void DoSpell(ServerPlayer player) {
        player.setDeltaMovement(0, 136.24, 0);
        player.hurtMarked = true;
    }
}
