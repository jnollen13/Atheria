package com.example.explomod.entity.client;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.BossGrassGolem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

    public class BossGrassGolemRenderer extends MobRenderer<BossGrassGolem, BossGrassGolemModel<BossGrassGolem>> {
        public BossGrassGolemRenderer(EntityRendererProvider.Context context) {
            super(context, new BossGrassGolemModel<>(context.bakeLayer(GrassGolemModel.LAYER_LOCATION)), 0.25f);
        }

        @Override
        public @NotNull ResourceLocation getTextureLocation(@NotNull BossGrassGolem entity) {
            return ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "textures/entity/grass.png");
        }

        @Override
        public void render(BossGrassGolem entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
            if(entity.isBaby()) {
                poseStack.scale(1.2f, 1.2f, 1.2f);
            } else {
                poseStack.scale(3f, 4f, 3f);
            }

            super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        }
    }

