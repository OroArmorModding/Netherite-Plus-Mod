package com.oroarmor.netherite_plus.client.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class NetheriteElytraFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	public static final ResourceLocation NETHERITE_ELYTRA_SKIN = id("textures/entity/netherite_elytra.png");

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final ElytraModel<T> elytra = new ElytraModel();

	public NetheriteElytraFeatureRenderer(RenderLayerParent<T, M> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		ItemStack itemStack = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
		if (itemStack.getItem() == NetheritePlusItems.NETHERITE_ELYTRA) {
			ResourceLocation identifier4;
			if (livingEntity instanceof AbstractClientPlayer) {
				AbstractClientPlayer abstractClientPlayerEntity = (AbstractClientPlayer) livingEntity;
				if (abstractClientPlayerEntity.isElytraLoaded() && abstractClientPlayerEntity.getElytraTextureLocation() != null) {
					identifier4 = abstractClientPlayerEntity.getElytraTextureLocation();
				} else if (abstractClientPlayerEntity.isCapeLoaded() && abstractClientPlayerEntity.getCloakTextureLocation() != null && abstractClientPlayerEntity.isModelPartShown(PlayerModelPart.CAPE)) {
					identifier4 = abstractClientPlayerEntity.getCloakTextureLocation();
				} else {
					identifier4 = NETHERITE_ELYTRA_SKIN;
				}
			} else {
				identifier4 = NETHERITE_ELYTRA_SKIN;
			}

			matrixStack.pushPose();
			matrixStack.translate(0.0D, 0.0D, 0.125D);
			getParentModel().copyPropertiesTo(this.elytra);
			this.elytra.setupAnim(livingEntity, f, g, j, k, l);
			VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, this.elytra.renderType(identifier4), false, itemStack.hasFoil());
			this.elytra.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.popPose();
		}
	}

}
