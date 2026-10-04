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

public class NetheriteBlocksNetheriteBeaconTrigger extends SimpleCriterionTrigger<NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance> {
    public static final Identifier ID = id("netherite_blocks_netherite_beacon");

    public void trigger(ServerPlayer player, NetheriteBeaconBlockEntity beacon) {
        this.trigger(player, (instance) -> instance.matches(beacon));
    }

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    public record TriggerInstance(Optional<Holder<LootItemCondition>> player, MinMaxBounds.Ints netheriteLevel) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(
                (instance) -> instance.group(
                        LootItemCondition.CODEC.optionalFieldOf("player").forGetter(NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance::player),
                        MinMaxBounds.Ints.CODEC.optionalFieldOf("netherite_level", MinMaxBounds.Ints.ANY).forGetter(NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance::netheriteLevel)
                ).apply(instance, NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance::new)
        );


        public static Criterion<NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance> netheriteBlocks(final MinMaxBounds.Ints level) {
            return NetheritePlusCriteriaTriggers.FULL_NETHERITE_NETHERITE_BEACON.createCriterion(new NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance(Optional.empty(), level));
        }

        public boolean matches(NetheriteBeaconBlockEntity beacon) {
            return netheriteLevel.matches(beacon.getNetheriteLevel());
        }
    }
}
