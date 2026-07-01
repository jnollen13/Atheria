package com.example.explomod.stats;

import com.example.explomod.ExploMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatType;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AtheriaStats {
    public static final DeferredRegister<ResourceLocation> C_STATS = DeferredRegister.create(Registries.CUSTOM_STAT, ExploMod.MODID);
    public static final DeferredRegister<StatType<?>> T_STATS = DeferredRegister.create(Registries.STAT_TYPE, ExploMod.MODID);

    static final String pic = "placed_in_crate";
    static final String tioc = "taken_out_crate";
    static final String sjt = "hidden_stats";


    public static final DeferredHolder<ResourceLocation, ResourceLocation> PLACED_IN_CRATE =
            C_STATS.register(pic, () -> ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, pic));
    public static final DeferredHolder<ResourceLocation, ResourceLocation> TAKEN_OUT_CRATE =
            C_STATS.register(tioc, () -> ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, tioc));
    public static final DeferredHolder<ResourceLocation, ResourceLocation> SPELLS_CAST =
            C_STATS.register("spells_cast", () -> ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "spells_cast"));

    public static final DeferredHolder<StatType<?>, StatType<Item>> HIDDEN_STATS =
            T_STATS.register(sjt, () -> makeRegistryStatType(sjt, BuiltInRegistries.ITEM));


    public static <T> StatType<T> makeRegistryStatType(String name, Registry<T> registry) {
        Component component = Component.translatable("stat_type.explomod." + name);
        return Registry.register(BuiltInRegistries.STAT_TYPE, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name), new StatType<>(registry, component));
    }

    public static void register(IEventBus eventBus) {
        C_STATS.register(eventBus);
        T_STATS.register(eventBus);
    }
}
