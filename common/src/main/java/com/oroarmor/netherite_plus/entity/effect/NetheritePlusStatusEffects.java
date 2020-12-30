package com.oroarmor.netherite_plus.entity.effect;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.init.Initable;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NetheritePlusStatusEffects implements Initable {

    public static final MobEffect LAVA_VISION;

    private static MobEffect register(String id, MobEffect entry) {
        return NetheritePlusMod.REGISTRIES.get().get(Registry.MOB_EFFECT_REGISTRY).register(new ResourceLocation(id), () -> entry).get();
    }

    static {
        LAVA_VISION = NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue() ? register("lava_vision", new MobEffect(MobEffectCategory.BENEFICIAL, 16744207)) : null;
    }

    public static void init() {
    }

}
