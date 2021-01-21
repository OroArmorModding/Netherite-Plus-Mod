package com.oroarmor.netherite_plus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class NetheritePlusModFabric implements ModInitializer {
	public void onInitialize() {
		NetheritePlusMod.initialize();

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusMod.CONFIG.saveConfigToFile());

		NetheritePlusMod.registerItemsWithMultiItemLib();
	}
}
