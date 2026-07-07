package com.example.explomod.packets;

import com.example.explomod.ExploMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SpellPacket(String spell) implements CustomPacketPayload {
    public static final Type<SpellPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "spell_packet"));

    // Codec required by NeoForge to handle packet streaming
    public static final StreamCodec<FriendlyByteBuf, SpellPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(128), SpellPacket::spell,
            SpellPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
