package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {

	@Redirect(method = "removeIfInvalid(Lnet/minecraft/entity/player/PlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item getItem(ItemStack stack) {
		return UniqueItemRegistry.FISHING_ROD.getDefaultItem(stack.getItem());
	}
}
