package com.example.explomod.block.custom;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.ModEntities;
import com.example.explomod.entity.custom.BossGrassGolem;
import com.example.explomod.entity.custom.GrassGolem;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import net.minecraft.world.level.block.state.pattern.BlockPatternBuilder;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class NewLampBlock extends Block{
    public static final BooleanProperty CLICKED = BooleanProperty.create("clicked");
    @Nullable
    private BlockPattern snowGolemBase;
    @Nullable
    private BlockPattern snowGolemFull;
    @Nullable
    private BlockPattern ironGolemBase;
    @Nullable
    private BlockPattern ironGolemFull;
    private static final Predicate<BlockState> PUMPKINS_PREDICATE;

    public NewLampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(CLICKED, false));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
    if(!level.isClientSide()) {
      boolean currentState = state.getValue(CLICKED);
      level.setBlockAndUpdate(pos, state.setValue(CLICKED, !currentState));
    }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CLICKED);
    }

    protected void onPlace(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!oldState.is(state.getBlock())) {
            this.trySpawnGolem(level, pos);
        }

    }

    public boolean canSpawnGolem(LevelReader level, BlockPos pos) {
        return this.getOrCreateSnowGolemBase().find(level, pos) != null;
    }

    private void trySpawnGolem(Level level, BlockPos pos) {
        BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch = this.getOrCreateSnowGolemFull().find(level, pos);
        if (blockpattern$blockpatternmatch != null) {
            GrassGolem snowgolem = ModEntities.GRASS_GOLEM.get().create(level);
            if (snowgolem != null) {
                snowgolem.setPlayerCreated(true);
                spawnGolemInWorld(level, blockpattern$blockpatternmatch, snowgolem, blockpattern$blockpatternmatch.getBlock(0, 2, 0).getPos());

            }
        } else {
            BlockPattern.BlockPatternMatch blockpattern$blockpatternmatch1 = this.getOrCreateIronGolemFull().find(level, pos);
            if (blockpattern$blockpatternmatch1 != null) {
                BossGrassGolem irongolem =  ModEntities.BOSS_GRASS_GOLEM.get().create(level);
                if (irongolem != null) {
                    spawnGolemInWorld(level, blockpattern$blockpatternmatch1, irongolem, blockpattern$blockpatternmatch1.getBlock(1, 2, 0).getPos());
                }
            }
        }

    }

    private static void spawnGolemInWorld(Level level, BlockPattern.BlockPatternMatch patternMatch, Entity golem, BlockPos pos) {
        clearPatternBlocks(level, patternMatch);
        golem.moveTo((double)pos.getX() + (double)0.5F, (double)pos.getY() + 0.05, (double)pos.getZ() + (double)0.5F, 0.0F, 0.0F);
        level.addFreshEntity(golem);

        for(ServerPlayer serverplayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate((double)5.0F))) {
            CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayer, golem);
        }

        updatePatternBlocks(level, patternMatch);
    }

    public static void clearPatternBlocks(Level level, BlockPattern.BlockPatternMatch patternMatch) {
        for(int i = 0; i < patternMatch.getWidth(); ++i) {
            for(int j = 0; j < patternMatch.getHeight(); ++j) {
                BlockInWorld blockinworld = patternMatch.getBlock(i, j, 0);
                level.setBlock(blockinworld.getPos(), Blocks.AIR.defaultBlockState(), 2);
                level.levelEvent(2001, blockinworld.getPos(), Block.getId(blockinworld.getState()));
            }
        }
    }

    public static void updatePatternBlocks(Level level, BlockPattern.BlockPatternMatch patternMatch) {
        for(int i = 0; i < patternMatch.getWidth(); ++i) {
            for(int j = 0; j < patternMatch.getHeight(); ++j) {
                BlockInWorld blockinworld = patternMatch.getBlock(i, j, 0);
                level.blockUpdated(blockinworld.getPos(), Blocks.AIR);
            }
        }
    }

    private BlockPattern getOrCreateSnowGolemBase() {
        if (this.snowGolemBase == null) {
            this.snowGolemBase = BlockPatternBuilder.start().aisle(new String[]{" ", "#", "#"}).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.TALL_GRASS))).build();
        }

        return this.snowGolemBase;
    }

    private BlockPattern getOrCreateSnowGolemFull() {
        if (this.snowGolemFull == null) {
            this.snowGolemFull = BlockPatternBuilder.start().aisle(new String[]{"^", "#", "#"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.TALL_GRASS))).build();
        }

        return this.snowGolemFull;
    }

    private BlockPattern getOrCreateIronGolemBase() {
        if (this.ironGolemBase == null) {
            this.ironGolemBase = BlockPatternBuilder.start().aisle(new String[]{"~ ~", "###", "~#~"}).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(Blocks.GRASS_BLOCK))).where('~', (p_284869_) -> p_284869_.getState().isAir()).build();
        }
        return this.ironGolemBase;
    }

    private BlockPattern getOrCreateIronGolemFull() {
        if (this.ironGolemFull == null) {
            this.ironGolemFull = BlockPatternBuilder.start().aisle(new String[]{"~^~", "###", "~#~"}).where('^', BlockInWorld.hasState(PUMPKINS_PREDICATE)).where('#', BlockInWorld.hasState(BlockStatePredicate.forBlock(ExploMod.DENSE_GRASS.get()))).where('~', (p_284868_) -> p_284868_.getState().isAir()).build();
        }
        return this.ironGolemFull;
    }
    static {
        PUMPKINS_PREDICATE = (p_51396_) -> p_51396_ != null && (p_51396_.is(Blocks.CARVED_PUMPKIN) || p_51396_.is(Blocks.CREEPER_HEAD)||p_51396_.is(Blocks.CREEPER_WALL_HEAD)||p_51396_.is(Blocks.PUMPKIN)||p_51396_.is(ExploMod.NEW_LAMP.get()));
    }
}
