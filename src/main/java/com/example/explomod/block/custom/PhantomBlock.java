package com.example.explomod.block.custom;

import com.example.explomod.ExploMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import com.example.explomod.utill.ModTags;

public class PhantomBlock extends Block {

    public PhantomBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if(entity instanceof ItemEntity itemEntity) {
            if(isValidItem(itemEntity.getItem())) {
                itemEntity.setItem(new ItemStack(ExploMod.PHANTOM_INGOT.get(), itemEntity.getItem().getCount()));
            }

            if(itemEntity.getItem().getItem() == Items.ROSE_BUSH) {
                itemEntity.setItem(new ItemStack(Items.WITHER_ROSE, itemEntity.getItem().getCount()));
            }
            if(itemEntity.getItem().getItem() == Items.IRON_INGOT) {
                itemEntity.setItem(new ItemStack(ExploMod.PHANTOM_INGOT.get(), itemEntity.getItem().getCount()));
            }
        }

        super.stepOn(level, pos, state, entity);
    }
    private boolean isValidItem(ItemStack item) {
        return item.is(ModTags.Items.TRANSFORMABLE_ITEMS);
    }

    @Override
    public void fallOn(Level p_153362_, BlockState p_153363_, BlockPos p_153364_, Entity p_153365_, float p_153366_) {
        p_153365_.causeFallDamage(p_153366_, 0.001F, p_153362_.damageSources().fall());
    }
}
