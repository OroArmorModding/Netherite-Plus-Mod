package com.oroarmor.netherite_plus.mixin.render;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

	@Shadow
	private ItemModels models;

	@Redirect(method = "getHeldItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	public Item getHeldItemModel(ItemStack stack) {
		return UniqueItemRegistry.TRIDENT.getDefaultItem(stack.getItem());
	}

	@Shadow
	protected abstract void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay,
			MatrixStack matrices, VertexConsumer vertexConsumer4);

	@Inject(method = "renderItem", at = @At(value = "HEAD"), cancellable = true)
	public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded,
			MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model,
			CallbackInfo info) {
		if (!stack.isEmpty() && stack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT) {
			matrices.push();
			boolean bl = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND
					|| renderMode == ModelTransformation.Mode.FIXED;
			if (stack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT && bl) {
				model = models.getModelManager().getModel(new ModelIdentifier(MOD_ID + ":netherite_trident#inventory"));
			}

			model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
			matrices.translate(-0.5D, -0.5D, -0.5D);
			if (model.isBuiltin() || stack.getItem() == NetheritePlusItems.NETHERITE_TRIDENT && !bl) {
				BuiltinModelItemRenderer.INSTANCE.render(stack, renderMode, matrices, vertexConsumers, light, overlay);
			} else {
				RenderLayer renderLayer = RenderLayers.getItemLayer(stack, true);
				VertexConsumer vertexConsumer4;
				vertexConsumer4 = ItemRenderer.getDirectGlintVertexConsumer(vertexConsumers, renderLayer, true,
						stack.hasGlint());

				renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer4);
			}

			matrices.pop();
			info.cancel();
		}

	}
}
