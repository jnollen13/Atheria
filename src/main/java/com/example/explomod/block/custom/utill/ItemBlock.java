package com.example.explomod.block.custom.utill;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ItemBlock extends Block {
    private ItemStack itemstack;

    public ItemBlock(ItemStack itemStack, BlockBehaviour.Properties properties) {
        super(properties);
        this.itemstack = itemstack;
    }

    public ItemStack getItem() {
        return this.itemstack;
    }
}