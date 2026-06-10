package com.example.explomod.block.custom.crates;

import com.example.explomod.ExploMod;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.attachment.AttachmentType;
import org.jetbrains.annotations.NotNull;

public class SugarCaneCrateBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<SugarCaneCrateBlock> CODEC = simpleCodec(SugarCaneCrateBlock::new);
    public static final int MIN = 0;
    public static final int MAX = 14;
    public static final IntegerProperty CANE = IntegerProperty.create("sugar_cane", MIN, MAX);
    private static final VoxelShape BOX_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 11.0, 15.0);

    @Override
    public @NotNull MapCodec<SugarCaneCrateBlock> codec() {
        return CODEC;
    }

    public SugarCaneCrateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(CANE, MIN)
                        .setValue(FACING, Direction.NORTH)
        );
    }

    @Override
    protected boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext useContext) {
        return !useContext.isSecondaryUseActive() && useContext.getItemInHand().getItem() == Items.SUGAR_CANE && state.getValue(CANE) < MAX || super.canBeReplaced(state, useContext);
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if(entity instanceof ItemEntity itemEntity) {
            if(itemEntity.getItem().getItem() == Items.SUGAR_CANE) {
                if(state.getValue(CANE)<MAX) {
                    int i = itemEntity.getItem().getCount();
                    if(itemEntity.getItem().getCount()+state.getValue(CANE)>MAX){
                        i=0;int j =state.getValue(CANE);int b;
                        if(j<=MAX){b=1;}else{b=-1;}
                        for (int a = state.getValue(CANE); a != MAX; a+=b) {i+=b;j+=b;}
                    }
                    itemEntity.getItem().setCount(itemEntity.getItem().getCount()-i);
                    level.setBlock(pos, state.setValue(CANE, state.getValue(CANE)+i),2);
                }
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public boolean onDestroyedByPlayer(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, boolean willHarvest, @NotNull FluidState fluid) {
        drop(state, level, pos, player, player.getMainHandItem().is(Items.SHEARS));
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    public void drop(BlockState state, Level level, BlockPos pos, Player player, Boolean isSheared){
        if(!state.getValue(CANE).equals(0)&&!player.hasInfiniteMaterials()) {
            ItemEntity itementity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.SUGAR_CANE.getDefaultInstance().copyWithCount(state.getValue(CANE)));
            level.addFreshEntity(itementity);
        }
        if(isSheared&&!player.hasInfiniteMaterials()){
            ItemEntity item1 = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.SUGAR_CANE.getDefaultInstance());
            ItemEntity item2 = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), Items.PAPER.getDefaultInstance());
            level.addFreshEntity(item1);
            level.addFreshEntity(item2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(CANE, 0).setValue(FACING, context.getHorizontalDirection().getClockWise(Direction.Axis.Y));
    }

    protected void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
        boolean flag = level.hasNeighborSignal(pos) || level.hasNeighborSignal(pos.above())||level.hasNeighborSignal(pos.below());
        if (flag) {
            level.scheduleTick(pos, this, 4);
            Player player = level.getNearestPlayer(TargetingConditions.DEFAULT, pos.getX(), pos.getY(), pos.getZ());
            drop(level.getBlockState(pos), level, pos, player,  false);
            level.setBlock(pos, state.getBlock().defaultBlockState().setValue(FACING, state.getValue(FACING)).setValue(CANE, 0), 2);
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(stack.is(Items.SUGAR_CANE)&&state.getValue(CANE)<MAX){
            Integer i = state.getValue(CANE)+1;
            player.causeFoodExhaustion(0.01f);
            stack.consume(1, player);
            level.setBlock(pos, state.setValue(CANE, i), 2);
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
        if(state.getValue(CANE)>MIN) {
            level.setBlock(pos, state.setValue(CANE, state.getValue(CANE) - 1), 2);
            if(!player.hasInfiniteMaterials()) {
                player.addItem(new ItemStack(Items.SUGAR_CANE));
            }
        }
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return BOX_AABB;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CANE);
        builder.add(FACING);
    }

    @Override
    protected boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, BlockPos pos) {
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }
}
