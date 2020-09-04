package com.oroarmor.netherite_plus.advancement.criterion;

import com.oroarmor.netherite_plus.mixin.CriteriaAccessor;

import net.minecraft.advancement.criterion.Criterion;

public class NetheritePlusCriteria {
	public static final FullNetheriteNetheriteBeaconCriterion FULL_NETHERITE_NETHERITE_BEACON = register(
			new FullNetheriteNetheriteBeaconCriterion());
	public static final ConstructNetheriteBeaconCriterion CONSTRUCT_NETHERITE_BEACON = register(
			new ConstructNetheriteBeaconCriterion());
	public static final RiptideNetheriteTridentCriterion RIPTIDE_NETHERITE_TRIDENT = register(
			new RiptideNetheriteTridentCriterion());

	private static <T extends Criterion<?>> T register(T object) {
		return CriteriaAccessor.callRegister(object);
	}

	public static void init() {
	}

}
