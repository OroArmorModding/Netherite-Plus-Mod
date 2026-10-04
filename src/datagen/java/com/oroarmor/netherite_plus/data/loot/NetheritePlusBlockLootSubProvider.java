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

package com.oroarmor.netherite_plus.data.loot;

import java.util.concurrent.CompletableFuture;

import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlocks;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;

public class NetheritePlusBlockLootSubProvider extends FabricBlockLootSubProvider {
    public NetheritePlusBlockLootSubProvider(FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(packOutput, registriesFuture);
    }

    @Override
    public void generate() {
        this.dropSelf(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK);
        this.add(NetheritePlusBlocks.NETHERITE_BEACON, this.createNameableBlockEntityTable(NetheritePlusBlocks.NETHERITE_BEACON));
        this.add(NetheritePlusBlocks.NETHERITE_ANVIL, this.createNameableBlockEntityTable(NetheritePlusBlocks.NETHERITE_ANVIL));

        this.add(NetheritePlusBlocks.NETHERITE_SHULKER_BOX, this.createShulkerBoxDrop(NetheritePlusBlocks.NETHERITE_SHULKER_BOX));
        NetheritePlusBlocks.DYED_NETHERITE_SHULKER_BOX.forEach(box -> this.add(box, createShulkerBoxDrop(box)));
    }

    @Override
    public <T extends ConditionUserBuilder<T>> T applyExplosionCondition(ItemLike type, ConditionUserBuilder<T> builder) {
        return !type.asItem().equals(NetheritePlusItems.FAKE_NETHERITE_BLOCK) ? builder.when(ExplosionCondition.survivesExplosion()) : builder.unwrap();
    }
}
