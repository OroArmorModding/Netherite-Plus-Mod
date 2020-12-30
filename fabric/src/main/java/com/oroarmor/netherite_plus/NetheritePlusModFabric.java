package com.oroarmor.netherite_plus;

import java.io.IOException;

import com.oroarmor.config.command.ConfigCommand;
import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import com.oroarmor.netherite_plus.config.NetheritePlusDynamicDataPack;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusModFabric implements ModInitializer {

    public void onInitialize() {
        NetheritePlusMod.initialize();

        ServerLifecycleEvents.SERVER_STOPPED.register(l -> NetheritePlusMod.CONFIG.saveConfigToFile());

        CommandRegistrationCallback.EVENT.register(new ConfigCommand(NetheritePlusMod.CONFIG));

        ServerPlayNetworking.registerGlobalReceiver(UpdateNetheriteBeaconC2SPacket.ID, (server, player, serverGamePacketListener, byteBuffer, packetSender) -> {
            UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket();
            try {
                packet.read(byteBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.execute(() -> {
                if (player.containerMenu instanceof NetheriteBeaconScreenHandler) {
                    ((NetheriteBeaconScreenHandler) player.containerMenu).setEffects(packet.getPrimary(), packet.getSecondary(), packet.getTertiaryEffectId());
                }
            });
        });

        registerItemsWithMultiItemLib();
        NetheritePlusDynamicDataPack.init();
    }

    private void registerItemsWithMultiItemLib() {
        UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD.get());
        UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD.get());
        UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA.get());
        UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW.get());
        UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW.get());
        UniqueItemRegistry.TRIDENT.addItemToRegistry(NETHERITE_TRIDENT.get());
    }
}
