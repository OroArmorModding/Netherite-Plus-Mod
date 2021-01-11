package com.oroarmor.netherite_plus;

import com.oroarmor.config.command.ConfigCommand;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class NetheritePlusModFabric implements ModInitializer {
	public void onInitialize() {
		NetheritePlusMod.initialize();

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusMod.CONFIG.saveConfigToFile());

		CommandRegistrationCallback.EVENT.register(new ConfigCommand(NetheritePlusMod.CONFIG));

		NetheritePlusMod.registerItemsWithMultiItemLib();
	}
}
