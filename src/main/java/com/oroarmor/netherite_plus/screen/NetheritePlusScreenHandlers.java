package com.oroarmor.netherite_plus.screen;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry.Factory;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;

public class NetheritePlusScreenHandlers {

	public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;

	public static void initializeMod() {
		NETHERITE_ANVIL = ScreenHandlerRegistry.registerSimple(id("netherite_anvil"), NetheriteAnvilScreenHandler::new);
	}

	@Environment(EnvType.CLIENT)
	public static void initializeClient() {
		ScreenRegistry.register(NetheritePlusScreenHandlers.NETHERITE_ANVIL,
				(Factory<NetheriteAnvilScreenHandler, NetheriteAnvilScreen>) (handler, inventory,
						title) -> new NetheriteAnvilScreen(handler, inventory, title));
	}
}
