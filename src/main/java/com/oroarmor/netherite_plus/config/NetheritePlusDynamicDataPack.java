package com.oroarmor.netherite_plus.config;

import static net.devtech.arrp.api.RuntimeResourcePack.id;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.tags.JTag;
import net.minecraft.util.Identifier;

public class NetheritePlusDynamicDataPack {

	public static final RuntimeResourcePack DataPack = RuntimeResourcePack.create("netherite_plus:dynamic_datapack");

	public static void configureDynamicDataPack() {
		if (NetheritePlusConfig.ENABLED.ENABLED_FAKE_NETHERITE_BLOCKS.getValue()) {
			DataPack.addTag(id("minecraft:blocks/beacon_base_blocks"),
					JTag.tag().add(new Identifier("netherite_plus", "fake_netherite_block")));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_ANVIL.getValue()) {
			DataPack.addTag(id("minecraft:blocks/anvil"),
					JTag.tag().add(new Identifier("netherite_plus", "netherite_anvil")));
		}

		RRPCallback.EVENT.register(a -> a.add(DataPack));
	}

}
