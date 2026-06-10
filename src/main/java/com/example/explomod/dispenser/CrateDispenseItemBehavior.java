package com.example.explomod.dispenser;

import com.example.explomod.ExploMod;
import com.example.explomod.block.custom.crates.EggBasketBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class CrateDispenseItemBehavior extends DefaultDispenseItemBehavior {
    private final DefaultDispenseItemBehavior defaultDispenseItemBehavior;

    public CrateDispenseItemBehavior(EggBasketBlock type) {
        this();
    }

    public CrateDispenseItemBehavior() {
        this.defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();
    }

    public @NotNull ItemStack execute(BlockSource blockSource, @NotNull ItemStack item) {

        Direction direction = blockSource.state().getValue(DispenserBlock.FACING);
        ServerLevel serverlevel = blockSource.level();
        BlockPos pos = blockSource.pos();
        BlockPos nextPos = pos.relative(direction);
        if (item.is(ExploMod.CRATE_ITEM)){
            if (serverlevel.getBlockState(nextPos).equals(Blocks.AIR.defaultBlockState())) {
                serverlevel.setBlock(nextPos, ExploMod.CRATE.get().defaultBlockState(), 2);
            }
        } else if (item.is(Items.EGG)) {
            if (serverlevel.getBlockState(nextPos).equals(ExploMod.CRATE.get().defaultBlockState().getValue(EggBasketBlock.EGGS))) {
                if(EggBasketBlock.EGGS.getValue("eggs").get()<EggBasketBlock.MAX_EGGS) {
                    serverlevel.setBlock(nextPos, ExploMod.CRATE.get().defaultBlockState().setValue(EggBasketBlock.EGGS, serverlevel.getBlockState(nextPos).getValue(EggBasketBlock.EGGS) + 1), 2);
                }
            }
        }
        item.shrink(1);
        return item;
    }

    protected void playSound(BlockSource blockSource) {
        blockSource.level().levelEvent(1000, blockSource.pos(), 0);
    }
}
