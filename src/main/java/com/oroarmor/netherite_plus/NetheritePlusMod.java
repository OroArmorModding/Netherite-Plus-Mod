package com.oroarmor.netherite_plus;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oroarmor.netherite_plus.combatibility.QuickShulkerHook;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.config.NetheritePlusDynamicDataPack;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;
import com.oroarmor.util.config.ConfigItemGroup;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class NetheritePlusMod implements ModInitializer {

	public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;

	public static final NetheritePlusConfig CONFIG = new NetheritePlusConfig();

	public static final Logger LOGGER = LogManager.getLogger("Netherite Plus");

	@Override
	public void onInitialize() {
		CONFIG.readConfigFromFile();

		if (NetheritePlusConfig.ENABLED.ENABLED_CONFIG_PRINT.getValue()) {
			CONFIG.getConfigs().stream().map(ConfigItemGroup::getConfigs)
					.forEach(l -> l.forEach(ci -> LOGGER.log(Level.INFO, ci.toString())));
		}

		if (FabricLoader.getInstance().isModLoaded("quickshulker")) {
			new QuickShulkerHook().registerProviders();
		}

		NetheritePlusItems.registerItems();
		NetheritePlusDynamicDataPack.configureDynamicDataPack();

		NETHERITE_ANVIL = ScreenHandlerRegistry.registerSimple(new Identifier("netherite_plus", "netherite_anvil"),
				NetheriteAnvilScreenHandler::new);

		NetheritePlusRecipeSerializer.init();

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> CONFIG.saveConfigToFile());
	}

}
