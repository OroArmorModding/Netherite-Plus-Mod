package com.oroarmor.netherite_plus.loot;

import com.oroarmor.util.init.Initable;

import net.minecraft.resources.ResourceLocation;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusLootManager implements Initable {

    public static final ResourceLocation LAVA_FISHING_LOOT_TABLE = id("gameplay/fishing");

    public static void init() {
//       REGISTRIES.get().get(Registry.LOOT_ENTRY_REGISTRY).register(id("gameplay/fishing"), () -> new LootPoolEntryType(null));
    }
}
