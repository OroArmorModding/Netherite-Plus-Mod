package com.oroarmor.netherite_plus.client;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.logging.log4j.Level;

public class ForgeNetheritePlusModClient {
    @SubscribeEvent
    public void registerClient(FMLClientSetupEvent event) {
        System.out.println("Initializing Client");
        NetheritePlusScreenHandlers.init();
        NetheritePlusScreenHandlers.initializeClient();

        System.out.println("Initialization Finalized.");
    }
}
