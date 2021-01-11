package com.oroarmor.multi_item_lib.mixin;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(UnbreakingEnchantment.class)
public class UnbreakingEnchantmentMixin {
	@Redirect(method = "shouldPreventDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private static Item shouldPreventDamage(ItemStack stack) {
		return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
	}
}
