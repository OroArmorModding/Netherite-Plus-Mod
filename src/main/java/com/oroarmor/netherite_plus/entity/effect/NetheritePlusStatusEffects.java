package com.oroarmor.netherite_plus.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public class NetheritePlusStatusEffects {

	public static final StatusEffect LAVA_VISION;

	private static StatusEffect register(int rawId, String id, StatusEffect entry) {
		return Registry.register(Registry.STATUS_EFFECT, rawId, id, entry);
	}

	static {
		LAVA_VISION = register(33, "lava_vision", new StatusEffect(StatusEffectType.BENEFICIAL, 16744207));
	}

	public static void init() {
	}

}
