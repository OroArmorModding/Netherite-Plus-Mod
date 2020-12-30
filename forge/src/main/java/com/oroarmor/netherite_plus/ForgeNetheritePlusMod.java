package com.oroarmor.netherite_plus;

import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(NetheritePlusMod.MOD_ID)
public class ForgeNetheritePlusMod {
    public ForgeNetheritePlusMod(){
        EventBuses.registerModEventBus(NetheritePlusMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        NetheritePlusMod.initialize();
    }
}
