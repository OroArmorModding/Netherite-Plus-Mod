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

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class RiptideNetheriteTridentTrigger extends SimpleCriterionTrigger<RiptideNetheriteTridentTrigger.TriggerInstance> {
    public static final Identifier ID = id("riptide_netherite_trident");

    public void trigger(ServerPlayer player) {
        this.trigger(player, TriggerInstance::matches);
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player) implements SimpleCriterionTrigger.SimpleInstance {
        public static final Codec<RiptideNetheriteTridentTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(RiptideNetheriteTridentTrigger.TriggerInstance::player)
                ).apply(instance, RiptideNetheriteTridentTrigger.TriggerInstance::new)
        );

        public static Criterion<RiptideNetheriteTridentTrigger.TriggerInstance> usedRiptide() {
            return NetheritePlusCriteriaTriggers.RIPTIDE_NETHERITE_TRIDENT.createCriterion(new RiptideNetheriteTridentTrigger.TriggerInstance(Optional.empty()));
        }

        public boolean matches() {
            return true;
        }
    }
}
