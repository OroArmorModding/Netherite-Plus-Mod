package com.oroarmor.netherite_plus;

import java.io.File;

import me.shedaniel.architectury.ExpectPlatform;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;

public class NetheritePlusModPlatform {
    @ExpectPlatform
    public static void sendBeaconUpdatePacket(FriendlyByteBuf buf){
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends CriterionTrigger<?>> T registerCriteria(T object) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Item getElytraItem(Item.Properties elytraSettings) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static File getConfigDir() {
        throw new AssertionError();
    }
}
