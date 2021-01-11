package com.oroarmor.multi_item_lib.mixin.render;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.entity.FishingBobberEntityRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(FishingBobberEntityRenderer.class)
public class FishingBobberEntityRendererMixin {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item getItem(ItemStack stack) {
		return UniqueItemRegistry.FISHING_ROD.getDefaultItem(stack.getItem());
	}
}
