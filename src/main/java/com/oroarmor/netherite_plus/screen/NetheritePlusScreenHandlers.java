package com.oroarmor.netherite_plus.screen;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteBeaconScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class NetheritePlusScreenHandlers {

	public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;
	public static ScreenHandlerType<NetheriteBeaconScreenHandler> NETHERITE_BEACON;

	public static void initializeMod() {
		NETHERITE_ANVIL = ScreenHandlerRegistry.registerSimple(id("netherite_anvil"), NetheriteAnvilScreenHandler::new);
		NETHERITE_BEACON = ScreenHandlerRegistry.registerSimple(id("netherite_beacon"),
				NetheriteBeaconScreenHandler::new);
	}

	@Environment(EnvType.CLIENT)
	public static void initializeClient() {
		ScreenRegistry.register(NetheritePlusScreenHandlers.NETHERITE_ANVIL, NetheriteAnvilScreen::new);

		ScreenRegistry.register(NetheritePlusScreenHandlers.NETHERITE_BEACON, NetheriteBeaconScreen::new);
	}
}
