package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Redirect(method = "checkFallFlying()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item tickMovement(ItemStack stack) {
		return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
	}

	@Redirect(method = "damageShield(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item shields(ItemStack stack) {
		return UniqueItemRegistry.SHIELD.getDefaultItem(stack.getItem());
	}
}
