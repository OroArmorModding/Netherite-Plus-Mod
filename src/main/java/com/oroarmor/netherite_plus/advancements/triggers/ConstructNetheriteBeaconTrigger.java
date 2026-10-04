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
import com.oroarmor.netherite_plus.world.level.block.entity.NetheriteBeaconBlockEntity;

import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.SimpleCriterionTrigger;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class ConstructNetheriteBeaconTrigger extends SimpleCriterionTrigger<ConstructNetheriteBeaconTrigger.TriggerInstance> {
    public static final Identifier ID = id("construct_netherite_beacon");

    public void trigger(ServerPlayer player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (trigger) -> trigger.matches(beacon));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, MinMaxBounds.Ints level) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<ConstructNetheriteBeaconTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(ConstructNetheriteBeaconTrigger.TriggerInstance::player),
                        MinMaxBounds.Ints.CODEC.optionalFieldOf("level", MinMaxBounds.Ints.ANY).forGetter(ConstructNetheriteBeaconTrigger.TriggerInstance::level)
                ).apply(instance, ConstructNetheriteBeaconTrigger.TriggerInstance::new)
        );

        public static Criterion<ConstructNetheriteBeaconTrigger.TriggerInstance> constructedBeacon(final MinMaxBounds.Ints level) {
            return NetheritePlusCriteriaTriggers.CONSTRUCT_NETHERITE_BEACON.createCriterion(new ConstructNetheriteBeaconTrigger.TriggerInstance(Optional.empty(), level));
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return this.level.matches(beacon.getBeaconLevel());
        }
    }
}
