/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public final class NetheritePlusItems {
    public static final Item.Settings NETHERITE_SHULKER_BOX_ITEM_SETTINGS = new Item.Settings().maxCount(1).group(ItemGroup.DECORATIONS).fireproof();
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
        return register(Registry.BLOCK.getId(block), item);
    }

    private static Item register(Identifier id, Item item) {
        if (item instanceof BlockItem) {
            ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
        }

        return Registry.register(Registry.ITEM, id, item);
    }

    private static void registerBowAndCrossbow() {
        NETHERITE_BOW = register(id("netherite_bow"), new NetheriteBowItem(new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.bow).group(ItemGroup.COMBAT).fireproof()));
        NETHERITE_CROSSBOW = register(id("netherite_crossbow"), new NetheriteCrossbowItem(new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.crossbow).group(ItemGroup.COMBAT).fireproof()));
    }

    private static void registerElytra() {
        Item.Settings elytraSettings = new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.elytra).group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON).fireproof();

        NETHERITE_ELYTRA = register(id("netherite_elytra"), new NetheriteElytraItem(elytraSettings));
    }

    private static void registerFishingRod() {
        NETHERITE_FISHING_ROD = register(id("netherite_fishing_rod"), new NetheriteFishingRodItem(new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.fishing_rod).group(ItemGroup.TOOLS).fireproof()));
    }

    private static void registerHorseArmor() {
        NETHERITE_HORSE_ARMOR = register(id("netherite_horse_armor"), new NetheriteHorseArmorItem(15, new Item.Settings().maxCount(1).group(ItemGroup.MISC).fireproof()));
    }

    public static void init() {
        if (NetheritePlusMod.CONFIG.enabled.shulker_boxes) {
            registerShulkerBoxes();
        }

        if (NetheritePlusMod.CONFIG.enabled.elytra) {
            registerElytra();
        }

        if (NetheritePlusMod.CONFIG.enabled.shields) {
            registerShield();
        }

        if (NetheritePlusMod.CONFIG.enabled.bows_and_crossbows) {
            registerBowAndCrossbow();
        }

        if (NetheritePlusMod.CONFIG.enabled.fishing_rod) {
            registerFishingRod();
        }

        if (NetheritePlusMod.CONFIG.enabled.horse_armor) {
            registerHorseArmor();
        }

        if (NetheritePlusMod.CONFIG.enabled.trident) {
            registerTrident();
        }

        if (NetheritePlusMod.CONFIG.enabled.fake_netherite_blocks) {
            FAKE_NETHERITE_BLOCK = register(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK, new BlockItem(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).fireproof()));
        }

        if (NetheritePlusMod.CONFIG.enabled.anvils) {
            NETHERITE_ANVIL_ITEM = register(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK, new BlockItem(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK, new Item.Settings().group(ItemGroup.DECORATIONS).fireproof()));
        }

        if (NetheritePlusMod.CONFIG.enabled.beacon) {
            NETHERITE_BEACON = register(NetheritePlusBlocks.NETHERITE_BEACON, new BlockItem(NetheritePlusBlocks.NETHERITE_BEACON, new Item.Settings().maxCount(64).group(ItemGroup.MISC).fireproof()));
        }

        if (NetheritePlusMod.CONFIG.enabled.shears) {
            NETHERITE_SHEARS = register(id("netherite_shears"), new ShearsItem(new Item.Settings().group(ItemGroup.TOOLS).fireproof().maxDamage(NetheritePlusMod.CONFIG.durability.shears)));
            DispenserBlock.registerBehavior(NETHERITE_SHEARS, new ShearsDispenserBehavior());
        }
    }

    private static void registerShield() {
        NETHERITE_SHIELD = register(id("netherite_shield"), new NetheriteShieldItem(new QuiltItemSettings().maxDamage(NetheritePlusMod.CONFIG.durability.shield).group(ItemGroup.COMBAT).fireproof().equipmentSlot(stack -> EquipmentSlot.OFFHAND)));
    }

    private static void registerShulkerBoxes() {
        NETHERITE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_WHITE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_ORANGE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_MAGENTA_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_YELLOW_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIME_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_PINK_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_GRAY_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_CYAN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_PURPLE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BLUE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BROWN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_GREEN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_RED_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BLACK_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX,  new BlockItem(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    }

    private static void registerTrident() {
        Item.Settings properties = new Item.Settings().maxDamage(NetheritePlusMod.CONFIG.durability.trident).group(ItemGroup.COMBAT).fireproof();
        NETHERITE_TRIDENT = register(id("netherite_trident"),  new NetheriteTridentItem(properties));
    }

}
