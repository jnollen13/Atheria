package com.example.explomod.worldgen.structure_dont_use.pieces;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.Locale;

public interface ModStructurePieceType {

    //useless

    StructurePieceType DARK_PORTAL_PIECE = setPieceId(DarkPortalPiece::new, "DpFp");
    StructurePieceType VENDOR_STALL = setTemplatePieceId(VenderStallPiece.IglooPiece::new, "VSep");


    private static StructurePieceType setFullContextPieceId(StructurePieceType pieceType, String pieceId) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, pieceId.toLowerCase(Locale.ROOT), pieceType);
    }

    private static StructurePieceType setTemplatePieceId(StructurePieceType.StructureTemplateType templateType, String pieceId) {
        return setFullContextPieceId(templateType, pieceId);
    }

    private static StructurePieceType setPieceId(StructurePieceType.ContextlessType type, String key) {
        return setFullContextPieceId(type, key);
    }
}
