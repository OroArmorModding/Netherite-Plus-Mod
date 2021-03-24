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
