package com.example.explomod.worldgen.dimension;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.DimensionType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class DimensionTypes {
    public static final DeferredRegister<DimensionType> DIMENSION_TYPE = DeferredRegister.create(Registries.DIMENSION_TYPE, ExploMod.MODID);
    public static final ResourceLocation DARK_EFFECTS = ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "dark");
}
