package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {

	@Overwrite
	private boolean removeIfInvalid(PlayerEntity playerEntity) {
		ItemStack itemStack = playerEntity.getMainHandStack();
		ItemStack itemStack2 = playerEntity.getOffHandStack();
		boolean bl = itemStack.getItem() instanceof FishingRodItem;
		boolean bl2 = itemStack2.getItem() instanceof FishingRodItem;
		if (!playerEntity.removed && playerEntity.isAlive() && (bl || bl2)
				&& ((FishingBobberEntity) ((Object) this)).squaredDistanceTo(playerEntity) <= 1024.0D) {
			return false;
		}
		((FishingBobberEntity) ((Object) this)).remove();
		return true;
	}
}
