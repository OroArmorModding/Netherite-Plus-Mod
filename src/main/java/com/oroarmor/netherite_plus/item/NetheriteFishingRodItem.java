package com.oroarmor.netherite_plus.item;

import net.minecraft.item.FishingRodItem;

public class NetheriteFishingRodItem extends FishingRodItem {

	public NetheriteFishingRodItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getEnchantability() {
		return 2;
	}

}
