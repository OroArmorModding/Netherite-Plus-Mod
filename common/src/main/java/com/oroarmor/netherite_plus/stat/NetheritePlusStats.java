package com.oroarmor.netherite_plus.stat;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NetheritePlusStats {
	public static final Identifier FLY_NETHERITE_ELYTRA = register("netherite_elytra_flight_distance", StatFormatter.DISTANCE);

	private static Identifier register(String string, StatFormatter statFormatter) {
		Identifier identifier = id(string);
		Registry.register(Registry.CUSTOM_STAT, identifier, identifier);
		Stats.CUSTOM.getOrCreateStat(identifier, statFormatter);
		return identifier;
	}

	public static void init() {
	}
}
