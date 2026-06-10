package com.example.explomod.worldgen.structure.pieces;

import com.example.explomod.ExploMod;
import com.example.explomod.worldgen.structure.structures.DarkPortalPiece;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;

public interface ModStructurePieceType {

    StructurePieceType DARK_PORTAL_PIECE = setPieceId(DarkPortalPiece::new, "DpFp");


    private static StructurePieceType setFullContextPieceId(StructurePieceType pieceType, String pieceId) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, pieceId.toLowerCase(Locale.ROOT), pieceType);
    }

    private static StructurePieceType setPieceId(StructurePieceType.ContextlessType type, String key) {
        return setFullContextPieceId(type, key);
    }
}
