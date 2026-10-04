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

package com.oroarmor.netherite_plus.data.tags;

import java.util.concurrent.CompletableFuture;

import com.oroarmor.netherite_plus.tags.NetheritePlusBlockTags;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlockItemsIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemId;
import net.minecraft.tags.BlockTags;

public class NetheritePlusBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public NetheritePlusBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.builder(NetheritePlusBlockTags.NETHERITE_SHULKER_BOXES)
                .add(NetheritePlusBlockItemsIds.NETHERITE_SHULKER_BOX)
                .addAll(NetheritePlusBlockItemsIds.DYED_NETHERITE_SHULKER_BOX.map(BlockItemId::block));

        this.builder(NetheritePlusBlockTags.DYED_NETHERITE_SHULKER_BOXES)
                .addAll(NetheritePlusBlockItemsIds.DYED_NETHERITE_SHULKER_BOX.map(BlockItemId::block));


        this.builder(BlockTags.BEACON_BASE_BLOCKS)
                .add(NetheritePlusBlockItemsIds.FAKE_NETHERITE_BLOCK.block());

        this.builder(BlockTags.ANVIL)
                .add(NetheritePlusBlockItemsIds.NETHERITE_ANVIL.block());
    }
}
