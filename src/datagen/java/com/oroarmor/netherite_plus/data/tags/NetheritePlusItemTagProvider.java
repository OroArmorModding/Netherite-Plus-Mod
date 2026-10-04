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
import com.oroarmor.netherite_plus.tags.NetheritePlusItemTags;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItemIds;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;

public class NetheritePlusItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public NetheritePlusItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, BlockTagsProvider blockTagsProvider) {
        super(output, registryLookupFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.copy(NetheritePlusBlockTags.NETHERITE_SHULKER_BOXES, NetheritePlusItemTags.NETHERITE_SHULKER_BOXES);
        this.copy(NetheritePlusBlockTags.DYED_NETHERITE_SHULKER_BOXES, NetheritePlusItemTags.DYED_NETHERITE_SHULKER_BOXES);
        this.tag(ConventionalItemTags.SHULKER_BOXES).addTag(NetheritePlusItemTags.NETHERITE_SHULKER_BOXES);

        this.tag(ItemTags.BOW_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_BOW);
        this.tag(ItemTags.SKELETON_PREFERRED_WEAPONS).add(NetheritePlusItemIds.NETHERITE_BOW);
        this.tag(ConventionalItemTags.BOW_TOOLS).add(NetheritePlusItemIds.NETHERITE_BOW);

        this.tag(ItemTags.CROSSBOW_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_CROSSBOW);
        this.tag(ItemTags.PIGLIN_PREFERRED_WEAPONS).add(NetheritePlusItemIds.NETHERITE_CROSSBOW);
        this.tag(ItemTags.PILLAGER_PREFERRED_WEAPONS).add(NetheritePlusItemIds.NETHERITE_CROSSBOW);
        this.tag(ConventionalItemTags.CROSSBOW_TOOLS).add(NetheritePlusItemIds.NETHERITE_CROSSBOW);

        this.tag(ItemTags.WITHER_SKELETON_DISLIKED_WEAPONS)
                .add(NetheritePlusItemIds.NETHERITE_BOW)
                .add(NetheritePlusItemIds.NETHERITE_CROSSBOW);

        this.tag(ItemTags.FISHING_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_FISHING_ROD);
        this.tag(ConventionalItemTags.FISHING_ROD_TOOLS).add(NetheritePlusItemIds.NETHERITE_FISHING_ROD);

        this.tag(ItemTags.MINING_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_SHEARS);
        this.tag(ConventionalItemTags.SHEAR_TOOLS).add(NetheritePlusItemIds.NETHERITE_SHEARS);

        this.tag(ConventionalItemTags.SHIELD_TOOLS).add(NetheritePlusItemIds.NETHERITE_SHIELD);

        this.tag(ItemTags.TRIDENT_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_TRIDENT);
        this.tag(ItemTags.DROWNED_PREFERRED_WEAPONS).add(NetheritePlusItemIds.NETHERITE_TRIDENT);
        this.tag(ItemTags.BREAKS_DECORATED_POTS).add(NetheritePlusItemIds.NETHERITE_TRIDENT);

        this.tag(ItemTags.DURABILITY_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_BOW)
                .add(NetheritePlusItemIds.NETHERITE_CROSSBOW)
                .add(NetheritePlusItemIds.NETHERITE_FISHING_ROD)
                .add(NetheritePlusItemIds.NETHERITE_SHEARS)
                .add(NetheritePlusItemIds.NETHERITE_TRIDENT)
                .add(NetheritePlusItemIds.NETHERITE_SHIELD);

        this.tag(ItemTags.CHEST_ARMOR_ENCHANTABLE).add(NetheritePlusItemIds.NETHERITE_ELYTRA);
    }
}
