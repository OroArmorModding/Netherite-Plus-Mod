package com.oroarmor.netherite_plus.entity.effect;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public class NetheritePlusStatusEffects {

	public static final StatusEffect LAVA_VISION;

	private static StatusEffect register(int rawId, String id, StatusEffect entry) {
		return Registry.register(Registry.STATUS_EFFECT, rawId, id, entry);
	}

	static {
		LAVA_VISION = NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue() ? register(33, "lava_vision", new StatusEffect(StatusEffectType.BENEFICIAL, 16744207)) : null;
	}

	public static void init() {
	}

}
