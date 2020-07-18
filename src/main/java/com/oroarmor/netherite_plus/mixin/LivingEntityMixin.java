package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Redirect(method = "initAi()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	public Item initAi(ItemStack itemStack) {
		return UniqueItemRegistry.ELYTRA.getDefaultItem(itemStack.getItem());
	}

}
