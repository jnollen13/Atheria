package com.example.explomod.entity.client;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.PhantomLizard;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PhantomLizardRenderer extends MobRenderer<PhantomLizard, PhantomLizardModel<PhantomLizard>> {
    public PhantomLizardRenderer(EntityRendererProvider.Context context) {
        super(context, new PhantomLizardModel<>(context.bakeLayer(PhantomLizardModel.LAYER_LOCATION)), 0.25f);
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomLizard entity) {
        return ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "textures/entity/phantoms/phantom_legged.png");
    }

    @Override
    public void render(PhantomLizard entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.45f, 0.45f, 0.45f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
