package com.example.explomod.triggers;

import com.example.explomod.ExploMod;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtheriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, ExploMod.MODID);

    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }
}
