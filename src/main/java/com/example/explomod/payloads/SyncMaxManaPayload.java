package com.example.explomod.payloads;

import com.example.explomod.ExploMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record SyncMaxManaPayload(float maxMana) implements CustomPacketPayload {
    public static final Type<SyncMaxManaPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "sync_max_mana"));

    public static final StreamCodec<FriendlyByteBuf, SyncMaxManaPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, SyncMaxManaPayload::maxMana,
            SyncMaxManaPayload::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
