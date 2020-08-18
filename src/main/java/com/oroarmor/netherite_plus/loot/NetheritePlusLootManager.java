package com.oroarmor.netherite_plus.loot;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.fabricmc.fabric.api.loot.v1.LootEntryTypeRegistry;

public class NetheritePlusLootManager {

	public static void initializeLoot() {
		LootEntryTypeRegistry.INSTANCE.register(id("gameplay/fishing"), null);
	}
}
