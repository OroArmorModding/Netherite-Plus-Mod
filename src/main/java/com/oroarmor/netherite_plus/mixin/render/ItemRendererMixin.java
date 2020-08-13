package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

	@Redirect(method = "renderItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	public Item renderItem(ItemStack stack) {
		return UniqueItemRegistry.TRIDENT.getDefaultItem(stack.getItem());
	}

	@Redirect(method = "getHeldItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	public Item getHeldItemModel(ItemStack stack) {
		return UniqueItemRegistry.TRIDENT.getDefaultItem(stack.getItem());
	}
}
