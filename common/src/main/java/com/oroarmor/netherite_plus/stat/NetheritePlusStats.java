package com.oroarmor.netherite_plus.stat;

import com.oroarmor.netherite_plus.NetheritePlusMod;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusStats {
    public static final ResourceLocation FLY_NETHERITE_ELYTRA = register("netherite_elytra_flight_distance", StatFormatter.DISTANCE);

    private static ResourceLocation register(String string, StatFormatter statFormatter) {
        ResourceLocation identifier = id(string);
        Registry.register(Registry.CUSTOM_STAT, identifier, identifier);
        Stats.CUSTOM.get(identifier, statFormatter);
        return identifier;
    }

    public static void init() {
    }
}
