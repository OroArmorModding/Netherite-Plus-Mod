package com.oroarmor.netherite_plus.client.render.item;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class NetheriteTridentItemRenderer {
	private static final TridentEntityModel modelTrident = new TridentEntityModel();

	public static void render(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.scale(1.0F, -1.0F, -1.0F);
		VertexConsumer vertexConsumer2 = ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, modelTrident.getLayer(id("textures/entity/netherite_trident.png")), false, stack.hasGlint());
		modelTrident.render(matrices, vertexConsumer2, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}

}
