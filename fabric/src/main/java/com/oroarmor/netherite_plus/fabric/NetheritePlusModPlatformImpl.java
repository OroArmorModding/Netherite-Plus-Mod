package com.oroarmor.netherite_plus.fabric;

import com.oroarmor.netherite_plus.compatibility.NetheritePlusTrinketsCompatibilty;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.Item;

public class NetheritePlusModPlatformImpl {
	public static Item getElytraItem(Item.Settings elytraSettings) {
		return !FabricLoader.getInstance().isModLoaded("trinkets") ? new NetheriteElytraItem(elytraSettings) : NetheritePlusTrinketsCompatibilty.getTrinketsElytra(elytraSettings);
	}

	public static Item.Settings setISTER(Item.Settings properties) {
		return properties;
	}
}
