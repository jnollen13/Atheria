package com.example.explomod.worldgen.structure_dont_use.structures;

import com.example.explomod.worldgen.structure_dont_use.ModStructureType;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.structures.IglooPieces;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class VenderStallStructure extends Structure {
    public static final MapCodec<Structure> CODEC = simpleCodec(VenderStallStructure::new);

    public VenderStallStructure(Structure.StructureSettings settings) {
        super(settings);
    }

    @Override
    public @NotNull Optional<Structure.GenerationStub> findGenerationPoint(Structure.@NotNull GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, p_227598_ -> this.generatePieces(p_227598_, context));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context) {
        ChunkPos chunkpos = context.chunkPos();
        WorldgenRandom worldgenrandom = context.random();
        BlockPos blockpos = new BlockPos(chunkpos.getMinBlockX(), 90, chunkpos.getMinBlockZ());
        Rotation rotation = Rotation.getRandom(worldgenrandom);
        IglooPieces.addPieces(context.structureTemplateManager(), blockpos, rotation, builder, worldgenrandom);
    }

    @Override
    public @NotNull StructureType<?> type() {
        return ModStructureType.VENDOR_STALL.value();
    }
}
