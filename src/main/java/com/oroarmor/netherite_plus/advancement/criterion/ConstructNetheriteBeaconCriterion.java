/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
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

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.google.gson.JsonObject;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ConstructNetheriteBeaconCriterion extends SimpleCriterionTrigger<ConstructNetheriteBeaconCriterion.Conditions> {
    private static final ResourceLocation ID = id("construct_netherite_beacon");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Conditions createInstance(JsonObject jsonObject, ContextAwarePredicate extended, DeserializationContext advancementEntityPredicateDeserializer) {
        MinMaxBounds.Ints intRange = MinMaxBounds.Ints.fromJson(jsonObject.get("level"));
        return new Conditions(extended, intRange);
    }

    public void trigger(ServerPlayer player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (conditions) -> conditions.matches(beacon));
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {
        private final MinMaxBounds.Ints level;

        public Conditions(ContextAwarePredicate player, MinMaxBounds.Ints level) {
            super(ConstructNetheriteBeaconCriterion.ID, player);
            this.level = level;
        }

        public static Conditions level(MinMaxBounds.Ints level) {
            return new Conditions(ContextAwarePredicate.ANY, level);
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return level.matches(beacon.getBeaconLevel());
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            JsonObject jsonObject = super.serializeToJson(predicateSerializer);
            jsonObject.add("level", level.serializeToJson());
            return jsonObject;
        }
    }
}
