package com.example.explomod.item.enchantment;

import com.example.explomod.ExploMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;

public class Enchantments {
    public static final ResourceKey<Enchantment> DISTILL = key("distill", false);
    public static final ResourceKey<Enchantment> DULLNESS = key("dullness", true);

    private static ResourceKey<Enchantment> key(String name, boolean defaultNamespace) {
        if(defaultNamespace) {
            return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.withDefaultNamespace(name));
        }else{
            return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, name));
        }
    }
}
