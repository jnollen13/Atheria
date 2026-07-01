package com.example.explomod.packets;

import com.example.explomod.ExploMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record HealSpellPacket() implements CustomPacketPayload {
    public static final Type<HealSpellPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "lightning_packet"));

    public static final StreamCodec<FriendlyByteBuf, HealSpellPacket> CODEC = StreamCodec.of(
            (buf, value) -> {
            },
            buf -> new HealSpellPacket()
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
