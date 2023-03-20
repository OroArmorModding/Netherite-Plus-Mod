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

import com.google.gson.JsonObject;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class ConstructNetheriteBeaconCriterion extends AbstractCriterion<ConstructNetheriteBeaconCriterion.Conditions> {
    private static final Identifier ID = id("construct_netherite_beacon");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public Conditions conditionsFromJson(JsonObject jsonObject, EntityPredicate.Extended extended, AdvancementEntityPredicateDeserializer advancementEntityPredicateDeserializer) {
        NumberRange.IntRange intRange = NumberRange.IntRange.fromJson(jsonObject.get("level"));
        return new Conditions(extended, intRange);
    }

    public void trigger(ServerPlayerEntity player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (conditions) -> conditions.matches(beacon));
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final NumberRange.IntRange level;

        public Conditions(EntityPredicate.Extended player, NumberRange.IntRange level) {
            super(ConstructNetheriteBeaconCriterion.ID, player);
            this.level = level;
        }

        public static Conditions level(NumberRange.IntRange level) {
            return new Conditions(EntityPredicate.Extended.EMPTY, level);
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return level.test(beacon.getBeaconLevel());
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("level", level.toJson());
            return jsonObject;
        }
    }
}
