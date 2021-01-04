package com.oroarmor.netherite_plus;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import com.oroarmor.netherite_plus.client.ForgeNetheritePlusModClient;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryManager;

import net.minecraft.core.Registry;
import net.minecraft.stats.Stat;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_TRIDENT;

@Mod(NetheritePlusMod.MOD_ID)
public class ForgeNetheritePlusMod {
    public ForgeNetheritePlusMod(){
        EventBuses.registerModEventBus(NetheritePlusMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        NetheritePlusMod.initialize();
        FMLJavaModLoadingContext.get().getModEventBus().register(new ForgeNetheritePlusModClient());
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
    }

    @SubscribeEvent
    public void registerItemsWithMultiItemLib(FMLLoadCompleteEvent event){
        UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD.getOrNull());
        UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD.getOrNull());
        UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA.getOrNull());
        UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW.getOrNull());
        UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW.getOrNull());
        UniqueItemRegistry.TRIDENT.addItemToRegistry(NETHERITE_TRIDENT.getOrNull());
    }
}
