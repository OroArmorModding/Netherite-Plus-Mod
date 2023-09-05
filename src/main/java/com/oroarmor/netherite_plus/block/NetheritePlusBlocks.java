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

package com.oroarmor.netherite_plus.block;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.MapColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;

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
        if (NetheritePlusMod.CONFIG.enabled.shulker_boxes.value()) {
            registerShulkerBoxBlocks();
        }

        if (NetheritePlusMod.CONFIG.enabled.fake_netherite_blocks.value()) {
            FAKE_NETHERITE_BLOCK = register("fake_netherite_block", new FakeNetheriteBlock(AbstractBlock.Settings.copy(Blocks.IRON_BLOCK).mapColor(MapColor.BLACK).sounds(BlockSoundGroup.NETHERITE)));
        }

        if (NetheritePlusMod.CONFIG.enabled.anvils.value()) {
            NETHERITE_ANVIL_BLOCK = register("netherite_anvil", new NetheriteAnvilBlock(AbstractBlock.Settings.copy(Blocks.ANVIL)));
        }

        if (NetheritePlusMod.CONFIG.enabled.beacon.value()) {
            NETHERITE_BEACON = register("netherite_beacon", new NetheriteBeaconBlock(AbstractBlock.Settings.copy(Blocks.BEACON)));

            NETHERITE_BEACON_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("netherite_beacon"),
                    BlockEntityType.Builder.create(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON).build(null));
        }
    }

    private static Block register(String id, Block block) {
        return Registry.register(Registries.BLOCK, id(id), block);
    }

    private static void registerShulkerBoxBlocks() {
        NETHERITE_SHULKER_BOX = register("netherite_shulker_box", createShulkerBoxBlock(null, AbstractBlock.Settings.create().mapColor(MapColor.GRAY)));
        NETHERITE_WHITE_SHULKER_BOX = register("netherite_white_shulker_box", createShulkerBoxBlock(DyeColor.WHITE, AbstractBlock.Settings.create().mapColor(MapColor.SNOW)));
        NETHERITE_ORANGE_SHULKER_BOX = register("netherite_orange_shulker_box", createShulkerBoxBlock(DyeColor.ORANGE, AbstractBlock.Settings.create().mapColor(MapColor.ORANGE)));
        NETHERITE_MAGENTA_SHULKER_BOX = register("netherite_magenta_shulker_box", createShulkerBoxBlock(DyeColor.MAGENTA, AbstractBlock.Settings.create().mapColor(MapColor.MAGENTA)));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register("netherite_light_blue_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_BLUE)));
        NETHERITE_YELLOW_SHULKER_BOX = register("netherite_yellow_shulker_box", createShulkerBoxBlock(DyeColor.YELLOW, AbstractBlock.Settings.create().mapColor(MapColor.YELLOW)));
        NETHERITE_LIME_SHULKER_BOX = register("netherite_lime_shulker_box", createShulkerBoxBlock(DyeColor.LIME, AbstractBlock.Settings.create().mapColor(MapColor.LIME)));
        NETHERITE_PINK_SHULKER_BOX = register("netherite_pink_shulker_box", createShulkerBoxBlock(DyeColor.PINK, AbstractBlock.Settings.create().mapColor(MapColor.PINK)));
        NETHERITE_GRAY_SHULKER_BOX = register("netherite_gray_shulker_box", createShulkerBoxBlock(DyeColor.GRAY, AbstractBlock.Settings.create().mapColor(MapColor.GRAY)));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register("netherite_light_gray_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.create().mapColor(MapColor.LIGHT_GRAY)));
        NETHERITE_CYAN_SHULKER_BOX = register("netherite_cyan_shulker_box", createShulkerBoxBlock(DyeColor.CYAN, AbstractBlock.Settings.create().mapColor(MapColor.CYAN)));
        NETHERITE_PURPLE_SHULKER_BOX = register("netherite_purple_shulker_box", createShulkerBoxBlock(DyeColor.PURPLE, AbstractBlock.Settings.create().mapColor(MapColor.PURPLE)));
        NETHERITE_BLUE_SHULKER_BOX = register("netherite_blue_shulker_box", createShulkerBoxBlock(DyeColor.BLUE, AbstractBlock.Settings.create().mapColor(MapColor.BLUE)));
        NETHERITE_BROWN_SHULKER_BOX = register("netherite_brown_shulker_box", createShulkerBoxBlock(DyeColor.BROWN, AbstractBlock.Settings.create().mapColor(MapColor.BROWN)));
        NETHERITE_GREEN_SHULKER_BOX = register("netherite_green_shulker_box", createShulkerBoxBlock(DyeColor.GREEN, AbstractBlock.Settings.create().mapColor(MapColor.GREEN)));
        NETHERITE_RED_SHULKER_BOX = register("netherite_red_shulker_box", createShulkerBoxBlock(DyeColor.RED, AbstractBlock.Settings.create().mapColor(MapColor.RED)));
        NETHERITE_BLACK_SHULKER_BOX = register("netherite_black_shulker_box", createShulkerBoxBlock(DyeColor.BLACK, AbstractBlock.Settings.create().mapColor(MapColor.BLACK)));

        NETHERITE_SHULKER_BOX_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE, id("netherite_shulker_box"),
                BlockEntityType.Builder.create(NetheriteShulkerBoxBlockEntity::new,
                        NETHERITE_SHULKER_BOX, NETHERITE_BLACK_SHULKER_BOX, NETHERITE_BLUE_SHULKER_BOX, NETHERITE_BROWN_SHULKER_BOX, NETHERITE_CYAN_SHULKER_BOX, NETHERITE_GRAY_SHULKER_BOX, NETHERITE_GREEN_SHULKER_BOX, NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_LIME_SHULKER_BOX, NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_PINK_SHULKER_BOX, NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_RED_SHULKER_BOX, NETHERITE_WHITE_SHULKER_BOX, NETHERITE_YELLOW_SHULKER_BOX)
                        .build(null));
    }

    private static NetheriteShulkerBoxBlock createShulkerBoxBlock(DyeColor color, AbstractBlock.Settings settings) {
        AbstractBlock.ContextPredicate contextPredicate = (state, world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return !(blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) || shulkerBoxBlockEntity.suffocates();
        };

        return new NetheriteShulkerBoxBlock(
                color,
                settings.solid()
                        .strength(2.0F)
                        .resistance(1200.0F)
                        .dynamicBounds()
                        .nonOpaque()
                        .suffocates(contextPredicate)
                        .blockVision(contextPredicate)
                        .pistonBehavior(PistonBehavior.DESTROY)
                        .solidBlock(Blocks::always)
        );
    }

}
