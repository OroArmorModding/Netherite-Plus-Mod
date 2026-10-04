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

package com.oroarmor.netherite_plus.world.item;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class NetheritePlusItemIds {
    public static final ResourceKey<Item> NETHERITE_ELYTRA = ResourceKey.create(Registries.ITEM, id("netherite_elytra"));

    public static final ResourceKey<Item> NETHERITE_FISHING_ROD = ResourceKey.create(Registries.ITEM, id("netherite_fishing_rod"));

    public static final ResourceKey<Item> NETHERITE_SHIELD = ResourceKey.create(Registries.ITEM, id("netherite_shield"));

    public static final ResourceKey<Item> NETHERITE_BOW = ResourceKey.create(Registries.ITEM, id("netherite_bow"));

    public static final ResourceKey<Item> NETHERITE_CROSSBOW = ResourceKey.create(Registries.ITEM, id("netherite_crossbow"));

    public static final ResourceKey<Item> NETHERITE_TRIDENT = ResourceKey.create(Registries.ITEM, id("netherite_trident"));

    public static final ResourceKey<Item> NETHERITE_SHEARS = ResourceKey.create(Registries.ITEM, id("netherite_shears"));
}
