package com.oroarmor.netherite_plus.advancement.criterion;

import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.util.init.Initable;
import net.minecraft.advancements.CriterionTrigger;

public class NetheritePlusCriteria implements Initable {
	public static final FullNetheriteNetheriteBeaconCriterion FULL_NETHERITE_NETHERITE_BEACON = register(new FullNetheriteNetheriteBeaconCriterion());
	public static final ConstructNetheriteBeaconCriterion CONSTRUCT_NETHERITE_BEACON = register(new ConstructNetheriteBeaconCriterion());
	public static final RiptideNetheriteTridentCriterion RIPTIDE_NETHERITE_TRIDENT = register(new RiptideNetheriteTridentCriterion());

	private static <T extends CriterionTrigger<?>> T register(T object) {
		return NetheritePlusModPlatform.registerCriteria(object);
	}

	public static void init() {
	}
}
