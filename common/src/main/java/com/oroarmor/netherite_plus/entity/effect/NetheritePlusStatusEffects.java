/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
