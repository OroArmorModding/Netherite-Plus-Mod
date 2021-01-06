package com.oroarmor.netherite_plus.entity.effect;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.init.Initable;
import me.shedaniel.architectury.registry.RegistrySupplier;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NetheritePlusStatusEffects implements Initable {

    public static final RegistrySupplier<MobEffect> LAVA_VISION;

    private static RegistrySupplier<MobEffect> register(String id, MobEffect entry) {
        return NetheritePlusMod.REGISTRIES.get().get(Registry.MOB_EFFECT_REGISTRY).registerSupplied(id(id), () -> entry);
    }

    static {
        LAVA_VISION = NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue() ? register("lava_vision", new MobEffect(MobEffectCategory.BENEFICIAL, 16744207)) : null;
    }

    public static void init() {
    }

}
