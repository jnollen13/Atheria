package com.example.explomod.packets;

import com.example.explomod.ExploMod;
import com.example.explomod.block.custom.portal.PortalClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;


public record PortalTravelSoundPacket() implements CustomPacketPayload {
    public static final Type<PortalTravelSoundPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "play_portal_travel_sound"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PortalTravelSoundPacket> STREAM_CODEC = StreamCodec.unit(new PortalTravelSoundPacket());

    @Override
    public @NotNull Type<PortalTravelSoundPacket> type() {
        return TYPE;
    }

    public static void execute(PortalTravelSoundPacket payload, IPayloadContext context) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().level != null) {
            PortalClientUtil.playTravelSound();
        }
    }
}