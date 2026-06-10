package com.example.explomod.worldgen.structure.structures;

import com.example.explomod.ExploMod;
import com.example.explomod.worldgen.structure.pieces.ModStructurePieceType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class DarkPortalPiece extends ScatteredFeaturePiece {
    public static final int WIDTH = 12;
    public static final int DEPTH = 15;
    private boolean placedMainChest;
    private boolean placedTrap1;
    private static final DarkPortalPiece.MossStoneSelector STONE_SELECTOR = new DarkPortalPiece.MossStoneSelector();
    private static final DarkPortalPiece.DarkPortalFrameSelector PORTAL_FRAME_SELECTOR = new DarkPortalPiece.DarkPortalFrameSelector();

    public DarkPortalPiece(RandomSource random, int x, int z) {
        super(ModStructurePieceType.DARK_PORTAL_PIECE, x, 64, z, 12, 10, 15, getRandomHorizontalDirection(random));
    }

    public DarkPortalPiece(CompoundTag tag) {
        super(ModStructurePieceType.DARK_PORTAL_PIECE, tag);
        this.placedMainChest = tag.getBoolean("placedMainChest");
        this.placedTrap1 = tag.getBoolean("placedTrap1");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putBoolean("placedMainChest", this.placedMainChest);
        tag.putBoolean("placedTrap1", this.placedTrap1);
    }

    @Override
    public void postProcess(
            WorldGenLevel level,
            StructureManager structureManager,
            ChunkGenerator generator,
            RandomSource random,
            BoundingBox box,
            ChunkPos chunkPos,
            BlockPos pos
    ) {
        if (this.updateAverageGroundHeight(level, box, 0)) {
            this.generateBox(level, box, 0, -1, 0, 10, 0, 10, false, random, STONE_SELECTOR);
            this.generateBox(level, box, 1, -1, 1, 4, 3, 1, false, random, PORTAL_FRAME_SELECTOR);
            this.generateAirBox(level, box, 2, 0, 1, 3, 2, 1);
            this.placeBlock(level, ExploMod.DARK_PORTAL.get().defaultBlockState(), 2,0,1, box);
            this.placeBlock(level, ExploMod.DARK_PORTAL.get().defaultBlockState(), 3,0,1, box);
            this.placeBlock(level, ExploMod.DARK_PORTAL.get().defaultBlockState(), 2,1,1, box);
            this.placeBlock(level, ExploMod.DARK_PORTAL.get().defaultBlockState(), 3,1,1, box);
            this.placeBlock(level, ExploMod.DARK_PORTAL.get().defaultBlockState(), 2,2,1, box);
            this.placeBlock(level, ExploMod.DARK_PORTAL.get().defaultBlockState(), 3,2,1, box);
            if (!this.placedTrap1) {
                this.placedTrap1 = this.createDispenser(level, box, random, 9, -2, 3, Direction.WEST, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
            }
            this.placeBlock(level, Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, Boolean.valueOf(true)), 8, -2, 3, box);
            if (!this.placedMainChest) {
                this.placedMainChest = this.createChest(level, box, random, 8, -3, 3, BuiltInLootTables.JUNGLE_TEMPLE);
            }
        }
    }

    static class MossStoneSelector extends StructurePiece.BlockSelector {
        @Override
        public void next(RandomSource p_227686_, int p_227687_, int p_227688_, int p_227689_, boolean p_227690_) {
            if (p_227686_.nextFloat() < 0.4F) {
                this.next = Blocks.COBBLESTONE.defaultBlockState();
            } else {
                this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
            }
        }
    }

    static class DarkPortalFrameSelector extends StructurePiece.BlockSelector {
        @Override
        public void next(RandomSource p_227686_, int p_227687_, int p_227688_, int p_227689_, boolean p_227690_) {
            if (p_227686_.nextFloat() < 0.4F) {
                this.next = Blocks.DARK_PRISMARINE.defaultBlockState();
            } else if (p_227686_.nextFloat() >= 0.4f && p_227686_.nextFloat() < 0.5f) {
                this.next = Blocks.DARK_PRISMARINE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.TOP);
            } else if (p_227686_.nextFloat() >= 0.5f && p_227686_.nextFloat() < 0.6f) {
                this.next = Blocks.DARK_PRISMARINE_SLAB.defaultBlockState().setValue(SlabBlock.TYPE, SlabType.BOTTOM);
            } else {
                this.next = Blocks.DEEPSLATE.defaultBlockState();
            }
        }
    }
}

