package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	public void onUse(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {

		ItemStack itemStack = player.getItemInHand(hand);

		if (!(itemStack.getItem() instanceof BlockItem)) {
			return;
		}

		int i = state.getValue(CauldronBlock.LEVEL);
		Block block = ((BlockItem) itemStack.getItem()).getBlock();

		if (block instanceof NetheriteShulkerBoxBlock && !world.isClientSide() && i > 0) {
			ItemStack itemStack5 = new ItemStack(NetheritePlusBlocks.NETHERITE_SHULKER_BOX.get(), 1);
			if (itemStack.hasTag()) {
				itemStack5.setTag(itemStack.getTag().copy());
			}

			player.setItemInHand(hand, itemStack5);
			setWaterLevel(world, pos, state, i - 1);
			player.awardStat(Stats.CLEAN_SHULKER_BOX);
			cir.setReturnValue(InteractionResult.SUCCESS);
		}
	}

	@Shadow
	public abstract void setWaterLevel(Level world, BlockPos pos, BlockState state, int i);
}
