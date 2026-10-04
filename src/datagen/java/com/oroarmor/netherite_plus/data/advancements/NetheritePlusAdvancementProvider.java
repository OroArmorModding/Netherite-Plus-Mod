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

package com.oroarmor.netherite_plus.data.advancements;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.oroarmor.netherite_plus.advancements.triggers.ConstructNetheriteBeaconTrigger;
import com.oroarmor.netherite_plus.advancements.triggers.NetheriteBlocksNetheriteBeaconTrigger;
import com.oroarmor.netherite_plus.advancements.triggers.RiptideNetheriteTridentTrigger;
import com.oroarmor.netherite_plus.stats.NetheritePlusStats;
import com.oroarmor.netherite_plus.tags.NetheritePlusItemTags;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItemIds;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.predicates.DamagePredicate;
import net.minecraft.advancements.predicates.DamageSourcePredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityEquipmentPredicate;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.advancements.predicates.entity.EntityTypePredicate;
import net.minecraft.advancements.predicates.entity.PlayerPredicate;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.EntityHurtPlayerTrigger;
import net.minecraft.advancements.triggers.FishingRodHookedTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.advancements.triggers.PlayerTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stats;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

public class NetheritePlusAdvancementProvider extends FabricAdvancementProvider {
    public NetheritePlusAdvancementProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup);
    }

    private static AdvancementHolder obtainItem(AdvancementHolder parent, Identifier id, Item item) {
        return Advancement.Builder.recipeAdvancement()
                .parent(parent)
                .display(
                        item,
                        Component.translatable(id.toLanguageKey("advancements", "title")),
                        Component.translatable(id.toLanguageKey("advancements", "description")),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion(id.getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(item))
                .requirements(AdvancementRequirements.Strategy.OR)
                .build(id);
    }

    @Override
    public void generateAdvancement(HolderLookup.Provider registryLookup, Consumer<AdvancementHolder> consumer) {
        AdvancementHolder root = Advancement.Builder.recipeAdvancement()
                .rootDisplay(
                        Items.NETHERITE_INGOT,
                        Component.translatable("advancements.netherite_plus.netherite_plus/root.title"),
                        Component.translatable("advancements.netherite_plus.netherite_plus/root.description"),
                        id("gui/advancements/backgrounds/netherite"),
                        AdvancementType.TASK,
                        false,
                        false,
                        false
                )
                .addCriterion("netherite_ingot", InventoryChangeTrigger.TriggerInstance.hasItems(Items.NETHERITE_INGOT))
                .requirements(AdvancementRequirements.Strategy.AND)
                .build(NetheritePlusAdvancements.ROOT);

        consumer.accept(root);

        consumer.accept(obtainItem(root, NetheritePlusAdvancements.FAKE_NETHERITE_BLOCK, NetheritePlusItems.FAKE_NETHERITE_BLOCK));

        consumer.accept(obtainItem(root, NetheritePlusAdvancements.NETHERITE_ANVIL, NetheritePlusItems.NETHERITE_ANVIL));

        consumer.accept(Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        NetheritePlusItems.NETHERITE_BOW,
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_BOW.toLanguageKey("advancements", "title")),
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_BOW.toLanguageKey("advancements", "description")),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion(NetheritePlusItemIds.NETHERITE_BOW.identifier().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(NetheritePlusItems.NETHERITE_BOW))
                .addCriterion(NetheritePlusItemIds.NETHERITE_CROSSBOW.identifier().getPath(), InventoryChangeTrigger.TriggerInstance.hasItems(NetheritePlusItems.NETHERITE_CROSSBOW))
                .requirements(AdvancementRequirements.Strategy.OR)
                .build(NetheritePlusAdvancements.NETHERITE_BOW)
        );

        AdvancementHolder netheriteBeacon = obtainItem(root, NetheritePlusAdvancements.NETHERITE_BEACON, NetheritePlusItems.NETHERITE_BEACON);
        consumer.accept(netheriteBeacon);
        AdvancementHolder fullNetheriteBeacon = Advancement.Builder.recipeAdvancement()
                .parent(netheriteBeacon)
                .display(
                        new ItemStackTemplate(NetheritePlusItems.NETHERITE_BEACON, DataComponentPatch.builder()
                                .set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(BlockStateProperties.POWERED, true))
                                .build()
                        ),
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_BEACON_SPECIAL.toLanguageKey("advancements", "title")),
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_BEACON_SPECIAL.toLanguageKey("advancements", "description")),
                        AdvancementType.GOAL,
                        true,
                        true,
                        false
                )
                .addCriterion("full_beacon", ConstructNetheriteBeaconTrigger.TriggerInstance.constructedBeacon(MinMaxBounds.Ints.exactly(5)))
                .requirements(AdvancementRequirements.Strategy.OR)
                .build(NetheritePlusAdvancements.NETHERITE_BEACON_SPECIAL);
        consumer.accept(fullNetheriteBeacon);
        consumer.accept(Advancement.Builder.recipeAdvancement()
                .parent(fullNetheriteBeacon)
                .display(
                        new ItemStackTemplate(NetheritePlusItems.NETHERITE_BEACON, DataComponentPatch.builder()
                                .set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(BlockStateProperties.POWERED, true))
                                .build()
                        ),
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_BEACON_ULTRA.toLanguageKey("advancements", "title")),
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_BEACON_ULTRA.toLanguageKey("advancements", "description")),
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        false
                )
                .addCriterion("full_netherite_beacon", NetheriteBlocksNetheriteBeaconTrigger.TriggerInstance.netheriteBlocks(MinMaxBounds.Ints.exactly(3 * 3 + 5 * 5 + 7 * 7 + 9 * 9 + 11 * 11)))
                .requirements(AdvancementRequirements.Strategy.OR)
                .build(NetheritePlusAdvancements.NETHERITE_BEACON_ULTRA)
        );

        AdvancementHolder netheriteElytra = obtainItem(root, NetheritePlusAdvancements.NETHERITE_ELYTRA, NetheritePlusItems.NETHERITE_ELYTRA);
        consumer.accept(netheriteElytra);
        consumer.accept(
                Advancement.Builder.recipeAdvancement()
                        .parent(netheriteElytra)
                        .display(
                                NetheritePlusItems.NETHERITE_ELYTRA,
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_ELYTRA_SPECIAL.toLanguageKey("advancements", "title")),
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_ELYTRA_SPECIAL.toLanguageKey("advancements", "description")),
                                AdvancementType.CHALLENGE,
                                true,
                                true,
                                true
                        )
                        .addCriterion("flew_10km",
                                CriteriaTriggers.TICK.createCriterion(
                                        new PlayerTrigger.TriggerInstance(
                                                Optional.of(
                                                        Holder.direct(
                                                                LootItemEntityPropertyCondition.hasProperties(
                                                                                LootContext.EntityTarget.THIS,
                                                                                EntityPredicate.Builder.entity().player(
                                                                                                PlayerPredicate.Builder.player()
                                                                                                        .addStat(
                                                                                                                Stats.CUSTOM,
                                                                                                                Holder.Reference.createStandAlone(BuiltInRegistries.CUSTOM_STAT, ResourceKey.create(Registries.CUSTOM_STAT, NetheritePlusStats.FLY_NETHERITE_ELYTRA_ONE_CM)),
                                                                                                                MinMaxBounds.Ints.atLeast(10 * 1000 * 100)
                                                                                                        )
                                                                                                        .build()
                                                                                        )
                                                                                        .build()
                                                                        )
                                                                        .build()
                                                        )
                                                )
                                        )
                                )
                        )
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(NetheritePlusAdvancements.NETHERITE_ELYTRA_SPECIAL)
        );

        AdvancementHolder netheriteFishingRod = obtainItem(root, NetheritePlusAdvancements.NETHERITE_FISHING_ROD, NetheritePlusItems.NETHERITE_FISHING_ROD);
        consumer.accept(netheriteFishingRod);
        consumer.accept(
                Advancement.Builder.recipeAdvancement()
                        .parent(netheriteFishingRod)
                        .display(
                                NetheritePlusItems.NETHERITE_FISHING_ROD,
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_FISHING_ROD_SPECIAL.toLanguageKey("advancements", "title")),
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_FISHING_ROD_SPECIAL.toLanguageKey("advancements", "description")),
                                AdvancementType.CHALLENGE,
                                true,
                                true,
                                true
                        )
                        .addCriterion("hooked_ancient_debris", FishingRodHookedTrigger.TriggerInstance.fishedItem(
                                        Optional.of(
                                                ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), NetheritePlusItems.NETHERITE_FISHING_ROD).build()
                                        ),
                                        Optional.empty(),
                                        Optional.of(
                                                ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), Items.ANCIENT_DEBRIS).build()
                                        )
                                )
                        )
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(NetheritePlusAdvancements.NETHERITE_FISHING_ROD_SPECIAL)
        );

        consumer.accept(obtainItem(root, NetheritePlusAdvancements.NETHERITE_SHEARS, NetheritePlusItems.NETHERITE_SHEARS));

        AdvancementHolder netheriteShield = obtainItem(root, NetheritePlusAdvancements.NETHERITE_SHIELD, NetheritePlusItems.NETHERITE_SHIELD);
        consumer.accept(netheriteShield);
        consumer.accept(
                Advancement.Builder.recipeAdvancement()
                        .parent(netheriteShield)
                        .display(
                                NetheritePlusItems.NETHERITE_SHIELD,
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_SHIELD_SPECIAL.toLanguageKey("advancements", "title")),
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_SHIELD_SPECIAL.toLanguageKey("advancements", "description")),
                                AdvancementType.CHALLENGE,
                                true,
                                true,
                                true
                        )
                        .addCriterion("deflected_with_shield", CriteriaTriggers.ENTITY_HURT_PLAYER.createCriterion(
                                        new EntityHurtPlayerTrigger.TriggerInstance(
                                                Optional.of(
                                                        Holder.direct(
                                                                AnyOfCondition.anyOf()
                                                                        .or(
                                                                                Holder.direct(
                                                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                                                        LootContext.EntityTarget.THIS,
                                                                                                        EntityPredicate.Builder.entity()
                                                                                                                .equipment(
                                                                                                                        EntityEquipmentPredicate.Builder.equipment()
                                                                                                                                .mainhand(
                                                                                                                                        ItemPredicate.Builder.item()
                                                                                                                                                .of(registryLookup.lookupOrThrow(Registries.ITEM), NetheritePlusItems.NETHERITE_SHIELD)
                                                                                                                                )
                                                                                                                )
                                                                                                )
                                                                                                .build()
                                                                                )
                                                                        )
                                                                        .or(
                                                                                Holder.direct(
                                                                                        LootItemEntityPropertyCondition.hasProperties(
                                                                                                        LootContext.EntityTarget.THIS,
                                                                                                        EntityPredicate.Builder.entity()
                                                                                                                .equipment(
                                                                                                                        EntityEquipmentPredicate.Builder.equipment()
                                                                                                                                .offhand(
                                                                                                                                        ItemPredicate.Builder.item()
                                                                                                                                                .of(registryLookup.lookupOrThrow(Registries.ITEM), NetheritePlusItems.NETHERITE_SHIELD)
                                                                                                                                )
                                                                                                                )
                                                                                                )
                                                                                                .build()
                                                                                )
                                                                        )
                                                                        .build()
                                                        )
                                                ),
                                                Optional.of(
                                                        DamagePredicate.Builder.damageInstance()
                                                                .blocked(true)
                                                                .sourceEntity(
                                                                        EntityPredicate.Builder.entity()
                                                                                .entityType(EntityTypePredicate.of(registryLookup.lookupOrThrow(Registries.ENTITY_TYPE), EntityTypes.BLAZE))
                                                                                .build()
                                                                )
                                                                .type(DamageSourcePredicate.Builder.damageType()
                                                                        .direct(EntityPredicate.Builder.entity()
                                                                                .entityType(EntityTypePredicate.of(registryLookup.lookupOrThrow(Registries.ENTITY_TYPE), EntityTypes.SMALL_FIREBALL))
                                                                        )
                                                                )
                                                                .build()
                                                )
                                        )
                                )
                        )
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(NetheritePlusAdvancements.NETHERITE_SHIELD_SPECIAL)
        );

        consumer.accept(Advancement.Builder.recipeAdvancement()
                .parent(root)
                .display(
                        NetheritePlusItems.NETHERITE_SHULKER_BOX,
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_SHULKER_BOXES.toLanguageKey("advancements", "title")),
                        Component.translatable(NetheritePlusAdvancements.NETHERITE_SHULKER_BOXES.toLanguageKey("advancements", "description")),
                        AdvancementType.TASK,
                        true,
                        true,
                        false
                )
                .addCriterion("shulker_box", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(registryLookup.lookupOrThrow(Registries.ITEM), NetheritePlusItemTags.NETHERITE_SHULKER_BOXES)))
                .requirements(AdvancementRequirements.Strategy.OR)
                .build(NetheritePlusAdvancements.NETHERITE_SHULKER_BOXES)
        );

        AdvancementHolder netheriteTrident = obtainItem(root, NetheritePlusAdvancements.NETHERITE_TRIDENT, NetheritePlusItems.NETHERITE_TRIDENT);
        consumer.accept(netheriteTrident);
        consumer.accept(
                Advancement.Builder.recipeAdvancement()
                        .parent(netheriteTrident)
                        .display(
                                NetheritePlusItems.NETHERITE_TRIDENT,
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_TRIDENT_SPECIAL.toLanguageKey("advancements", "title")),
                                Component.translatable(NetheritePlusAdvancements.NETHERITE_TRIDENT_SPECIAL.toLanguageKey("advancements", "description")),
                                AdvancementType.CHALLENGE,
                                true,
                                true,
                                true
                        )
                        .addCriterion(NetheritePlusAdvancements.NETHERITE_TRIDENT_SPECIAL.getPath(), RiptideNetheriteTridentTrigger.TriggerInstance.usedRiptide())
                        .requirements(AdvancementRequirements.Strategy.OR)
                        .build(NetheritePlusAdvancements.NETHERITE_TRIDENT_SPECIAL)
        );
    }
}
