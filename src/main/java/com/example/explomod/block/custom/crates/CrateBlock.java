package com.example.explomod.block.custom;

import com.example.explomod.ExploMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CrateBlock extends Block {
    public static final MapCodec<CrateBlock> CODEC = simpleCodec(CrateBlock::new);
    private static final VoxelShape FLOOR_AABB = Block.box(2.0, 0.0, 2.0, 14.0, 0.01, 14.0);
    private static final VoxelShape WALL1 = Block.box(14.0, 0.0, 1.0, 15.0, 11.0, 15.0);
    private static final VoxelShape WALL2 = Block.box(2.0, 0.0, 14.0, 14.0, 11.0, 15.0);
    private static final VoxelShape WALL3= Block.box(2.0, 0.0, 1.0, 14.0, 11.0, 2.0);
    private static final VoxelShape WALL4 = Block.box(1.0, 0.0, 1.0, 2.0, 11.0, 15.0);
    private static final VoxelShape SHAPE = Shapes.or(WALL1, WALL2, WALL3, WALL4, FLOOR_AABB);

    @Override
    public @NotNull MapCodec<CrateBlock> codec() {
        return CODEC;
    }

    public CrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        ItemStack o = player.getOffhandItem();
        ItemStack m = player.getMainHandItem();
        if((o.is(Items.EGG)&&m.is(Items.PAPER)||(o.is(Items.PAPER)&&m.is(Items.EGG)))){
            m.consume(1, player);
            o.consume(1, player);
            level.setBlock(pos, Objects.requireNonNull(ExploMod.CRATE.get().getStateForPlacement(new BlockPlaceContext(level, player, InteractionHand.MAIN_HAND, stack, hitResult))), 2);
            return ItemInteractionResult.SUCCESS;
        }else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }
}
