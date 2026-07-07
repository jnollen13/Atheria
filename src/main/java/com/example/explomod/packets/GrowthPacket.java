package com.example.explomod.packets;

import com.example.explomod.ExploMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GrowthPacket implements CustomPacketPayload {
    public static final Type<GrowthPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "airburst_packet"));

    public static final StreamCodec<FriendlyByteBuf, GrowthPacket> CODEC = StreamCodec.of(
            (buf, value) -> {},
            buf -> new GrowthPacket()
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
