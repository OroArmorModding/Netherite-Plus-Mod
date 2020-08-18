package com.oroarmor.netherite_plus.loot;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;

import net.fabricmc.fabric.api.loot.v1.LootEntryTypeRegistry;
import net.minecraft.util.Identifier;

public class NetheritePlusLootManager {

	public static void initializeLoot() {
		LootEntryTypeRegistry.INSTANCE.register(new Identifier(MOD_ID, "gameplay/fishing"), null);
	}
}
