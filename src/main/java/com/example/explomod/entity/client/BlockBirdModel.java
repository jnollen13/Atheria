package com.example.explomod.entity.client;


import com.example.explomod.ExploMod;
import com.example.explomod.entity.custom.BlockBird;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class BlockBirdModel<T extends BlockBird> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(ExploMod.MODID, "blockbird"), "main");
	private final ModelPart root;
	private final ModelPart feet;
	private final ModelPart rFoot;
	private final ModelPart lFoot;
	private final ModelPart body;
	private final ModelPart wings;
	private final ModelPart w1;
	private final ModelPart w2;
	private final ModelPart face;

	public BlockBirdModel(ModelPart root) {
		this.root = root.getChild("root");
		this.feet = this.root.getChild("feet");
		this.rFoot = this.feet.getChild("rFoot");
		this.lFoot = this.feet.getChild("lFoot");
		this.body = this.root.getChild("body");
		this.wings = this.root.getChild("wings");
		this.w1 = this.wings.getChild("w1");
		this.w2 = this.wings.getChild("w2");
		this.face = this.root.getChild("face");
		this.root.yRot=-90f;
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition feet = root.addOrReplaceChild("feet", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition rFoot = feet.addOrReplaceChild("rFoot", CubeListBuilder.create().texOffs(13, 15).addBox(-2.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 14).addBox(-2.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition lFoot = feet.addOrReplaceChild("lFoot", CubeListBuilder.create().texOffs(13, 15).addBox(0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 14).addBox(0.0F, -1.0F, 0.0F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(3, 1).addBox(-2.0F, -4.0F, -1.0F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wings = root.addOrReplaceChild("wings", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition w1 = wings.addOrReplaceChild("w1", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing_r1 = w1.addOrReplaceChild("wing_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, -2.0F, 0.5F, 0.0F, 0.0F, -0.4363F));

		PartDefinition w2 = wings.addOrReplaceChild("w2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wing_r2 = w2.addOrReplaceChild("wing_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.15F, -2.0F, -1.0F, 1.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -1.5F, 0.5F, 0.0F, 0.0F, 0.4363F));

		PartDefinition face = root.addOrReplaceChild("face", CubeListBuilder.create().texOffs(0, 7).addBox(-1.25F, -2.75F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(3, 8).addBox(-0.75F, -3.25F, 1.25F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(@NotNull BlockBird entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		if(!entity.onGround()){
			this.animate(entity.flying, BlockBirdAnimations.animation, 1.0f, 1f);
		}
	}


	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public @NotNull ModelPart root() {
		return root;
	}
}