package com.example.explomod.network;

import com.example.explomod.ExploMod;
import com.example.explomod.data.AtheriaDataAttachments;
import com.example.explomod.data.LearnedSpells;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClientboundSyncKnowledgePacket(List<ResourceLocation> unlockedItems) implements CustomPacketPayload {
    public static final Type<ClientboundSyncKnowledgePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "sync_spells"));

    public static final StreamCodec<FriendlyByteBuf, ClientboundSyncKnowledgePacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundSyncKnowledgePacket::unlockedItems,
            ClientboundSyncKnowledgePacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }

    public static void handle(ClientboundSyncKnowledgePacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().player.setData(AtheriaDataAttachments.KNOWN_SPELLS.get(), new LearnedSpells(payload.unlockedItems()));
            }
        });
    }
}
