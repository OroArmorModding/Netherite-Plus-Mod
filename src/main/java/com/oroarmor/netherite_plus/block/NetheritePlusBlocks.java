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

package com.oroarmor.netherite_plus.block;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusBlocks {
    public static Block NETHERITE_WHITE_SHULKER_BOX;
    public static Block NETHERITE_SHULKER_BOX;
    public static Block NETHERITE_ORANGE_SHULKER_BOX;
    public static Block NETHERITE_MAGENTA_SHULKER_BOX;
    public static Block NETHERITE_LIGHT_BLUE_SHULKER_BOX;
    public static Block NETHERITE_YELLOW_SHULKER_BOX;
    public static Block NETHERITE_BLACK_SHULKER_BOX;
    public static Block NETHERITE_BLUE_SHULKER_BOX;
    public static Block NETHERITE_BROWN_SHULKER_BOX;
    public static Block NETHERITE_CYAN_SHULKER_BOX;
    public static Block NETHERITE_GRAY_SHULKER_BOX;
    public static Block NETHERITE_GREEN_SHULKER_BOX;
    public static Block NETHERITE_LIGHT_GRAY_SHULKER_BOX;
    public static Block NETHERITE_LIME_SHULKER_BOX;
    public static Block NETHERITE_PINK_SHULKER_BOX;
    public static Block NETHERITE_PURPLE_SHULKER_BOX;
    public static Block NETHERITE_RED_SHULKER_BOX;

    public static Block FAKE_NETHERITE_BLOCK;

    public static Block NETHERITE_ANVIL_BLOCK;

    public static Block NETHERITE_BEACON;

    public static BlockEntityType<NetheriteShulkerBoxBlockEntity> NETHERITE_SHULKER_BOX_ENTITY;
    public static BlockEntityType<NetheriteBeaconBlockEntity> NETHERITE_BEACON_BLOCK_ENTITY;

    static {
        if (NetheritePlusMod.CONFIG.enabled.shulker_boxes) {
            registerShulkerBoxBlocks();
        }

        if (NetheritePlusMod.CONFIG.enabled.fake_netherite_blocks) {
            FAKE_NETHERITE_BLOCK = register("fake_netherite_block", new FakeNetheriteBlock(AbstractBlock.Settings.of(Material.METAL, MapColor.BLACK).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.NETHERITE)));
        }

        if (NetheritePlusMod.CONFIG.enabled.anvils) {
            NETHERITE_ANVIL_BLOCK = register("netherite_anvil", new NetheriteAnvilBlock(AbstractBlock.Settings.of(Material.REPAIR_STATION, MapColor.IRON_GRAY).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.ANVIL)));
        }

        if (NetheritePlusMod.CONFIG.enabled.beacon) {
            NETHERITE_BEACON = register("netherite_beacon", new NetheriteBeaconBlock(AbstractBlock.Settings.of(Material.GLASS, MapColor.DIAMOND_BLUE).strength(3.0F).luminance((state) -> 15).nonOpaque()));

            NETHERITE_BEACON_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("netherite_beacon"),
                    BlockEntityType.Builder.create(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON).build(null));
        }
    }

    private static NetheriteShulkerBoxBlock createShulkerBoxBlock(DyeColor color, AbstractBlock.Settings settings) {
        AbstractBlock.ContextPredicate contextPredicate = (blockState, blockView, blockPos) -> {
            BlockEntity blockEntity = blockView.getBlockEntity(blockPos);
            if (!(blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity)) {
                return true;
            }
            return shulkerBoxBlockEntity.suffocates();
        };
        return new NetheriteShulkerBoxBlock(color, settings.strength(2.0F).dynamicBounds().nonOpaque().suffocates(contextPredicate).blockVision(contextPredicate));
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registry.BLOCK, id(id), block);
    }

    private static void registerShulkerBoxBlocks() {
        NETHERITE_SHULKER_BOX = register("netherite_shulker_box", createShulkerBoxBlock(null, AbstractBlock.Settings.of(Material.SHULKER_BOX).strength(2f, 1200f)));
        NETHERITE_WHITE_SHULKER_BOX = register("netherite_white_shulker_box", createShulkerBoxBlock(DyeColor.WHITE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.WHITE)));
        NETHERITE_ORANGE_SHULKER_BOX = register("netherite_orange_shulker_box", createShulkerBoxBlock(DyeColor.ORANGE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.ORANGE)));
        NETHERITE_MAGENTA_SHULKER_BOX = register("netherite_magenta_shulker_box", createShulkerBoxBlock(DyeColor.MAGENTA, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.MAGENTA)));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register("netherite_light_blue_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.LIGHT_BLUE)));
        NETHERITE_YELLOW_SHULKER_BOX = register("netherite_yellow_shulker_box", createShulkerBoxBlock(DyeColor.YELLOW, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.YELLOW)));
        NETHERITE_LIME_SHULKER_BOX = register("netherite_lime_shulker_box", createShulkerBoxBlock(DyeColor.LIME, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.LIME)));
        NETHERITE_PINK_SHULKER_BOX = register("netherite_pink_shulker_box", createShulkerBoxBlock(DyeColor.PINK, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.PINK)));
        NETHERITE_GRAY_SHULKER_BOX = register("netherite_gray_shulker_box", createShulkerBoxBlock(DyeColor.GRAY, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.GRAY)));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register("netherite_light_gray_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.LIGHT_GRAY)));
        NETHERITE_CYAN_SHULKER_BOX = register("netherite_cyan_shulker_box", createShulkerBoxBlock(DyeColor.CYAN, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.CYAN)));
        NETHERITE_PURPLE_SHULKER_BOX = register("netherite_purple_shulker_box", createShulkerBoxBlock(DyeColor.PURPLE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.TERRACOTTA_PURPLE)));
        NETHERITE_BLUE_SHULKER_BOX = register("netherite_blue_shulker_box", createShulkerBoxBlock(DyeColor.BLUE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.BLUE)));
        NETHERITE_BROWN_SHULKER_BOX = register("netherite_brown_shulker_box", createShulkerBoxBlock(DyeColor.BROWN, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.BROWN)));
        NETHERITE_GREEN_SHULKER_BOX = register("netherite_green_shulker_box", createShulkerBoxBlock(DyeColor.GREEN, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.GREEN)));
        NETHERITE_RED_SHULKER_BOX = register("netherite_red_shulker_box", createShulkerBoxBlock(DyeColor.RED, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.RED)));
        NETHERITE_BLACK_SHULKER_BOX = register("netherite_black_shulker_box", createShulkerBoxBlock(DyeColor.BLACK, AbstractBlock.Settings.of(Material.SHULKER_BOX, MapColor.BLACK)));

        NETHERITE_SHULKER_BOX_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("netherite_shulker_box"),
                BlockEntityType.Builder.create(NetheriteShulkerBoxBlockEntity::new,
                        NETHERITE_SHULKER_BOX, NETHERITE_BLACK_SHULKER_BOX, NETHERITE_BLUE_SHULKER_BOX, NETHERITE_BROWN_SHULKER_BOX, NETHERITE_CYAN_SHULKER_BOX, NETHERITE_GRAY_SHULKER_BOX, NETHERITE_GREEN_SHULKER_BOX, NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_LIME_SHULKER_BOX, NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_PINK_SHULKER_BOX, NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_RED_SHULKER_BOX, NETHERITE_WHITE_SHULKER_BOX, NETHERITE_YELLOW_SHULKER_BOX)
                        .build(null));
    }

}
