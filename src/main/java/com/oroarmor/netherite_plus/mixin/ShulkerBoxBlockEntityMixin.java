package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {

	@Inject(method = "canInsert", at = @At("HEAD"), cancellable = true)
	public void canInsert(int slot, ItemStack stack, Direction dir, CallbackInfoReturnable<Boolean> cir) {
		if (!FabricLoader.getInstance().isModLoaded("shulkularity") && Block.getBlockFromItem(stack.getItem()) instanceof NetheriteShulkerBoxBlock) {
			cir.setReturnValue(false);
		}
	}
}
