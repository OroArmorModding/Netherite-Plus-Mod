package com.oroarmor.netherite_plus.block;

import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NetheriteBeaconBlock extends BeaconBlock {
	public NetheriteBeaconBlock(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new NetheriteBeaconBlockEntity();
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof NetheriteBeaconBlockEntity) {
				player.openHandledScreen((NetheriteBeaconBlockEntity) blockEntity);
				player.incrementStat(Stats.INTERACT_WITH_BEACON);
			}

			return ActionResult.CONSUME;
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof NetheriteBeaconBlockEntity) {
				((NetheriteBeaconBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}

	}
}
