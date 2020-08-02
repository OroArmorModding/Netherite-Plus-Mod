package com.oroarmor.netherite_plus;

import com.oroarmor.netherite_plus.item.NetheritePlusModItems;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class NetheritePlusMod implements ModInitializer {

	public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;

	@Override
	public void onInitialize() {
		NetheritePlusConfigManager.config.readConfigFromFile();

		NetheritePlusModItems.registerItems();
		NetheritePlusDynamicDataPack.configureDynamicDataPack();

		NETHERITE_ANVIL = ScreenHandlerRegistry.registerSimple(new Identifier("netherite_plus", "netherite_anvil"),
				NetheriteAnvilScreenHandler::new);

		NetheritePlusRecipeSerializer.init();

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusConfigManager.config.saveConfigToFile());
	}

}
