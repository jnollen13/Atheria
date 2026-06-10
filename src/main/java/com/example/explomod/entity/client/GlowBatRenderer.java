package com.example.explomod.entity.client;

import com.example.explomod.entity.custom.GlowBat;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class GlowBatRenderer extends MobRenderer<GlowBat, GlowBatModel<GlowBat>> {
    public GlowBatRenderer(EntityRendererProvider.Context context) {
        super(context, new GlowBatModel<>(context.bakeLayer(GlowBatModel.LAYER_LOCATION)), 0.26f);
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GlowBat glowBat) {
        return ResourceLocation.withDefaultNamespace("textures/entity/bat.png");
    }

    @Override
    public void render(GlowBat entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    protected int getBlockLightLevel(GlowBat p_174146_, @NotNull BlockPos p_174147_) {
        int i = (int) Mth.clampedLerp(0.0F, 15.0F, 1.0F - (float)p_174146_.getDarkTicksRemaining() / 10.0F);
        return i == 15 ? 15 : Math.max(i, super.getBlockLightLevel(p_174146_, p_174147_));
    }
}
