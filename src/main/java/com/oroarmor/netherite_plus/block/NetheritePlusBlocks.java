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

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

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
            FAKE_NETHERITE_BLOCK = register("fake_netherite_block", new FakeNetheriteBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).mapColor(MapColor.COLOR_BLACK).sound(SoundType.NETHERITE_BLOCK)));
        }

        if (NetheritePlusMod.CONFIG.enabled.anvils.value()) {
            NETHERITE_ANVIL_BLOCK = register("netherite_anvil", new NetheriteAnvilBlock(BlockBehaviour.Properties.copy(Blocks.ANVIL)));
        }

        if (NetheritePlusMod.CONFIG.enabled.beacon.value()) {
            NETHERITE_BEACON = register("netherite_beacon", new NetheriteBeaconBlock(BlockBehaviour.Properties.copy(Blocks.BEACON)));

            NETHERITE_BEACON_BLOCK_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id("netherite_beacon"),
                    BlockEntityType.Builder.of(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON).build(null));
        }
    }

    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, id(id), block);
    }

    private static void registerShulkerBoxBlocks() {
        NETHERITE_SHULKER_BOX = register("netherite_shulker_box", createShulkerBoxBlock(null, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)));
        NETHERITE_WHITE_SHULKER_BOX = register("netherite_white_shulker_box", createShulkerBoxBlock(DyeColor.WHITE, BlockBehaviour.Properties.of().mapColor(MapColor.SNOW)));
        NETHERITE_ORANGE_SHULKER_BOX = register("netherite_orange_shulker_box", createShulkerBoxBlock(DyeColor.ORANGE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE)));
        NETHERITE_MAGENTA_SHULKER_BOX = register("netherite_magenta_shulker_box", createShulkerBoxBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA)));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register("netherite_light_blue_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE)));
        NETHERITE_YELLOW_SHULKER_BOX = register("netherite_yellow_shulker_box", createShulkerBoxBlock(DyeColor.YELLOW, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW)));
        NETHERITE_LIME_SHULKER_BOX = register("netherite_lime_shulker_box", createShulkerBoxBlock(DyeColor.LIME, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN)));
        NETHERITE_PINK_SHULKER_BOX = register("netherite_pink_shulker_box", createShulkerBoxBlock(DyeColor.PINK, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK)));
        NETHERITE_GRAY_SHULKER_BOX = register("netherite_gray_shulker_box", createShulkerBoxBlock(DyeColor.GRAY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY)));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register("netherite_light_gray_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY)));
        NETHERITE_CYAN_SHULKER_BOX = register("netherite_cyan_shulker_box", createShulkerBoxBlock(DyeColor.CYAN, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN)));
        NETHERITE_PURPLE_SHULKER_BOX = register("netherite_purple_shulker_box", createShulkerBoxBlock(DyeColor.PURPLE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE)));
        NETHERITE_BLUE_SHULKER_BOX = register("netherite_blue_shulker_box", createShulkerBoxBlock(DyeColor.BLUE, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE)));
        NETHERITE_BROWN_SHULKER_BOX = register("netherite_brown_shulker_box", createShulkerBoxBlock(DyeColor.BROWN, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN)));
        NETHERITE_GREEN_SHULKER_BOX = register("netherite_green_shulker_box", createShulkerBoxBlock(DyeColor.GREEN, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN)));
        NETHERITE_RED_SHULKER_BOX = register("netherite_red_shulker_box", createShulkerBoxBlock(DyeColor.RED, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED)));
        NETHERITE_BLACK_SHULKER_BOX = register("netherite_black_shulker_box", createShulkerBoxBlock(DyeColor.BLACK, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK)));

        NETHERITE_SHULKER_BOX_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, id("netherite_shulker_box"),
                BlockEntityType.Builder.of(NetheriteShulkerBoxBlockEntity::new,
                                NETHERITE_SHULKER_BOX, NETHERITE_BLACK_SHULKER_BOX, NETHERITE_BLUE_SHULKER_BOX, NETHERITE_BROWN_SHULKER_BOX, NETHERITE_CYAN_SHULKER_BOX, NETHERITE_GRAY_SHULKER_BOX, NETHERITE_GREEN_SHULKER_BOX, NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_LIME_SHULKER_BOX, NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_PINK_SHULKER_BOX, NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_RED_SHULKER_BOX, NETHERITE_WHITE_SHULKER_BOX, NETHERITE_YELLOW_SHULKER_BOX)
                        .build(null));
    }

    private static NetheriteShulkerBoxBlock createShulkerBoxBlock(DyeColor color, BlockBehaviour.Properties settings) {
        BlockBehaviour.StatePredicate contextPredicate = (state, world, pos) -> {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            return !(blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) || shulkerBoxBlockEntity.suffocates();
        };

        return new NetheriteShulkerBoxBlock(
                color,
                settings.forceSolidOn()
                        .strength(2.0F)
                        .explosionResistance(1200.0F)
                        .dynamicShape()
                        .noOcclusion()
                        .isSuffocating(contextPredicate)
                        .isViewBlocking(contextPredicate)
                        .pushReaction(PushReaction.DESTROY)
                        .isRedstoneConductor(Blocks::always)
        );
    }

}
