package com.oroarmor.netherite_plus;

import me.shedaniel.architectury.annotations.ExpectPlatform;

import net.minecraft.item.Item;

public class NetheritePlusModPlatform {
    @ExpectPlatform
    public static Item.Settings setISTER(Item.Settings properties) {
        throw new AssertionError();
    }
}
