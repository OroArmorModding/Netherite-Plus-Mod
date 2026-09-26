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

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class RiptideNetheriteTridentCriterion extends SimpleCriterionTrigger<RiptideNetheriteTridentCriterion.Conditions> {
    public static final ResourceLocation id = id("riptide_netherite_trident");

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public Conditions createInstance(JsonObject jsonObject, ContextAwarePredicate extended, DeserializationContext advancementEntityPredicateDeserializer) {
        return new Conditions(extended);
    }

    public void trigger(ServerPlayer player) {
        this.trigger(player, (conditions) -> conditions.matches(player));
    }

    public static class Conditions extends AbstractCriterionTriggerInstance {

        public Conditions(ContextAwarePredicate player) {
            super(id, player);
        }

        public boolean matches(ServerPlayer player) {
            return true;
        }

        @Override
        public JsonObject serializeToJson(SerializationContext predicateSerializer) {
            return super.serializeToJson(predicateSerializer);
        }

    }
}
