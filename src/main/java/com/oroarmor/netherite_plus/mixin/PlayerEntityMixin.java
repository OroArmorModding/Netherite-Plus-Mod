package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.stat.NetheritePlusStats;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	@Redirect(method = "checkFallFlying()Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item checkFallFlying(ItemStack stack) {
		return UniqueItemRegistry.ELYTRA.getDefaultItem(stack.getItem());
	}

	@Redirect(method = "damageShield(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
	private Item shields(ItemStack stack) {
		return UniqueItemRegistry.SHIELD.getDefaultItem(stack.getItem());
	}

	@SuppressWarnings("unused")
	@Inject(method = "increaseTravelMotionStats", at = @At("RETURN"))
	private void increaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo info) {
		if (!((PlayerEntity) (Object) this).hasVehicle()) {
			if (((PlayerEntity) (Object) this).isFallFlying()) {
				boolean hasNetheriteElytra = false;
				for (ItemStack item : ((PlayerEntity) (Object) this).getArmorItems()) {
					hasNetheriteElytra |= item.getItem() == NetheritePlusItems.NETHERITE_ELYTRA;
				}
				if (!hasNetheriteElytra) {
					return;
				}

				int l = Math.round(MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
				((PlayerEntity) (Object) this).increaseStat(NetheritePlusStats.FLY_NETHERITE_ELYTRA, l);
			}
		}
	}
}
