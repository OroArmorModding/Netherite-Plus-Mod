package com.oroarmor.netherite_plus;

import com.oroarmor.netherite_plus.item.NetheritePlusModItems;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class NetheritePlusMod implements ModInitializer {

	@Override
	public void onInitialize() {
		NetheritePlusConfigManager.config.readConfigFromFile();

		NetheritePlusModItems.registerItems();

		NetheritePlusRecipeSerializer.init();

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusConfigManager.config.saveConfigToFile());
	}

}
