package com.oroarmor.netherite_plus.screen;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteBeaconScreen;
import com.oroarmor.util.init.Initable;
import me.shedaniel.architectury.hooks.ScreenHooks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;

import net.minecraft.world.inventory.MenuType;

public class NetheritePlusScreenHandlers implements Initable {

    public static MenuType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;
    public static MenuType<NetheriteBeaconScreenHandler> NETHERITE_BEACON;

    static {
        NETHERITE_ANVIL = NetheritePlusModPlatform.registerScreenHandler(id("netherite_anvil"), NetheriteAnvilScreenHandler::new);
        NETHERITE_BEACON = NetheritePlusModPlatform.registerScreenHandler(id("netherite_beacon"), NetheriteBeaconScreenHandler::new);
    }

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void initializeClient() {
        NetheritePlusModPlatform.registerScreen(NetheritePlusScreenHandlers.NETHERITE_ANVIL, NetheriteAnvilScreen::new);
        NetheritePlusModPlatform.registerScreen(NetheritePlusScreenHandlers.NETHERITE_BEACON, NetheriteBeaconScreen::new);
    }
}
