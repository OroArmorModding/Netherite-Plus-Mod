package com.oroarmor.netherite_plus.screen;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteBeaconScreen;
import com.oroarmor.util.init.Initable;
import me.shedaniel.architectury.registry.MenuRegistry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusScreenHandlers implements Initable {

	public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;
	public static ScreenHandlerType<NetheriteBeaconScreenHandler> NETHERITE_BEACON;

	static {
		NETHERITE_ANVIL = register(id("netherite_anvil"), NetheriteAnvilScreenHandler::new);
		NETHERITE_BEACON = register(id("netherite_beacon"), NetheriteBeaconScreenHandler::new);
	}

	public static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, MenuRegistry.SimpleMenuTypeFactory<T> factory) {
		ScreenHandlerType<T> type = MenuRegistry.of(factory);
		NetheritePlusMod.REGISTRIES.get().get(Registry.MENU_KEY).registerSupplied(id, () -> type);
		return type;
	}

	public static void init() {
	}

	@Environment(EnvType.CLIENT)
	public static void initializeClient() {
		MenuRegistry.registerScreenFactory(NetheritePlusScreenHandlers.NETHERITE_ANVIL, NetheriteAnvilScreen::new);
		MenuRegistry.registerScreenFactory(NetheritePlusScreenHandlers.NETHERITE_BEACON, NetheriteBeaconScreen::new);
	}
}
