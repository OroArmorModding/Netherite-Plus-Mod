package com.oroarmor.netherite_plus.item;

import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;

public class NetheriteCrossbowItem extends CrossbowItem {
	public NetheriteCrossbowItem(Settings settings) {
		super(settings);
	}

	@Override
	public boolean isUsedOnRelease(ItemStack stack) {
		return stack.getItem() == NetheritePlusModItems.NETHERITE_CROSSBOW;
	}
}
