package com.oroarmor.netherite_plus.entity.effect;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import me.shedaniel.architectury.registry.RegistrySupplier;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusStatusEffects {
    public static final RegistrySupplier<StatusEffect> LAVA_VISION;

    static {
        LAVA_VISION = NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue() ? register("lava_vision", new StatusEffect(StatusEffectType.BENEFICIAL, 16744207)) : null;
    }

    private static RegistrySupplier<StatusEffect> register(String id, StatusEffect entry) {
        return NetheritePlusMod.REGISTRIES.get().get(Registry.MOB_EFFECT_KEY).registerSupplied(id(id), () -> entry);
    }

    public static void init() {
    }

}
