package com.oroarmor.netherite_plus.compatibility;

import com.oroarmor.netherite_plus.item.NetheriteElytraItem_Trinkets;

import net.minecraft.world.item.Item;

public final class NetheritePlusTrinketsCompatibilty {

	public static Item getTrinketsElytra(Item.Properties elytraSettings) {
		return new NetheriteElytraItem_Trinkets(elytraSettings);
	}

}
