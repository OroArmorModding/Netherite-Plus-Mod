package com.oroarmor.multi_item_lib.mixin.render;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {
	@Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item renderFirstPersonItem(ItemStack itemStack) {
		return UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
	}

	@Redirect(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item renderItem(ItemStack itemStack) {
		return UniqueItemRegistry.BOW.isItemInRegistry(itemStack.getItem()) ? Items.BOW : UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
	}
}
