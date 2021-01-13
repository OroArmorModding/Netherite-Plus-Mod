package com.oroarmor.netherite_plus.client.render.item;

import java.util.List;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation.Mode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.DyeColor;
import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;

public class NetheriteShieldItemRenderer {

	private static final ShieldEntityModel modelShield = new ShieldEntityModel();

	public static void render(ItemStack stack, Mode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		boolean bl = stack.getSubTag("BlockEntityTag") != null;
		matrices.push();
		matrices.scale(1.0F, -1.0F, -1.0F);
		SpriteIdentifier spriteIdentifier = bl ? NetheritePlusTextures.NETHERITE_SHIELD_BASE : NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN;
		VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
		modelShield.getHandle().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		if (bl) {
			List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(ShieldItem.getColor(stack), BannerBlockEntity.getPatternListTag(stack));
			BannerBlockEntityRenderer.renderCanvas(matrices, vertexConsumers, light, overlay, modelShield.getPlate(), spriteIdentifier, false, list, stack.hasGlint());
		} else {
			modelShield.getPlate().render(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		}

		matrices.pop();
	}
}
