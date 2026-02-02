package com.example.explomod.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class StoolBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<StoolBlock> CODEC = simpleCodec(StoolBlock::new);
    private static final VoxelShape LEG1 = Block.box(1.0, 0.0, 1.0, 3.0, 16.0, 3.0);
    private static final VoxelShape LEG2 = Block.box(13.0, 0.0, 1.0, 15.0, 16.0, 3.0);
    private static final VoxelShape LEG3 = Block.box(13.0, 0.0, 13.0, 15.0, 16.0, 15.0);
    private static final VoxelShape LEG4 = Block.box(1.0, 0.0, 13.0, 3.0, 16.0, 15.0);
    private static final VoxelShape TOP = Block.box(0.0, 16.0, 0.0, 16.0, 17.0, 16.0);
    private static final VoxelShape LEGS = Shapes.or(LEG1, LEG2, LEG3, LEG4);
    private static final VoxelShape SUPPORT1 = Block.box(3.0, 5.0, 14.0, 13.0, 6.0, 15.0);
    private static final VoxelShape SUPPORT2 = Block.box(3.0, 5.0, 1.0, 13.0, 6.0, 2.0);
    private static final VoxelShape SUPPORT3 = Block.box(3.0, 12.0, 2.0, 13.0, 13.0, 3.0);
    private static final VoxelShape SUPPORT4 = Block.box(3.0, 12.0, 13.0, 13.0, 13.0, 14.0);
    private static final VoxelShape SUPPORT5 = Block.box(2.0, 11.0, 3.0, 3.0, 12.0, 13.0);
    private static final VoxelShape SUPPORT6 = Block.box(13.0, 11.0, 3.0, 14.0, 12.0, 13.0);
    private static final VoxelShape SUPPORT7 = Block.box(1.0, 4.0, 3.0, 2.0, 5.0, 13.0);
    private static final VoxelShape SUPPORT8 = Block.box(14.0, 4.0, 3.0, 15.0, 5.0, 13.0);
    private static final VoxelShape SUPPORTS = Shapes.or(SUPPORT1, SUPPORT2, SUPPORT3, SUPPORT4, SUPPORT5, SUPPORT6, SUPPORT7, SUPPORT8);
    private static final VoxelShape SHAPE = Shapes.or(SUPPORTS, TOP, LEGS);

    public StoolBlock(Properties properties) {
        super(properties);
    }
    
    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
