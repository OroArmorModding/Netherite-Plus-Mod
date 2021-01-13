package com.oroarmor.netherite_plus.compatibility;

import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;

@Environment(EnvType.CLIENT)
public class NetheritePlusTrinketsRenderer {

	@Environment(EnvType.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final static ElytraEntityModel<AbstractClientPlayerEntity> elytra = new ElytraEntityModel();

	public static void renderTrinketsElytra(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity livingEntity, Item item) {
		matrixStack.push();
		matrixStack.scale(1.5f, 1.5f, 1.5f);
		matrixStack.translate(0.0D, -0.775D, 0.125D);
		elytra.setAngles(livingEntity, 0f, 0f, 0f, 0f, 0f);
		VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, elytra.getLayer(NetheriteElytraFeatureRenderer.NETHERITE_ELYTRA_SKIN), false, item.getDefaultStack().hasGlint());
		elytra.render(matrixStack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
	}

}
