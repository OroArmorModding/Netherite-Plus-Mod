package com.oroarmor.netherite_plus.client.render.item;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

import net.minecraft.client.model.ShieldModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;

public class NetheriteShieldItemRenderer {

	private static final ShieldModel modelShield = new ShieldModel();

	public static void render(ItemStack stack, TransformType mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
		boolean bl = stack.getTagElement("BlockEntityTag") != null;
		matrices.pushPose();
		matrices.scale(1.0F, -1.0F, -1.0F);
		Material spriteIdentifier = bl ? NetheritePlusTextures.NETHERITE_SHIELD_BASE : NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN;
		VertexConsumer vertexConsumer = spriteIdentifier.sprite().wrap(ItemRenderer.getFoilBufferDirect(vertexConsumers, modelShield.renderType(spriteIdentifier.atlasLocation()), true, stack.hasFoil()));
		modelShield.handle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (bl) {
			List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.createPatterns(ShieldItem.getColor(stack), BannerBlockEntity.getItemPatterns(stack));
			BannerRenderer.renderPatterns(matrices, vertexConsumers, light, overlay, modelShield.plate(), spriteIdentifier, false, list, stack.hasFoil());
		} else {
			modelShield.plate().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrices.popPose();
	}
}
