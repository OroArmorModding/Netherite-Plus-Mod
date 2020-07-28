package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(MobEntity.class)
public class MobEntityMixin {

	@Redirect(method = "getPreferredEquipmentSlot(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/EquipmentSlot;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private static Item tickMovement(ItemStack stack) {
		return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
	}
}
