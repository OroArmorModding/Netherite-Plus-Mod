package com.oroarmor.netherite_plus.compatibility;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oroarmor.netherite_plus.client.render.NetheriteElytraFeatureRenderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public class NetheritePlusTrinketsRenderer {

	@Environment(EnvType.CLIENT)
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public final static ElytraModel<AbstractClientPlayer> elytra = new ElytraModel();

	public static void renderTrinketsElytra(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, AbstractClientPlayer livingEntity, Item item) {
		matrixStack.pushPose();
		matrixStack.scale(1.5f, 1.5f, 1.5f);
		matrixStack.translate(0.0D, -0.775D, 0.125D);
		elytra.setupAnim(livingEntity, 0f, 0f, 0f, 0f, 0f);
		VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, elytra.renderType(NetheriteElytraFeatureRenderer.NETHERITE_ELYTRA_SKIN), false, item.getDefaultInstance().hasFoil());
		elytra.renderToBuffer(matrixStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.popPose();
	}

}
