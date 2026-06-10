package com.example.explomod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BoomBlock extends Block {
    public BoomBlock(Properties properties) {
        super(properties);
    }

    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        Vec3 vec3 = pos.getCenter();
        level.explode(null, level.damageSources().magic(), null, vec3, 30.0F, false, Level.ExplosionInteraction.BLOCK);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        if (level.hasNeighborSignal(pos)) {
            Vec3 vec3 = pos.getCenter();
            level.explode(null, level.damageSources().campfire(), null, vec3, 11.2F, true, Level.ExplosionInteraction.BLOCK);
        }
    }

    protected void onProjectileHit(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            Vec3 vec3 = pos.getCenter();
            level.explode(null, level.damageSources().magic(), null, vec3, 14.2F, true, Level.ExplosionInteraction.BLOCK);
        }
    }

}
