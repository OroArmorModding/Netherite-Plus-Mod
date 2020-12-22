package com.oroarmor.netherite_plus.client.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ShulkerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class NetheriteShulkerBoxBlockEntityRenderer extends BlockEntityRenderer<NetheriteShulkerBoxBlockEntity> {
	private final ShulkerModel<?> model;

	public NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
		model = new ShulkerModel<>();
	}

	@Override
	public void render(NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
		Direction direction = Direction.UP;
		if (shulkerBoxBlockEntity.hasLevel()) {
			BlockState blockState = shulkerBoxBlockEntity.getLevel().getBlockState(shulkerBoxBlockEntity.getBlockPos());
			if (blockState.getBlock() instanceof NetheriteShulkerBoxBlock) {
				direction = blockState.getValue(NetheriteShulkerBoxBlock.FACING);
			}
		}

		DyeColor dyeColor = shulkerBoxBlockEntity.getColor();
		Material spriteIdentifier2;
		if (dyeColor == null) {
			spriteIdentifier2 = new Material(NetheritePlusTextures.SHULKER_BOXES_ATLAS_TEXTURE, id("entity/netherite_shulker/netherite_shulker"));
		} else {
			spriteIdentifier2 = new Material(NetheritePlusTextures.SHULKER_BOXES_ATLAS_TEXTURE, id("entity/netherite_shulker/netherite_shulker_" + dyeColor.getName()));
		}

		matrixStack.pushPose();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.scale(0.9995F, 0.9995F, 0.9995F);
		matrixStack.mulPose(direction.getRotation());
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		matrixStack.translate(0.0D, -1.0D, 0.0D);
		VertexConsumer vertexConsumer = spriteIdentifier2.buffer(vertexConsumerProvider, RenderType::entityCutoutNoCull);
		model.getBase().render(matrixStack, vertexConsumer, i, j);
		matrixStack.translate(0.0D, -shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5F, 0.0D);
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(270.0F * shulkerBoxBlockEntity.getAnimationProgress(f)));
		model.getLid().render(matrixStack, vertexConsumer, i, j);
		matrixStack.popPose();
	}
}
