package com.oroarmor.netherite_plus;

import com.oroarmor.netherite_plus.item.NetheriteElytraItem_Trinkets;

import net.minecraft.item.Item;

public final class NetheritePlusTrinketsCompatibilty {

	public static Item getTrinketsElytra(Item.Settings elytraSettings) {
		return new NetheriteElytraItem_Trinkets(elytraSettings);
	}

}
