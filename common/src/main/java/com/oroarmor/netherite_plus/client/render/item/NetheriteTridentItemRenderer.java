package com.oroarmor.netherite_plus.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import net.minecraft.client.model.TridentModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheriteTridentItemRenderer  {
	private static final TridentModel modelTrident = new TridentModel();

	public static void render(ItemStack stack, TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		matrices.pushPose();
		matrices.scale(1.0F, -1.0F, -1.0F);
		VertexConsumer vertexConsumer2 = ItemRenderer.getFoilBufferDirect(vertexConsumers, modelTrident.renderType(id("textures/entity/netherite_trident.png")), false, stack.hasFoil());
		modelTrident.renderToBuffer(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.popPose();
	}

}
