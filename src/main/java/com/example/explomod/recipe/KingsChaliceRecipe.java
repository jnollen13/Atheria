package com.example.explomod.recipe;

import com.example.explomod.ExploMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class KingsChaliceRecipe extends CustomRecipe {

    public KingsChaliceRecipe(CraftingBookCategory category) {
        super(category);
    }

    public boolean matches(CraftingInput input, @NotNull Level level) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemstack2 = input.getItem(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof PotionItem) {
                    if (!itemstack1.isEmpty()) {
                        return false;
                    }

                    PotionContents potionContents = itemstack2.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);

                    if(potionContents==PotionContents.EMPTY){
                        return false;
                    }

                    itemstack1 = itemstack2;
                } else {
                    if (!itemstack2.is(ExploMod.KINGS_CHALICE.get())) {
                        return false;
                    }

                    if (!itemstack.isEmpty()) {
                        return false;
                    }

                    PotionContents potionContents = itemstack2.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
                    if (potionContents!=PotionContents.EMPTY) {
                        return false;
                    }

                    itemstack = itemstack2;
                }
            }
        }

        return !itemstack.isEmpty() && !itemstack1.isEmpty();
    }

    public @NotNull ItemStack assemble(CraftingInput input, HolderLookup.@NotNull Provider registries) {
        ItemStack itemstack = ItemStack.EMPTY;
        ItemStack itemstack1 = ItemStack.EMPTY;
        ItemStack enchantStack = ItemStack.EMPTY;

        for(int i = 0; i < input.size(); ++i) {
            ItemStack itemstack2 = input.getItem(i);
            if (!itemstack2.isEmpty()) {
                if (itemstack2.getItem() instanceof PotionItem) {
                    itemstack = itemstack2;
                } else if (itemstack2.is(ExploMod.KINGS_CHALICE.get())) {
                    itemstack1 = itemstack2.copy();
                    enchantStack=itemstack2;
                }
            }
        }

        if (itemstack1.isEmpty()) {
            return itemstack1;
        } else {
            itemstack1.set(DataComponents.POTION_CONTENTS, itemstack.get(DataComponents.POTION_CONTENTS));
            itemstack1.set(DataComponents.ENCHANTMENTS, enchantStack.get(DataComponents.ENCHANTMENTS));
            return itemstack1;
        }
    }


    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
        NonNullList<ItemStack> nonnulllist = NonNullList.withSize(input.size(), ItemStack.EMPTY);

        for(int i = 0; i < nonnulllist.size(); ++i) {
            ItemStack item = input.getItem(i);
            if (item.getItem() instanceof PotionItem) {
                nonnulllist.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return nonnulllist;
    }


    public boolean canCraftInDimensions(int width, int height) {
        return width >= 2 && height >= 2;
    }


    public @NotNull RecipeSerializer<?> getSerializer() {
        return AtheriaRecipeSerializer.KINGS_CHALICE.value();
    }
}
