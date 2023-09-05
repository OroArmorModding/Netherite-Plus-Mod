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

package com.oroarmor.netherite_plus.item;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import org.quiltmc.qsl.item.extensions.api.crossbow.CrossbowShotProjectileEvents;
import org.quiltmc.qsl.item.extensions.api.crossbow.ProjectileModifyingCrossbowItem;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;

public class NetheriteCrossbowItem extends ProjectileModifyingCrossbowItem {
    public NetheriteCrossbowItem(Settings settings) {
        super(settings);
        CrossbowShotProjectileEvents.CROSSBOW_MODIFY_SHOT_PROJECTILE.register(this);
    }

    @Override
    public void onProjectileShot(ItemStack crossbowStack, ItemStack arrowStack, LivingEntity user, PersistentProjectileEntity projectile) {
        projectile.setDamage(projectile.getDamage() * NetheritePlusMod.CONFIG.damage.crossbow_damage_multiplier.value() + NetheritePlusMod.CONFIG.damage.crossbow_damage_addition.value());
    }
}
