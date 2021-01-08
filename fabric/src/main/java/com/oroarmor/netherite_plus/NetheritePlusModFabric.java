package com.oroarmor.netherite_plus;

import java.io.IOException;

import com.oroarmor.config.command.ConfigCommand;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class NetheritePlusModFabric implements ModInitializer {
    public void onInitialize() {
        NetheritePlusMod.initialize();

        ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusMod.CONFIG.saveConfigToFile());

        CommandRegistrationCallback.EVENT.register(new ConfigCommand(NetheritePlusMod.CONFIG));

        NetheritePlusMod.registerItemsWithMultiItemLib();
    }
}
