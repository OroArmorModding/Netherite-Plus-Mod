package com.oroarmor.netherite_plus;

import com.oroarmor.netherite_plus.item.NetheritePlusModItems;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;

import net.fabricmc.api.ModInitializer;

public class NetheritePlusMod implements ModInitializer {

	@Override
	public void onInitialize() {
//		new NetheritePlusConfigManager().save();

		NetheritePlusConfigManager.load();

		NetheritePlusModItems.registerItems();

		NetheritePlusRecipeSerializer.init();
	}

}
