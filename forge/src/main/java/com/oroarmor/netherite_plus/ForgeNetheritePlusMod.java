package com.oroarmor.netherite_plus;

import com.oroarmor.netherite_plus.client.ForgeNetheritePlusModClient;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.Level;

@Mod(NetheritePlusMod.MOD_ID)

public class ForgeNetheritePlusMod {
    public ForgeNetheritePlusMod(){
        EventBuses.registerModEventBus(NetheritePlusMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        NetheritePlusMod.initialize();
        FMLJavaModLoadingContext.get().getModEventBus().register(new ForgeNetheritePlusModClient());
    }
}
