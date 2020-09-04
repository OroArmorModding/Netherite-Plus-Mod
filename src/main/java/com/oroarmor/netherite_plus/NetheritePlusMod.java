package com.oroarmor.netherite_plus;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.command.NetheritePlusCommand;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.config.NetheritePlusDynamicDataPack;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.loot.NetheritePlusLootManager;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import com.oroarmor.util.config.ConfigItemGroup;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;

public class NetheritePlusMod implements ModInitializer {

	public static final NetheritePlusConfig CONFIG = new NetheritePlusConfig();

	public static final Logger LOGGER = LogManager.getLogger("Netherite Plus");

	public static final String MOD_ID = "netherite_plus";

	@Override
	public void onInitialize() {
		processConfig();

		NetheritePlusItems.init();
		NetheritePlusDynamicDataPack.init();
		NetheritePlusScreenHandlers.init();
		NetheritePlusLootManager.init();
		NetheritePlusRecipeSerializer.init();
		NetheritePlusStatusEffects.init();
		NetheritePlusCriteria.init();

		CommandRegistrationCallback.EVENT.register(new NetheritePlusCommand());
	}

	private void processConfig() {
		CONFIG.readConfigFromFile();

		if (NetheritePlusConfig.ENABLED.ENABLED_CONFIG_PRINT.getValue()) {
			CONFIG.getConfigs().stream().map(ConfigItemGroup::getConfigs)
					.forEach(l -> l.forEach(ci -> LOGGER.log(Level.INFO, ci.toString())));
		}

		ServerLifecycleEvents.SERVER_STOPPED.register(l -> CONFIG.saveConfigToFile());
	}

	public static Identifier id(String id) {
		return new Identifier(MOD_ID, id);
	}

}
