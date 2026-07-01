package com.example.explomod.packets;

import com.example.explomod.ExploMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record ToggleManaBarPacket() implements CustomPacketPayload {
    public static final Type<FireballPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "fireball_packet"));

    // Codec required by NeoForge to handle packet streaming
    public static final StreamCodec<FriendlyByteBuf, FireballPacket> CODEC = StreamCodec.of(
            (buf, value) -> {}, // No extra data to write
            buf -> new FireballPacket() // Reconstruct packet
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
