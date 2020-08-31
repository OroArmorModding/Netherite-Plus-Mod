package com.oroarmor.netherite_plus.client.render.item;

import java.util.List;

import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRenderer;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.DyeColor;

public class NetheriteShieldItemRenderer implements BuiltinItemRenderer {

	private final ShieldEntityModel modelShield = new ShieldEntityModel();

	@Override
	public void render(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
			int overlay) {
		boolean bl = stack.getSubTag("BlockEntityTag") != null;
		matrices.push();
		matrices.scale(1.0F, -1.0F, -1.0F);
		SpriteIdentifier spriteIdentifier = bl ? NetheritePlusTextures.NETHERITE_SHIELD_BASE
				: NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN;
		VertexConsumer vertexConsumer = spriteIdentifier.getSprite()
				.getTextureSpecificVertexConsumer(ItemRenderer.getDirectGlintVertexConsumer(vertexConsumers,
						modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
		modelShield.method_23775().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (bl) {
			List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(ShieldItem.getColor(stack),
					BannerBlockEntity.getPatternListTag(stack));
			BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay,
					modelShield.method_23774(), spriteIdentifier, false, list, stack.hasGlint());
		} else {
			modelShield.method_23774().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrices.pop();

	}

}
