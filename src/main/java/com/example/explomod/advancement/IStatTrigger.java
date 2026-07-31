package com.example.explomod.advancement;

import net.minecraft.server.level.ServerPlayer;

public interface IStatTrigger extends ITrigger {
    void trigger(ServerPlayer player);
}
