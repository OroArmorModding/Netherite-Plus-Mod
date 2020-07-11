package com.oroarmor.netherite_plus.render;

import com.oroarmor.netherite_plus.NetheritePlusClientMod;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShulkerEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

@Environment(EnvType.CLIENT)
public class NetheriteShulkerBoxBlockEntityRenderer extends BlockEntityRenderer<NetheriteShulkerBoxBlockEntity> {
	private final ShulkerEntityModel<?> model;

	public NetheriteShulkerBoxBlockEntityRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher) {
		super(blockEntityRenderDispatcher);
		model = new ShulkerEntityModel<>();
	}

	@Override
	public void render(NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity, float f, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		Direction direction = Direction.UP;
		if (shulkerBoxBlockEntity.hasWorld()) {
			BlockState blockState = shulkerBoxBlockEntity.getWorld().getBlockState(shulkerBoxBlockEntity.getPos());
			if (blockState.getBlock() instanceof ShulkerBoxBlock) {
				direction = blockState.get(ShulkerBoxBlock.FACING);
			}
		}

		DyeColor dyeColor = shulkerBoxBlockEntity.getColor();
		SpriteIdentifier spriteIdentifier2;
		if (dyeColor == null) {
			spriteIdentifier2 = new SpriteIdentifier(NetheritePlusClientMod.SHULKER_BOXES_ATLAS_TEXTURE,
					new Identifier("netherite_plus", "entity/netherite_shulker/netherite_shulker"));
		} else {
			spriteIdentifier2 = new SpriteIdentifier(NetheritePlusClientMod.SHULKER_BOXES_ATLAS_TEXTURE, new Identifier(
					"netherite_plus", "entity/netherite_shulker/netherite_shulker_" + dyeColor.getName()));
		}

		matrixStack.push();
		matrixStack.translate(0.5D, 0.5D, 0.5D);
		matrixStack.scale(0.9995F, 0.9995F, 0.9995F);
		matrixStack.multiply(direction.getRotationQuaternion());
		matrixStack.scale(1.0F, -1.0F, -1.0F);
		matrixStack.translate(0.0D, -1.0D, 0.0D);
		VertexConsumer vertexConsumer = spriteIdentifier2.getVertexConsumer(vertexConsumerProvider,
				RenderLayer::getEntityCutoutNoCull);
		model.getBottomShell().render(matrixStack, vertexConsumer, i, j);
		matrixStack.translate(0.0D, -shulkerBoxBlockEntity.getAnimationProgress(f) * 0.5F, 0.0D);
		matrixStack.multiply(
				Vector3f.POSITIVE_Y.getDegreesQuaternion(270.0F * shulkerBoxBlockEntity.getAnimationProgress(f)));
		model.getTopShell().render(matrixStack, vertexConsumer, i, j);
		matrixStack.pop();
	}
}
