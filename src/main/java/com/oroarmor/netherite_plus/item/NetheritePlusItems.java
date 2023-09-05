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

package com.oroarmor.netherite_plus.item;


import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.minecraft.block.Block;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.cauldron.LeveledCauldronBlock;
import net.minecraft.block.dispenser.DispenserBlock;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShearsItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public final class NetheritePlusItems {
    public static final Item.Settings NETHERITE_SHULKER_BOX_ITEM_SETTINGS = new Item.Settings().maxCount(1).fireproof();
    public static final CauldronBehavior CLEAN_NETHERITE_BOX = (state, world, pos, player, hand, stack) -> {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (!(block instanceof NetheriteShulkerBoxBlock)) {
            return ActionResult.PASS;
        } else {
            if (!world.isClient) {
                ItemStack itemStack = new ItemStack(NetheritePlusBlocks.NETHERITE_SHULKER_BOX);
                if (stack.hasNbt()) {
                    itemStack.setNbt(stack.getNbt().copy());
                }

                player.setStackInHand(hand, itemStack);
                player.incrementStat(Stats.CLEAN_SHULKER_BOX);
                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            }

            return ActionResult.success(world.isClient);
        }
    };

    public static Item NETHERITE_ELYTRA;

    public static Item NETHERITE_FISHING_ROD;

    public static Item NETHERITE_SHIELD;

    public static Item NETHERITE_BOW;

    public static Item NETHERITE_CROSSBOW;

    public static Item NETHERITE_TRIDENT;

    public static Item NETHERITE_SHULKER_BOX;
    public static Item NETHERITE_WHITE_SHULKER_BOX;
    public static Item NETHERITE_ORANGE_SHULKER_BOX;
    public static Item NETHERITE_MAGENTA_SHULKER_BOX;
    public static Item NETHERITE_LIGHT_BLUE_SHULKER_BOX;
    public static Item NETHERITE_YELLOW_SHULKER_BOX;
    public static Item NETHERITE_LIME_SHULKER_BOX;
    public static Item NETHERITE_PINK_SHULKER_BOX;
    public static Item NETHERITE_GRAY_SHULKER_BOX;
    public static Item NETHERITE_LIGHT_GRAY_SHULKER_BOX;
    public static Item NETHERITE_CYAN_SHULKER_BOX;
    public static Item NETHERITE_PURPLE_SHULKER_BOX;
    public static Item NETHERITE_BLUE_SHULKER_BOX;
    public static Item NETHERITE_BROWN_SHULKER_BOX;
    public static Item NETHERITE_GREEN_SHULKER_BOX;
    public static Item NETHERITE_RED_SHULKER_BOX;
    public static Item NETHERITE_BLACK_SHULKER_BOX;

    public static Item NETHERITE_BEACON;

    public static Item NETHERITE_HORSE_ARMOR;

    public static Item FAKE_NETHERITE_BLOCK;

    public static Item NETHERITE_ANVIL_ITEM;

    public static Item NETHERITE_SHEARS;

    private static Item register(Block block, BlockItem item) {
        if (block instanceof NetheriteShulkerBoxBlock) {
            CauldronBehavior.WATER_CAULDRON_BEHAVIOR.put(item, CLEAN_NETHERITE_BOX);
        }
        return register(Registries.BLOCK.getId(block), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registries.ITEM, id, item);
    }

    private static void registerBowAndCrossbow() {
        NETHERITE_BOW = register(id("netherite_bow"), new NetheriteBowItem(new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.bow.value()).fireproof()));
        NETHERITE_CROSSBOW = register(id("netherite_crossbow"), new NetheriteCrossbowItem(new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.crossbow.value()).fireproof()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.BOW, NETHERITE_BOW);
            entries.addAfter(Items.CROSSBOW, NETHERITE_CROSSBOW);
        });
    }

    private static void registerElytra() {
        Item.Settings elytraSettings = new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.elytra.value()).rarity(Rarity.UNCOMMON).fireproof();

        NETHERITE_ELYTRA = register(id("netherite_elytra"), new NetheriteElytraItem(elytraSettings));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS_AND_UTILITIES).register(entries -> {
            entries.addAfter(Items.ELYTRA, NETHERITE_ELYTRA);
        });
    }

    private static void registerFishingRod() {
        NETHERITE_FISHING_ROD = register(id("netherite_fishing_rod"), new NetheriteFishingRodItem(new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.fishing_rod.value()).fireproof()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS_AND_UTILITIES).register(entries -> {
            entries.addAfter(Items.FISHING_ROD, NETHERITE_FISHING_ROD);
        });
    }

    private static void registerHorseArmor() {
        NETHERITE_HORSE_ARMOR = register(id("netherite_horse_armor"), new NetheriteHorseArmorItem(15, new Item.Settings().maxCount(1).fireproof()));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.DIAMOND_HORSE_ARMOR, NETHERITE_HORSE_ARMOR);
        });
    }

    public static void init() {
        if (NetheritePlusMod.CONFIG.enabled.shulker_boxes.value()) {
            registerShulkerBoxes();
        }

        if (NetheritePlusMod.CONFIG.enabled.elytra.value()) {
            registerElytra();
        }

        if (NetheritePlusMod.CONFIG.enabled.shields.value()) {
            registerShield();
        }

        if (NetheritePlusMod.CONFIG.enabled.bows_and_crossbows.value()) {
            registerBowAndCrossbow();
        }

        if (NetheritePlusMod.CONFIG.enabled.fishing_rod.value()) {
            registerFishingRod();
        }

        if (NetheritePlusMod.CONFIG.enabled.horse_armor.value()) {
            registerHorseArmor();
        }

        if (NetheritePlusMod.CONFIG.enabled.trident.value()) {
            registerTrident();
        }

        if (NetheritePlusMod.CONFIG.enabled.fake_netherite_blocks.value()) {
            FAKE_NETHERITE_BLOCK = register(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK, new BlockItem(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK, new Item.Settings().fireproof()));
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(entries -> {
                entries.addBefore(Items.IRON_BLOCK, FAKE_NETHERITE_BLOCK);
            });
        }

        if (NetheritePlusMod.CONFIG.enabled.anvils.value()) {
            NETHERITE_ANVIL_ITEM = register(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK, new BlockItem(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK, new Item.Settings().fireproof()));
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL_BLOCKS).register(entries -> {
                entries.addAfter(Items.DAMAGED_ANVIL, NETHERITE_ANVIL_ITEM);
            });
        }

        if (NetheritePlusMod.CONFIG.enabled.beacon.value()) {
            NETHERITE_BEACON = register(NetheritePlusBlocks.NETHERITE_BEACON, new BlockItem(NetheritePlusBlocks.NETHERITE_BEACON, new Item.Settings().maxCount(64).fireproof()));
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL_BLOCKS).register(entries -> {
                entries.addAfter(Items.BEACON, NETHERITE_BEACON);
            });
        }

        if (NetheritePlusMod.CONFIG.enabled.shears.value()) {
            NETHERITE_SHEARS = register(id("netherite_shears"), new ShearsItem(new Item.Settings().fireproof().maxDamage(NetheritePlusMod.CONFIG.durability.shears.value())));
            DispenserBlock.registerBehavior(NETHERITE_SHEARS, new ShearsDispenserBehavior());
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS_AND_UTILITIES).register(entries -> {
                entries.addAfter(Items.SHEARS, NETHERITE_SHEARS);
            });
        }
    }

    private static void registerShield() {
//        NETHERITE_SHIELD = register(id("netherite_shield"), new NetheriteShieldItem(new QuiltItemSettings().maxDamage(NetheritePlusMod.CONFIG.durability.shield.value()).fireproof().equipmentSlot(stack -> EquipmentSlot.OFFHAND)));
//        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
//            entries.addAfter(Items.SHIELD, NETHERITE_SHIELD);
//        });
    }

    private static void registerShulkerBoxes() {
        NETHERITE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_WHITE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_ORANGE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_MAGENTA_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_YELLOW_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIME_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_PINK_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_GRAY_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_CYAN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_PURPLE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BLUE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BROWN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_GREEN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_RED_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BLACK_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX, new BlockItem(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX, NETHERITE_WHITE_SHULKER_BOX, NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_GRAY_SHULKER_BOX, NETHERITE_BLACK_SHULKER_BOX, NETHERITE_BROWN_SHULKER_BOX, NETHERITE_RED_SHULKER_BOX, NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_LIME_SHULKER_BOX, NETHERITE_GREEN_SHULKER_BOX, NETHERITE_CYAN_SHULKER_BOX, NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_BLUE_SHULKER_BOX, NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_PINK_SHULKER_BOX);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COLORED_BLOCKS).register(entries -> {
            entries.addAfter(Items.PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX, NETHERITE_WHITE_SHULKER_BOX, NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_GRAY_SHULKER_BOX, NETHERITE_BLACK_SHULKER_BOX, NETHERITE_BROWN_SHULKER_BOX, NETHERITE_RED_SHULKER_BOX, NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_LIME_SHULKER_BOX, NETHERITE_GREEN_SHULKER_BOX, NETHERITE_CYAN_SHULKER_BOX, NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_BLUE_SHULKER_BOX, NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_PINK_SHULKER_BOX);
        });
    }

    private static void registerTrident() {
        Item.Settings properties = new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.trident.value()).fireproof();
        NETHERITE_TRIDENT = register(id("netherite_trident"), new NetheriteTridentItem(properties));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.addAfter(Items.TRIDENT, NETHERITE_TRIDENT);
        });
    }

}
