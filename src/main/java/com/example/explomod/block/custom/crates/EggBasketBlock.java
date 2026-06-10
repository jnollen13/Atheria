package com.example.explomod.block.custom.crates;

import com.example.explomod.ExploMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class EggBasketBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<EggBasketBlock> CODEC = simpleCodec(EggBasketBlock::new);
    public static final int MIN_EGGS = 0;
    public static final int MAX_EGGS = 8;
    public static final IntegerProperty EGGS = IntegerProperty.create("eggs", MIN_EGGS, MAX_EGGS);
    private static final VoxelShape FOUR_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 11.0, 15.0);

    @Override
    public @NotNull MapCodec<EggBasketBlock> codec() {
        return CODEC;
    }

    public EggBasketBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(EGGS, MIN_EGGS)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().getItem() == Items.EGG && state.getValue(EGGS) < MAX_EGGS || super.canBeReplaced(state, useContext);
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if(entity instanceof ItemEntity itemEntity) {
            if(itemEntity.getItem().getItem() == Items.EGG) {
                if(state.getValue(EGGS)<MAX_EGGS) {
                    int i = itemEntity.getItem().getCount();
                    if(itemEntity.getItem().getCount()+state.getValue(EGGS)>MAX_EGGS){
                        i=0;int j =state.getValue(EGGS);int b;
                        if(j<=MAX_EGGS){b=1;}else{b=-1;}
                        for (int a = state.getValue(EGGS); a != MAX_EGGS; a+=b) {i+=b;j+=b;}
                    }
                    itemEntity.getItem().setCount(itemEntity.getItem().getCount()-i);
                    level.setBlock(pos, state.setValue(EGGS, state.getValue(EGGS)+i),2);
                }
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        BlockPos loc = pos.below(1);
        BlockState locstate = level.getBlockState(loc);
        if(state.getValue(EGGS)>0) {
            if (locstate.hasProperty(EGGS)) {
                if (locstate.getValue(EGGS) < MAX_EGGS) {
                    state.setValue(EGGS, state.getValue(EGGS)-1);
                    locstate.setValue(EGGS, locstate.getValue(EGGS)+1);
                }
            }
        }
        super.randomTick(state, level, pos, random);
    }

    @Override
    public void onBlockStateChange(LevelReader level, BlockPos pos, BlockState oldState, BlockState state) {
        BlockPos loc = pos.below(1);
        BlockState locstate = level.getBlockState(loc);
        if(state.getValue(EGGS)>0) {
            if (locstate.hasProperty(EGGS)) {
                if (locstate.getValue(EGGS) < MAX_EGGS) {
                    state.setValue(EGGS, state.getValue(EGGS)-1);
                    locstate.setValue(EGGS, locstate.getValue(EGGS)+1);
                }
            }
        }
        super.onBlockStateChange(level, pos, oldState, state);
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        drop(state, level, pos, player, player.getMainHandItem().is(Items.SHEARS));
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void drop(BlockState state, Level level, BlockPos pos, Player player, Boolean isSheared){
        if(!state.getValue(EGGS).equals(0)&&!player.hasInfiniteMaterials()) {
            ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.EGG.getDefaultInstance().copyWithCount(state.getValue(EGGS)));
            level.addFreshEntity(itementity);
        }
        if(isSheared&&!player.hasInfiniteMaterials()){
            ItemEntity item1 = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.EGG.getDefaultInstance());
            ItemEntity item2 = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.PAPER.getDefaultInstance());
            level.addFreshEntity(item1);
            level.addFreshEntity(item2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
            return this.defaultBlockState().setValue(EGGS, 0).setValue(FACING, context.getHorizontalDirection().getClockWise(Direction.Axis.Y));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(stack.is(Items.EGG)&&state.getValue(EGGS)<MAX_EGGS){
            Integer i = state.getValue(EGGS)+1;
            player.causeFoodExhaustion(0.01f);
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(EGGS, i), 2);
            return ItemInteractionResult.SUCCESS;
        } else if (stack.is(Items.SHEARS)) {
            stack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(stack));
            level.setBlock(pos, ExploMod.EMPTY_CRATE.get().defaultBlockState(), 2);
            drop(state, level, pos, player, true);
            return ItemInteractionResult.SUCCESS;
        } else {
            if(!stack.is(Items.DEBUG_STICK)) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }else{
                return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
            }
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if(state.getValue(EGGS)>MIN_EGGS) {
            level.setBlock(pos, state.setValue(EGGS, state.getValue(EGGS) - 1), 2);
            if(!player.hasInfiniteMaterials()) {
                player.addItem(new ItemStack(Items.EGG));
            }
        }
            return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return FOUR_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EGGS);
        builder.add(FACING);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }
}
