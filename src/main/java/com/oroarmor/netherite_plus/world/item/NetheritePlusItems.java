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


import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlockItemsIds;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlocks;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.rpg_foundation.ranged_weapon.api.CustomBow;
import net.rpg_foundation.ranged_weapon.api.CustomCrossbow;
import net.rpg_foundation.ranged_weapon.api.RangedWeaponConfig;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public final class NetheritePlusItems {
    public static final Item.Properties NETHERITE_SHULKER_BOX_ITEM_PROPERTIES = new Item.Properties()
            .stacksTo(1)
            .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
            .fireResistant();

    public static Item NETHERITE_SHULKER_BOX = registerBlock(
            NetheritePlusBlockItemsIds.NETHERITE_SHULKER_BOX,
            NetheritePlusBlocks.NETHERITE_SHULKER_BOX,
            NETHERITE_SHULKER_BOX_ITEM_PROPERTIES
    );
    public static ColorCollection<Item> DYED_NETHERITE_SHULKER_BOX = ColorCollection.registerBlockItems(
            NetheritePlusBlockItemsIds.DYED_NETHERITE_SHULKER_BOX,
            NetheritePlusBlocks.DYED_NETHERITE_SHULKER_BOX,
            (id, block, _) -> registerBlock(id, block, NETHERITE_SHULKER_BOX_ITEM_PROPERTIES)
    );

    public static Item NETHERITE_ELYTRA = registerItem(NetheritePlusItemIds.NETHERITE_ELYTRA, Item::new, new Item.Properties()
            .durability(NetheritePlusMod.CONFIG.durability.elytra)
            .rarity(Rarity.EPIC)
            .component(DataComponents.GLIDER, Unit.INSTANCE)
            .component(
                    DataComponents.EQUIPPABLE,
                    Equippable.builder(EquipmentSlot.CHEST)
                            .setEquipSound(SoundEvents.ARMOR_EQUIP_ELYTRA)
                            .setAsset(NetheritePlusElytraComponents.NETHERITE_ELYTRA_EQUIPMENT_ASSET)
                            .setDamageOnHurt(false).build()
            )
            .repairable(Items.PHANTOM_MEMBRANE)
            .attributes(NetheritePlusElytraComponents.NETHERITE_ELYTRA_ARMOR_MATERIAL.createAttributes(ArmorType.CHESTPLATE))
            .fireResistant());

    public static Item NETHERITE_FISHING_ROD = registerItem(
            NetheritePlusItemIds.NETHERITE_FISHING_ROD,
            FishingRodItem::new,
            new Item.Properties().durability(NetheritePlusMod.CONFIG.durability.fishing_rod)
                    .enchantable(2)
                    .fireResistant()
    );

    public static Item NETHERITE_SHIELD = registerItem(
            NetheritePlusItemIds.NETHERITE_SHIELD,
            ShieldItem::new,
            new Item.Properties().durability(NetheritePlusMod.CONFIG.durability.shield)
                    .component(DataComponents.BANNER_PATTERNS, BannerPatternLayers.EMPTY)
                    .repairable(Items.NETHERITE_INGOT)
                    .equippableUnswappable(EquipmentSlot.OFFHAND)
                    .delayedComponent(
                            DataComponents.BLOCKS_ATTACKS,
                            (context) -> new BlocksAttacks(
                                    0.25F,
                                    1.0F,
                                    List.of(
                                            new BlocksAttacks.DamageReduction(
                                                    90.0F,
                                                    Optional.empty(),
                                                    0.0F,
                                                    1.0F)
                                    ),
                                    new BlocksAttacks.ItemDamageFunction(
                                            3.0F,
                                            1.0F,
                                            1.0F
                                    ),
                                    Optional.of(context.getOrThrow(DamageTypeTags.BYPASSES_SHIELD)),
                                    Optional.of(SoundEvents.SHIELD_BLOCK),
                                    Optional.of(SoundEvents.SHIELD_BREAK)
                            )
                    )
                    .component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK)
                    .fireResistant()
    );

    public static Item NETHERITE_BOW = registerItem(
            NetheritePlusItemIds.NETHERITE_BOW,
            p1 -> new CustomBow(
                    p1,
                    new RangedWeaponConfig(
                            RangedWeaponConfig.BOW.damage() * NetheritePlusMod.CONFIG.damage.bowDamageMultiplier + NetheritePlusMod.CONFIG.damage.bow_damage_addition,
                            RangedWeaponConfig.BOW.pull_time()
                    )
            ),
            new Item.Properties()
                    .durability(NetheritePlusMod.CONFIG.durability.bow)
                    .enchantable(1)
                    .fireResistant()
    );

    public static Item NETHERITE_CROSSBOW = registerItem(
            NetheritePlusItemIds.NETHERITE_CROSSBOW,
            p1 -> new CustomCrossbow(
                    p1,
                    new RangedWeaponConfig(
                            RangedWeaponConfig.BOW.damage() * NetheritePlusMod.CONFIG.damage.crossbowDamageMultiplier + NetheritePlusMod.CONFIG.damage.crossbowDamageAddition,
                            RangedWeaponConfig.BOW.pull_time()
                    )
            ),
            new Item.Properties()
                    .durability(NetheritePlusMod.CONFIG.durability.crossbow)
                    .enchantable(1)
                    .fireResistant()
    );

    public static Item NETHERITE_TRIDENT = registerItem(
            NetheritePlusItemIds.NETHERITE_TRIDENT,
            TridentItem::new,
            new Item.Properties()
                    .rarity(Rarity.RARE)
                    .durability(NetheritePlusMod.CONFIG.durability.trident)
                    .attributes(
                            ItemAttributeModifiers.builder()
                                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 8.0 * NetheritePlusMod.CONFIG.damage.tridentDamageMultiplier + NetheritePlusMod.CONFIG.damage.tridentDamageAddition, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.9F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                    .build()
                    )
                    .component(DataComponents.TOOL, TridentItem.createToolProperties())
                    .enchantable(1)
                    .component(DataComponents.WEAPON, new Weapon(1))
                    .fireResistant()
    );

    public static Item NETHERITE_SHEARS = registerItem(
            NetheritePlusItemIds.NETHERITE_SHEARS,
            ShearsItem::new,
            new Item.Properties().durability(NetheritePlusMod.CONFIG.durability.shears)
                    .component(DataComponents.TOOL, ShearsItem.createToolProperties())
                    .fireResistant()
    );

    public static Item FAKE_NETHERITE_BLOCK = registerBlock(
            NetheritePlusBlockItemsIds.FAKE_NETHERITE_BLOCK,
            NetheritePlusBlocks.FAKE_NETHERITE_BLOCK,
            p -> p.fireResistant()
    );

    public static Item NETHERITE_ANVIL = registerBlock(
            NetheritePlusBlockItemsIds.NETHERITE_ANVIL,
            NetheritePlusBlocks.NETHERITE_ANVIL,
            p -> p.fireResistant()
    );

    public static Item NETHERITE_BEACON = registerBlock(
            NetheritePlusBlockItemsIds.NETHERITE_BEACON,
            NetheritePlusBlocks.NETHERITE_BEACON,
            p -> p.fireResistant()
    );

    private static Item registerBlock(BlockItemId id, Block block, Item.Properties properties) {
        return registerItem(id.item(), p -> new BlockItem(block, p), properties.useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    private static Item registerBlock(BlockItemId id, Block block, UnaryOperator<Item.Properties> properties) {
        return registerItem(id.item(), p -> new BlockItem(block, properties.apply(p)), new Item.Properties().useBlockDescriptionPrefix().requiredFeatures(block.requiredFeatures()));
    }

    private static Item registerItem(ResourceKey<Item> id, Function<Item.Properties, Item> itemFactory, Item.Properties properties) {
        Item item = itemFactory.apply(properties.setId(id));
        if (item instanceof BlockItem blockItem) {
            blockItem.registerBlocks(Item.BY_BLOCK, item);
        }

        return Registry.register(BuiltInRegistries.ITEM, id, item);
    }

    public static void init() {
        ShulkerBoxDispenseBehavior shulkerBoxDispenseBehavior = new ShulkerBoxDispenseBehavior();
        DispenserBlock.registerBehavior(NetheritePlusItems.NETHERITE_SHULKER_BOX, shulkerBoxDispenseBehavior);
        NetheritePlusItems.DYED_NETHERITE_SHULKER_BOX.forEach(item -> DispenserBlock.registerBehavior(item, shulkerBoxDispenseBehavior));
        DispenserBlock.registerBehavior(NETHERITE_SHEARS, new ShearsDispenseItemBehavior());

        addToCreativeModeTabs();
    }

    public static void addToCreativeModeTabs() {
        List<DyeColor> gameplayColorOrder = List.of(
                DyeColor.WHITE,
                DyeColor.LIGHT_GRAY,
                DyeColor.GRAY,
                DyeColor.BLACK,
                DyeColor.BROWN,
                DyeColor.RED,
                DyeColor.ORANGE,
                DyeColor.YELLOW,
                DyeColor.LIME,
                DyeColor.GREEN,
                DyeColor.CYAN,
                DyeColor.LIGHT_BLUE,
                DyeColor.BLUE,
                DyeColor.PURPLE,
                DyeColor.MAGENTA,
                DyeColor.PINK
        );

        CreativeModeTabEvents.ModifyOutput addShulkerBoxes = entries -> {
            entries.insertAfter(Items.DYED_SHULKER_BOX.pink(), NETHERITE_SHULKER_BOX);
            Item last = NETHERITE_SHULKER_BOX;
            for (DyeColor color : gameplayColorOrder) {
                Item item = DYED_NETHERITE_SHULKER_BOX.pick(color);
                entries.insertAfter(last, item);
                last = item;
            }
        };
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.BUILDING_BLOCKS).register(entries -> {
            entries.insertAfter(Items.IRON_BLOCK, FAKE_NETHERITE_BLOCK);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COLORED_BLOCKS).register(addShulkerBoxes);

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT).register(entries -> {
            entries.insertAfter(Items.SHIELD, NETHERITE_SHIELD);
            entries.insertAfter(Items.BOW, NETHERITE_BOW);
            entries.insertAfter(Items.CROSSBOW, NETHERITE_CROSSBOW);
            entries.insertAfter(Items.TRIDENT, NETHERITE_TRIDENT);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FUNCTIONAL_BLOCKS).register(entries -> {
            addShulkerBoxes.modifyOutput(entries);
            entries.insertAfter(Items.DAMAGED_ANVIL, NETHERITE_ANVIL);
            entries.insertAfter(Items.BEACON, NETHERITE_BEACON);
        });

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            entries.insertAfter(Items.ELYTRA, NETHERITE_ELYTRA);
            entries.insertAfter(Items.FISHING_ROD, NETHERITE_FISHING_ROD);
            entries.insertAfter(Items.SHEARS, NETHERITE_SHEARS);
        });
    }

}
