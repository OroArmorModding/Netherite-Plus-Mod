package com.oroarmor.netherite_plus.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class NetheriteShieldItem extends ShieldItem {
	public NetheriteShieldItem(Settings settings) {
		super(settings);
	}

	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BLOCK;
	}

//	@Override
//	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
//		ItemStack itemStack = user.getStackInHand(hand);
//		user.setCurrentHand(hand);
//		//System.out.println("used");
//		return TypedActionResult.consume(itemStack);
//	}

}
