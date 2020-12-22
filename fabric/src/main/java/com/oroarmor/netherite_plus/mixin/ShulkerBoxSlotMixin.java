package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {

	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	public void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (!FabricLoader.getInstance().isModLoaded("shulkularity") && Block.byItem(stack.getItem()) instanceof NetheriteShulkerBoxBlock) {
			cir.setReturnValue(false);
		}
	}
}
