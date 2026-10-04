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

package com.oroarmor.netherite_plus.world.level.block;

import com.oroarmor.netherite_plus.NetheritePlusMod;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.ColorCollection;

public class NetheritePlusBlockItemsIds {
    public static final BlockItemId NETHERITE_SHULKER_BOX = create("netherite_shulker_box");
    public static final ColorCollection<BlockItemId> DYED_NETHERITE_SHULKER_BOX = createSimpleColored("netherite_shulker_box");

    public static final BlockItemId FAKE_NETHERITE_BLOCK = create("fake_netherite_block");

    public static final BlockItemId NETHERITE_ANVIL = create("netherite_anvil");

    public static final BlockItemId NETHERITE_BEACON = create("netherite_beacon");

    private static BlockItemId create(String name) {
        Identifier id = NetheritePlusMod.id(name);
        return BlockItemId.create(id, id);
    }

    private static ColorCollection<BlockItemId> createSimpleColored(final String baseName) {
        return ColorCollection.prefixWithColor(ColorCollection.create(baseName)).map(NetheritePlusBlockItemsIds::create);
    }
}
