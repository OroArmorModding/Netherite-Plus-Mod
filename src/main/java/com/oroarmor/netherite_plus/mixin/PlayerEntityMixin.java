package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.stat.NetheritePlusStats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@Mixin(Player.class)
public class PlayerEntityMixin {

	@Inject(method = "checkMovementStatistics", at = @At("RETURN"))
	private void increaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo info) {
		if (!((Player) (Object) this).isPassenger()) {
			if (((Player) (Object) this).isFallFlying()) {
				boolean hasNetheriteElytra = false;
				for (ItemStack item : ((Player) (Object) this).getArmorSlots()) {
					hasNetheriteElytra |= item.getItem() == NetheritePlusItems.NETHERITE_ELYTRA;
				}
				if (!hasNetheriteElytra) {
					return;
				}

				int l = Math.round(Mth.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
				((Player) (Object) this).awardStat(NetheritePlusStats.FLY_NETHERITE_ELYTRA, l);
			}
		}
	}
}
