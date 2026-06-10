package com.example.explomod.entity.client;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.GrassGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class GrassGolemRenderer extends MobRenderer<GrassGolem, GrassGolemModel<GrassGolem>> {
    public GrassGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new GrassGolemModel<>(context.bakeLayer(GrassGolemModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GrassGolem entity) {
        return ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "textures/entity/grass.png");
    }

    @Override
    public void render(GrassGolem entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
