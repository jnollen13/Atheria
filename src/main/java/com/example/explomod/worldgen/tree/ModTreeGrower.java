package com.example.explomod.worldgen.tree;

import com.example.explomod.ExploMod;
import net.minecraft.world.level.block.grower.TreeGrower;
import com.example.explomod.worldgen.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrower {
    public static final TreeGrower BLOODWOOD = new TreeGrower(ExploMod.MODID + ":bloodwood",
            Optional.empty(), Optional.of(ModConfiguredFeatures.BLOODWOOD_KEY), Optional.empty());

    public static final TreeGrower WINTER_OAK = new TreeGrower(ExploMod.MODID + ":winter_oak",
            Optional.empty(), Optional.of(ModConfiguredFeatures.WINTER_OAK_KEY), Optional.empty());

    public static final TreeGrower LARGE_DARK_OAK = new TreeGrower(ExploMod.MODID + ":large_dark_oak",
            Optional.empty(), Optional.of(ModConfiguredFeatures.DARK_OAK_KEY), Optional.empty());
}
