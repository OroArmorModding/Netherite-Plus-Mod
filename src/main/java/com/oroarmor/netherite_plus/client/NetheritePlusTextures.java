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

package com.oroarmor.netherite_plus.client;


import net.minecraft.client.resource.Material;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusTextures {
    public static final Identifier NETHERITE_SHULKER_BOXES_ATLAS_TEXTURE = id("textures/atlas/netherite_shulker_boxes.png");
    public static final Identifier NETHERITE_SHIELD_PATTERNS_ATLAS_TEXTURE = id("textures/atlas/netherite_shield_patterns.png");

    public static final Material NETHERITE_SHIELD_BASE = new Material(
            NETHERITE_SHIELD_PATTERNS_ATLAS_TEXTURE, id("entity/netherite_shield_base")
    );
    public static final Material NETHERITE_SHIELD_BASE_NO_PATTERN = new Material(
            NETHERITE_SHIELD_PATTERNS_ATLAS_TEXTURE, id("entity/netherite_shield_base_nopattern")
    );
}
