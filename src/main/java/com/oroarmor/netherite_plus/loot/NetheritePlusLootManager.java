package com.oroarmor.netherite_plus.loot;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.fabricmc.fabric.api.loot.v1.LootEntryTypeRegistry;
import net.minecraft.util.Identifier;

public class NetheritePlusLootManager {

	public static final Identifier LAVA_FISHING_LOOT_TABLE = id("gameplay/fishing");

	public static void init() {
		LootEntryTypeRegistry.INSTANCE.register(id("gameplay/fishing"), null);
	}
}
