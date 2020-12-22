package com.oroarmor.netherite_plus.entity.effect;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.init.Initable;
import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NetheritePlusStatusEffects implements Initable {

	public static final MobEffect LAVA_VISION;

	private static MobEffect register(int rawId, String id, MobEffect entry) {
		return Registry.registerMapping(Registry.MOB_EFFECT, rawId, id, entry);
	}

	static {
		LAVA_VISION = NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue() ? register(33, "lava_vision", new MobEffect(MobEffectCategory.BENEFICIAL, 16744207)) : null;
	}

	public static void init() {
	}

}
