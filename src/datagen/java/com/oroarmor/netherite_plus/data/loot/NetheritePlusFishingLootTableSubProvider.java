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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import com.oroarmor.netherite_plus.loot.NetheritePlusLootManager;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableSubProvider;

import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.FishingHookPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.EmptyTagLookupWrapper;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

public class NetheritePlusFishingLootTableSubProvider extends SimpleFabricLootTableSubProvider {
    private final HolderLookup.Provider lookup;

    public NetheritePlusFishingLootTableSubProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture, LootContextParamSets.FISHING);
        lookup = registryLookupFuture.join();
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        HolderLookup.RegistryLookup<LootTable> tables = lookup.lookupOrThrow(Registries.LOOT_TABLE);
        HolderGetter<Enchantment> enchantments = lookup.lookupOrThrow(Registries.ENCHANTMENT);

        BiFunction<ResourceKey<LootTable>, LootTable.Builder, Holder.Reference<LootTable>> add = (id, builder) -> {
            consumer.accept(id, builder);
            return Holder.Reference.createStandAlone(tables instanceof EmptyTagLookupWrapper<LootTable>(HolderLookup.RegistryLookup<LootTable> parent) ? parent : tables, id);
        };

        Holder.Reference<LootTable> junkTable = add.apply(
                NetheritePlusLootManager.LAVA_FISHING_JUNK_LOOT_TABLE,
                LootTable.lootTable()
                        .pool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(LootItem.lootTableItem(Items.STRING).setWeight(5))
                                        .add(LootItem.lootTableItem(Items.BONE).setWeight(5))
                                        .add(LootItem.lootTableItem(Items.ROTTEN_FLESH).setWeight(5))
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))).setWeight(3))
                                        .add(LootItem.lootTableItem(Items.MAGMA_CREAM).apply(SetItemCountFunction.setCount(ContextIntProviders.between(1, 5))).setWeight(3))
                                        .build()
                        )
        );


        Holder.Reference<LootTable> treasureTable = add.apply(
                NetheritePlusLootManager.LAVA_FISHING_TREASURE_LOOT_TABLE,
                LootTable.lootTable()
                        .pool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(LootItem.lootTableItem(Items.GOLD_NUGGET).setWeight(30))
                                        .add(LootItem.lootTableItem(Items.SADDLE).setWeight(15))
                                        .add(LootItem.lootTableItem(Items.ANCIENT_DEBRIS).setWeight(5))
                                        .add(
                                                LootItem.lootTableItem(Items.GOLDEN_HELMET)
                                                        .apply(EnchantWithLevelsFunction.enchantWithLevels(enchantments, ContextIntProviders.exactly(30)))
                                                        .apply(SetItemDamageFunction.setDamage(ContextFloatProviders.between(0.0f, 0.5f)))
                                                        .setWeight(10)
                                        )
                                        .add(
                                                LootItem.lootTableItem(Items.GOLDEN_CHESTPLATE)
                                                        .apply(EnchantWithLevelsFunction.enchantWithLevels(enchantments, ContextIntProviders.exactly(30)))
                                                        .apply(SetItemDamageFunction.setDamage(ContextFloatProviders.between(0.0f, 0.5f)))
                                                        .setWeight(10)
                                        )
                                        .add(
                                                LootItem.lootTableItem(Items.GOLDEN_LEGGINGS)
                                                        .apply(EnchantWithLevelsFunction.enchantWithLevels(enchantments, ContextIntProviders.exactly(30)))
                                                        .apply(SetItemDamageFunction.setDamage(ContextFloatProviders.between(0.0f, 0.5f)))
                                                        .setWeight(10)
                                        )
                                        .add(
                                                LootItem.lootTableItem(Items.GOLDEN_BOOTS)
                                                        .apply(EnchantWithLevelsFunction.enchantWithLevels(enchantments, ContextIntProviders.exactly(30)))
                                                        .apply(SetItemDamageFunction.setDamage(ContextFloatProviders.between(0.0f, 0.5f)))
                                                        .setWeight(10)
                                        )
                                        .add(
                                                LootItem.lootTableItem(Items.GOLDEN_SWORD)
                                                        .apply(EnchantWithLevelsFunction.enchantWithLevels(enchantments, ContextIntProviders.exactly(30)))
                                                        .apply(SetItemDamageFunction.setDamage(ContextFloatProviders.between(0.0f, 0.5f)))
                                                        .setWeight(10)
                                        )
                                        .build()
                        )
        );

        add.apply(
                NetheritePlusLootManager.LAVA_FISHING_LOOT_TABLE,
                LootTable.lootTable()
                        .withPool(
                                LootPool.lootPool()
                                        .setRolls(ContextIntProviders.exactly(1))
                                        .add(
                                                NestedLootTable.lootTableReference(junkTable)
                                                        .setQuality(-2)
                                                        .setWeight(60)
                                        )
                                        .add(
                                                NestedLootTable.lootTableReference(treasureTable)
                                                        .setQuality(2)
                                                        .setWeight(40)
                                                        .when(
                                                                LootItemEntityPropertyCondition.hasProperties(
                                                                        LootContext.EntityTarget.THIS,
                                                                        EntityPredicate.Builder.entity().fishingHook(FishingHookPredicate.inOpenWater(true))
                                                                )
                                                        )
                                        )
                        )
        );
    }

    @Override
    public void run() {
    }
}
