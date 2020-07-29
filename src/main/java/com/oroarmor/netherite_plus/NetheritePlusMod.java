package com.oroarmor.netherite_plus;

import com.oroarmor.netherite_plus.item.NetheritePlusModItems;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class NetheritePlusMod implements ModInitializer {

	@Override
	public void onInitialize() {
		System.out.println(FabricLoader.getInstance().isModLoaded("trinkets"));

		NetheritePlusConfigManager.load();

		NetheritePlusModItems.registerItems();

		NetheritePlusRecipeSerializer.init();

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusConfigManager.save());
	}

}
