package com.oroarmor.netherite_plus.mixin.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.client.NetheritePlusTextures;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.DyeColor;

@Environment(EnvType.CLIENT)
@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

	private static final NetheriteShulkerBoxBlockEntity[] RENDER_NETHERITE_SHULKER_BOX_DYED = Arrays
			.stream(DyeColor.values()).sorted(Comparator.comparingInt(DyeColor::getId))
			.map(NetheriteShulkerBoxBlockEntity::new).toArray((i) -> {
				return new NetheriteShulkerBoxBlockEntity[i];
			});
	private static final NetheriteShulkerBoxBlockEntity RENDER_NETHERITE_SHULKER_BOX = new NetheriteShulkerBoxBlockEntity(
			(DyeColor) null);
	private final ShieldEntityModel modelShield = new ShieldEntityModel();

	private final TridentEntityModel modelTrident = new TridentEntityModel();

	@SuppressWarnings("unused")
	@Inject(method = "render(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;II)V", at = @At("HEAD"), cancellable = true)
	public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo info) {

		Item item = stack.getItem();

		if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof NetheriteShulkerBoxBlock) {

			BlockEntity blockEntity9;
			DyeColor dyeColor = NetheriteShulkerBoxBlock.getColor(item);
			if (dyeColor == null) {
				blockEntity9 = RENDER_NETHERITE_SHULKER_BOX;
			} else {
				blockEntity9 = RENDER_NETHERITE_SHULKER_BOX_DYED[dyeColor.getId()];
			}

			BlockEntityRenderDispatcher.INSTANCE.renderEntity(blockEntity9, matrixStack, vertexConsumerProvider, i, j);
			info.cancel();
		} else if (item == NetheritePlusItems.NETHERITE_SHIELD) {
			boolean bl = stack.getSubTag("BlockEntityTag") != null;
			matrixStack.push();
			matrixStack.scale(1.0F, -1.0F, -1.0F);
			SpriteIdentifier spriteIdentifier = bl ? NetheritePlusTextures.NETHERITE_SHIELD_BASE
					: NetheritePlusTextures.NETHERITE_SHIELD_BASE_NO_PATTERN;
			VertexConsumer vertexConsumer = spriteIdentifier.getSprite()
					.getTextureSpecificVertexConsumer(ItemRenderer.getDirectGlintVertexConsumer(vertexConsumerProvider,
							modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
			modelShield.method_23775().render(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
			if (bl) {
				List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(ShieldItem.getColor(stack),
						BannerBlockEntity.getPatternListTag(stack));
				BannerBlockEntityRenderer.renderCanvas(matrixStack, vertexConsumerProvider, i, j,
						modelShield.method_23774(), spriteIdentifier, false, list, stack.hasGlint());
			} else {
				modelShield.method_23774().render(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
			}

			matrixStack.pop();
		} else if (item == NetheritePlusItems.NETHERITE_TRIDENT) {
			matrixStack.push();
			matrixStack.scale(1.0F, -1.0F, -1.0F);
			VertexConsumer vertexConsumer2 = ItemRenderer.getDirectGlintVertexConsumer(vertexConsumerProvider,
					modelTrident.getLayer(id("textures/entity/netherite_trident.png")), false, stack.hasGlint());
			modelTrident.render(matrixStack, vertexConsumer2, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.pop();
		}

	}
}
