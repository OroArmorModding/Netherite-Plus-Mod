package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(UnbreakingEnchantment.class)
public class UnbreakingEnchantmentMixin {
	@Redirect(method = "shouldPreventDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item shouldPreventDamage(ItemStack stack) {
		return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
	}
}
