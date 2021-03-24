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

package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.stat.NetheritePlusStats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "increaseTravelMotionStats", at = @At("RETURN"))
    private void increaseTravelMotionStats(double dx, double dy, double dz, CallbackInfo info) {
        if (NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue() && !((PlayerEntity) (Object) this).hasVehicle()) {
            if (((PlayerEntity) (Object) this).isFallFlying()) {
                boolean hasNetheriteElytra = false;
                for (ItemStack item : ((PlayerEntity) (Object) this).getArmorItems()) {
                    hasNetheriteElytra |= item.getItem() == NetheritePlusItems.NETHERITE_ELYTRA.get();
                }
                if (!hasNetheriteElytra) {
                    return;
                }

                int l = Math.round(MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 100.0F);
                ((PlayerEntity) (Object) this).increaseStat(NetheritePlusStats.FLY_NETHERITE_ELYTRA, l);
            }
        }
    }
}
