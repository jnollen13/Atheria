package com.example.explomod.worldgen.structure_dont_use;

import com.example.explomod.ExploMod;
import com.example.explomod.worldgen.structure_dont_use.structures.DarkPortalStructure;
import com.example.explomod.worldgen.structure_dont_use.structures.VenderStallStructure;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public interface ModStructureType <S extends Structure> {
    DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, ExploMod.MODID);

    Holder<StructureType<?>> DARK_PORTAL = STRUCTURE_TYPES.register("dark_portal", () -> () -> DarkPortalStructure.CODEC);
    Holder<StructureType<?>> VENDOR_STALL = STRUCTURE_TYPES.register("vendor_stall", () -> () -> VenderStallStructure.CODEC);

    static void register(IEventBus eventBus) {
        STRUCTURE_TYPES.register(eventBus);
    }
}
