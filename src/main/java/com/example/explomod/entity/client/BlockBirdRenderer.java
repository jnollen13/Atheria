package com.example.explomod.entity.client;

import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.BlockBird;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BlockBirdRenderer extends MobRenderer<BlockBird, BlockBirdModel<BlockBird>> {
    public BlockBirdRenderer(EntityRendererProvider.Context context) {
        super(context, new BlockBirdModel<>(context.bakeLayer(BlockBirdModel.LAYER_LOCATION)), 0.22875f);
    }


    public static final ResourceLocation RED = ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "textures/entity/block_bird/red.png");
    public static final ResourceLocation WHITE = ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "textures/entity/block_bird/white.png");

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BlockBird entity) {
        return getVariantTexture(entity.getVariant());
    }

    @Override
    public void render(BlockBird entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            poseStack.scale(1f, 1f, 1f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    public static ResourceLocation getVariantTexture(BlockBird.Variant variant) {
        if (variant.getId()<3){
            return RED;
        }else{
            return WHITE;
        }
    }
}
