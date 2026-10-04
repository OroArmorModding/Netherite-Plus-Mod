/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.advancements.triggers;

import net.minecraft.advancements.triggers.CriterionTrigger;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

public class NetheritePlusCriteriaTriggers {
    public static final ConstructNetheriteBeaconTrigger CONSTRUCT_NETHERITE_BEACON = register(ConstructNetheriteBeaconTrigger.ID, new ConstructNetheriteBeaconTrigger());
    public static final NetheriteBlocksNetheriteBeaconTrigger FULL_NETHERITE_NETHERITE_BEACON = register(NetheriteBlocksNetheriteBeaconTrigger.ID, new NetheriteBlocksNetheriteBeaconTrigger());
    public static final RiptideNetheriteTridentTrigger RIPTIDE_NETHERITE_TRIDENT = register(RiptideNetheriteTridentTrigger.ID, new RiptideNetheriteTridentTrigger());

    private static <T extends CriterionTrigger<?>> T register(Identifier id, T criterion) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES, id, criterion);
    }

    public static void init() {
    }
}
