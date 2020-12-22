package com.oroarmor.netherite_plus.mixin.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.ItemModelShaper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	private ItemModelShaper itemModelShaper;

	@Shadow
	protected abstract void renderModelLists(BakedModel model, ItemStack stack, int light, int overlay, PoseStack matrices, VertexConsumer vertexConsumer4);

	@Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
	public void renderItem(ItemStack stack, ItemTransforms.TransformType renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
		if (!stack.isEmpty() && stack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT) {
			matrices.pushPose();
			boolean bl = renderMode == ItemTransforms.TransformType.GUI || renderMode == ItemTransforms.TransformType.GROUND || renderMode == ItemTransforms.TransformType.FIXED;
			if (stack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT && bl) {
				model = itemModelShaper.getModelManager().getModel(new ModelResourceLocation(MOD_ID + ":netherite_trident#inventory"));
			}

			model.getTransforms().getTransform(renderMode).apply(leftHanded, matrices);
			matrices.translate(-0.5D, -0.5D, -0.5D);
			if (model.isCustomRenderer() || stack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT && !bl) {
				BlockEntityWithoutLevelRenderer.instance.renderByItem(stack, renderMode, matrices, vertexConsumers, light, overlay);
			} else {
				RenderType renderLayer = ItemBlockRenderTypes.getRenderType(stack, true);
				VertexConsumer vertexConsumer4;
				vertexConsumer4 = ItemRenderer.getFoilBufferDirect(vertexConsumers, renderLayer, true, stack.hasFoil());

				renderModelLists(model, stack, light, overlay, matrices, vertexConsumer4);
			}

			matrices.popPose();
			info.cancel();
		}
	}
}
