package com.oroarmor.netherite_plus.advancement.criterion;

import me.shedaniel.architectury.registry.CriteriaTriggersRegistry;

import net.minecraft.advancement.criterion.Criterion;

public class NetheritePlusCriteria {
    public static final FullNetheriteNetheriteBeaconCriterion FULL_NETHERITE_NETHERITE_BEACON = register(new FullNetheriteNetheriteBeaconCriterion());
    public static final ConstructNetheriteBeaconCriterion CONSTRUCT_NETHERITE_BEACON = register(new ConstructNetheriteBeaconCriterion());
    public static final RiptideNetheriteTridentCriterion RIPTIDE_NETHERITE_TRIDENT = register(new RiptideNetheriteTridentCriterion());

    private static <T extends Criterion<?>> T register(T object) {
        return CriteriaTriggersRegistry.register(object);
    }

    public static void init() {
    }
}
