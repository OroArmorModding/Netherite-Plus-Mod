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

package com.oroarmor.netherite_plus.client.renderer;


import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SpriteMapper;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;

public class NetheritePlusSheets {
    public static final SpriteMapper NETHERITE_SHIELD_MAPPER = new SpriteMapper(Sheets.SHIELD_SHEET, "entity/netherite_shield");
    public static final SpriteId NETHERITE_SHIELD_BASE = NETHERITE_SHIELD_MAPPER.apply(id("netherite_shield_base"));
    public static final SpriteId NETHERITE_SHIELD_BASE_NO_PATTERN = NETHERITE_SHIELD_MAPPER.apply(id("netherite_shield_base_nopattern"));
    public static final SpriteId NETHERITE_SHIELD_PATTERN_BASE = NETHERITE_SHIELD_MAPPER.apply(id("base"));

    public static final SpriteMapper NETHERITE_SHULKER_MAPPER = new SpriteMapper(Sheets.SHULKER_SHEET, "entity/netherite_shulker");
    public static final SpriteId DEFAULT_NETHERITE_SHULKER_TEXTURE_LOCATION = NETHERITE_SHULKER_MAPPER.apply(id("netherite_shulker"));
    public static final List<SpriteId> NETHERITE_SHULKER_TEXTURE_LOCATION = Arrays.stream(DyeColor.values())
            .sorted(Comparator.comparingInt(DyeColor::getId))
            .map(NetheritePlusSheets::createShulkerSprite)
            .collect(ImmutableList.toImmutableList());

    public static final Identifier NETHERITE_TRIDENT = id("textures/entity/netherite_trident/netherite_trident.png");


    public static SpriteId createShulkerSprite(final DyeColor color) {
        return NETHERITE_SHULKER_MAPPER.apply(colorToShulkerSprite(color));
    }

    public static Identifier colorToShulkerSprite(final DyeColor color) {
        return id("netherite_shulker_" + color.getName());
    }

    public static void init() {
    }
}
