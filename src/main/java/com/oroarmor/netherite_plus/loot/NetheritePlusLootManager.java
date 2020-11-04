package com.oroarmor.netherite_plus.loot;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.util.init.Initable;

import net.fabricmc.fabric.api.loot.v1.LootEntryTypeRegistry;
import net.minecraft.util.Identifier;

public class NetheritePlusLootManager implements Initable {

	public static final Identifier LAVA_FISHING_LOOT_TABLE = id("gameplay/fishing");

	public static void init() {
		LootEntryTypeRegistry.INSTANCE.register(id("gameplay/fishing"), null);
	}
}
