package com.oroarmor.netherite_plus.mixin.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@Environment(EnvType.CLIENT)
@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

	@Redirect(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item renderFirstPersonItem(ItemStack itemStack) {
		return UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
	}

	@Redirect(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item renderItem(ItemStack itemStack) {
		return itemStack.getItem() == NetheritePlusItems.NETHERITE_BOW ? Items.CROSSBOW
				: UniqueItemRegistry.CROSSBOW.getDefaultItem(itemStack.getItem());
	}
}
