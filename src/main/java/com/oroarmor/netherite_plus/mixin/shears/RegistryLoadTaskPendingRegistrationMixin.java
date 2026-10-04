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

package com.oroarmor.netherite_plus.mixin.shears;

import java.util.Optional;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.level.storage.loot.predicates.LootPredicates;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

@Mixin(targets = "net/minecraft/resources/RegistryLoadTask$PendingRegistration")
public class RegistryLoadTaskPendingRegistrationMixin {

    @SuppressWarnings("unchecked")
    @WrapOperation(method = "loadFromResource", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/DataResult;getOrThrow()Ljava/lang/Object;"))
    private static <R> R injectNetheriteShears(DataResult<R> instance, Operation<R> original, final Decoder<R> elementDecoder, final RegistryOps<JsonElement> ops, final ResourceKey<R> elementKey, final Resource thunk) {
        R result = original.call(instance);

        if (elementKey.isFor(Registries.PREDICATE) && elementKey.identifier().equals(LootPredicates.TOOL_CAN_SHEAR.identifier())) {
            MatchTool match = (MatchTool) result;
            if (match.predicate().isPresent()) {
                ItemPredicate predicate = match.predicate().get();
                var items = Stream.concat(
                        predicate.items().orElse(HolderSet.empty()).stream(),
                        Stream.of(NetheritePlusItems.NETHERITE_SHEARS.builtInRegistryHolder())
                ).toList();
                MatchTool newMatch = new MatchTool(
                        Optional.of(
                                new ItemPredicate(
                                        Optional.of(HolderSet.direct(items)),
                                        predicate.count(),
                                        predicate.components()
                                )
                        )
                );
                return (R) newMatch;
            }
        }

        return result;
    }
}
