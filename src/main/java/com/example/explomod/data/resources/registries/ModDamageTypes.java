package com.example.explomod.data.resources.registries;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ModDamageTypes {
    public static final ResourceKey<DamageType> CRUSH = createKey("crush");
    public static final ResourceKey<DamageType> BURN = createKey("burn");
    public static final ResourceKey<DamageType> ICE_CRYSTAL = createKey("ice_crystal");
    public static final ResourceKey<DamageType> OVERHEAT = createKey("overheat");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(CRUSH, new DamageType("explomod.crush", 0.1F));
        context.register(BURN, new DamageType("explomod.burn", 0.1F, DamageEffects.BURNING));
        context.register(ICE_CRYSTAL, new DamageType("explomod.ice_crystal", 0.1F, DamageEffects.FREEZING));
        context.register(OVERHEAT, new DamageType("explomod.overheat", 0.1F, DamageEffects.BURNING));
    }

    private static ResourceKey<DamageType> createKey(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
    }

    public static DamageSource damageSource(Level level, ResourceKey<DamageType> key) {
        return new DamageSource(level.holderOrThrow(key));
    }

    public static DamageSource entityDamageSource(Level level, ResourceKey<DamageType> key, @Nullable Entity entity) {
        return new DamageSource(level.holderOrThrow(key), entity);
    }

    public static DamageSource indirectEntityDamageSource(Level level, ResourceKey<DamageType> key, @Nullable Entity source, @Nullable Entity trueSource) {
        return new DamageSource(level.holderOrThrow(key), source, trueSource);
    }
}
