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

package com.oroarmor.netherite_plus.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import com.oroarmor.netherite_plus.stats.NetheritePlusStats;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Definition(id = "AVIATE_ONE_CM", field = "Lnet/minecraft/stats/Stats;AVIATE_ONE_CM:Lnet/minecraft/resources/Identifier;")
    @Definition(id = "awardStat", method = "Lnet/minecraft/server/level/ServerPlayer;awardStat(Lnet/minecraft/resources/Identifier;I)V")
    @Expression("this.awardStat(AVIATE_ONE_CM, ?)")
    @WrapOperation(method = "checkMovementStatistics", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private void increaseTravelMotionStats(ServerPlayer instance, Identifier identifier, int horizontalDistance, Operation<Void> original) {
        boolean hasNetheriteElytra = false;
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            hasNetheriteElytra |= slot.isArmor() && this.equipment.get(slot).is(NetheritePlusItems.NETHERITE_ELYTRA);
        }
        if (hasNetheriteElytra) {
            original.call(instance, NetheritePlusStats.FLY_NETHERITE_ELYTRA_ONE_CM, horizontalDistance);
        }

        original.call(instance, identifier, horizontalDistance);
    }
}
