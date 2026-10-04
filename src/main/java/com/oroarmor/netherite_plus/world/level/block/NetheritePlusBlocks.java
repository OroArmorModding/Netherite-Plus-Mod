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

package com.oroarmor.netherite_plus.world.level.block;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.util.function.Function;

import com.oroarmor.netherite_plus.world.level.block.entity.NetheriteBeaconBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColorCollection;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class NetheritePlusBlocks {
    public static final Block NETHERITE_SHULKER_BOX = registerBlock(
            NetheritePlusBlockItemsIds.NETHERITE_SHULKER_BOX.block(),
            p -> new ShulkerBoxBlock(null, p),
            shulkerBoxProperties(MapColor.COLOR_PURPLE)
    );
    public static final ColorCollection<Block> DYED_NETHERITE_SHULKER_BOX = ColorCollection.registerBlocks(
            NetheritePlusBlockItemsIds.DYED_NETHERITE_SHULKER_BOX,
            NetheritePlusBlocks::registerBlock,
            ShulkerBoxBlock::new,
            color -> shulkerBoxProperties(color == DyeColor.PURPLE ? MapColor.TERRACOTTA_PURPLE : color.getMapColor())
    );

    public static final Block FAKE_NETHERITE_BLOCK = registerBlock(
            NetheritePlusBlockItemsIds.FAKE_NETHERITE_BLOCK,
            Block::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.IRON_BLOCK)
                    .mapColor(MapColor.COLOR_BLACK)
                    .sound(SoundType.NETHERITE_BLOCK)
    );

    public static final Block NETHERITE_ANVIL = registerBlock(
            NetheritePlusBlockItemsIds.NETHERITE_ANVIL,
            AnvilBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL)
    );

    public static final Block NETHERITE_BEACON = registerBlock(
            NetheritePlusBlockItemsIds.NETHERITE_BEACON,
            NetheriteBeaconBlock::new,
            BlockBehaviour.Properties.ofFullCopy(Blocks.BEACON)
    );

    public static final BlockEntityType<NetheriteBeaconBlockEntity> NETHERITE_BEACON_BLOCK_ENTITY = Registry.register(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            id("netherite_beacon"),
            FabricBlockEntityTypeBuilder.create(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON).build()
    );

    static {
        BlockEntityTypes.SHULKER_BOX.addValidBlock(NETHERITE_SHULKER_BOX);
        DYED_NETHERITE_SHULKER_BOX.forEach(BlockEntityTypes.SHULKER_BOX::addValidBlock);
    }

    private static Block registerBlock(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        return registerBlock(id.block(), blockFactory, properties);
    }

    private static Block registerBlock(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties) {
        Block block = blockFactory.apply(properties.setId(id));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    private static BlockBehaviour.Properties shulkerBoxProperties(final MapColor mapColor) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.SHULKER_BOX).mapColor(mapColor).explosionResistance(1200.0F);
    }

}
