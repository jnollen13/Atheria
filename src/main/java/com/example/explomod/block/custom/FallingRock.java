package com.example.explomod.block.custom;

import com.example.explomod.ExploMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FallingRock extends ColoredFallingBlock {
    private final VoxelShape shape;
    public FallingRock(ColorRGBA dustColor, VoxelShape shape, Properties properties) {
        super(dustColor, properties);
        this.shape=shape;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return shape;
    }

    protected boolean canSurvive(@NotNull BlockState p_304413_, @NotNull LevelReader p_304885_, BlockPos p_304808_) {
        return canSupportCenter(p_304885_, p_304808_.below(), Direction.UP);
    }

    @Override
    protected void onProjectileHit(@NotNull Level level, @NotNull BlockState state, @NotNull BlockHitResult hit, @NotNull Projectile projectile) {
        super.onProjectileHit(level, state, hit, projectile);
        if(this.asBlock()!=ExploMod.ROCK.get()) {
            level.setBlock(hit.getBlockPos(), Blocks.AIR.defaultBlockState(), 2);
            Vec3 location = hit.getLocation();
            ItemEntity dropItems = new ItemEntity(level, location.x(), location.y(), location.z(), new ItemStack(this.asItem()));
            level.addFreshEntity(dropItems);
        }
    }
}
