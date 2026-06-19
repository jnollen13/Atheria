package com.example.explomod.worldgen.structure_dont_use.structures;

import com.example.explomod.worldgen.structure_dont_use.ModStructureType;
import com.example.explomod.worldgen.structure_dont_use.pieces.DarkPortalPiece;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.NotNull;

public class DarkPortalStructure extends SinglePieceStructure {
    public static final MapCodec<Structure> CODEC = simpleCodec(DarkPortalStructure::new);

    public DarkPortalStructure(Structure.StructureSettings p_227694_) {
        super(DarkPortalPiece::new, 12, 15, p_227694_);
    }

    public @NotNull StructureType<?> type() {
        return ModStructureType.DARK_PORTAL.value();
    }
}
