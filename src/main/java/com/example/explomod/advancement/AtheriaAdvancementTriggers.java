package com.example.explomod.advancement;

import com.example.explomod.ExploMod;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtheriaAdvancementTriggers {
    private static final DeferredRegister<CriterionTrigger<?>> ADVANCEMENT_TRIGGERS = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, ExploMod.MODID);

    public static final DeferredHolder<CriterionTrigger<?>, StatTrigger> STAT = ADVANCEMENT_TRIGGERS.register("item_use_stat", StatTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, BreakStatTrigger> BREAK_STAT = ADVANCEMENT_TRIGGERS.register("item_break_stat", BreakStatTrigger::new);

    public static void register(IEventBus eventBus) {
        ADVANCEMENT_TRIGGERS.register(eventBus);
    }
}
