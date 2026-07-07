package com.example.explomod.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public record LearnedSpells(List<ResourceLocation> unlockedItems) {
    public static final Codec<LearnedSpells> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ResourceLocation.CODEC.listOf().fieldOf("unlocked_items").forGetter(LearnedSpells::unlockedItems)
            ).apply(instance, LearnedSpells::new)
    );

    // Helper to safely unlock a brand new item without duplicating entries
    public LearnedSpells unlockSpell(Item item) {
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(item);
        List<ResourceLocation> updatedList = new ArrayList<>(this.unlockedItems);
        if (!updatedList.contains(itemId)) {
            updatedList.add(itemId);
        }
        return new LearnedSpells(updatedList);
    }

    // Check if the item is already unlocked (like checking if a recipe is known)
    public boolean knowsSpell(Item item) {
        return this.unlockedItems.contains(BuiltInRegistries.ITEM.getKey(item));
    }
}
