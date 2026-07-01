package com.example.explomod.registries;

import com.example.explomod.ExploMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ABuiltInRegistries {
    public static final DeferredRegister<? extends Registry<?>> REGISTRIES = DeferredRegister.create(BuiltInRegistries.REGISTRY, ExploMod.MODID);


}
