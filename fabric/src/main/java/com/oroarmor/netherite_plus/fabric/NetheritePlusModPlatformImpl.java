package com.oroarmor.netherite_plus.fabric;

import java.io.File;

import com.oroarmor.netherite_plus.compatibility.NetheritePlusTrinketsCompatibilty;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import me.shedaniel.architectury.ExpectPlatform;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.fabricmc.loader.api.FabricLoader;
import static com.oroarmor.netherite_plus.config.NetheritePlusConfig.CONFIG_FILE_NAME;

public class NetheritePlusModPlatformImpl {
    public static void sendBeaconUpdatePacket(FriendlyByteBuf buf){
        ClientPlayNetworking.send(UpdateNetheriteBeaconC2SPacket.ID, buf);
    }

    public static <T extends CriterionTrigger<?>> T registerCriteria(T object) {
        return CriteriaAccessor.callRegister(object);
    }

    public static Item getElytraItem(Item.Properties elytraSettings) {
        return !FabricLoader.getInstance().isModLoaded("trinkets") ? new NetheriteElytraItem(elytraSettings) : NetheritePlusTrinketsCompatibilty.getTrinketsElytra(elytraSettings);
    }

    public static File getConfigDir() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME);
    }
}
