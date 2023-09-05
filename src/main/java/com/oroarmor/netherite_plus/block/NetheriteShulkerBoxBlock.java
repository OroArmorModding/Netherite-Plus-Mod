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

import java.util.Iterator;
import java.util.List;

import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity.AnimationStage;
import org.jetbrains.annotations.Nullable;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteShulkerBoxBlock extends BlockWithEntity {
    public static final EnumProperty<Direction> FACING = FacingBlock.FACING;
    public static final Identifier CONTENTS = new Identifier("contents");

    @Nullable
    private final DyeColor color;

    public NetheriteShulkerBoxBlock(@Nullable DyeColor color, Settings settings) {
        super(settings);
        this.color = color;
        setDefaultState(stateManager.getDefaultState().with(FACING, Direction.UP));
    }

    public static Block get(DyeColor dyeColor) {
        if (dyeColor == null) {
            return NetheritePlusBlocks.NETHERITE_SHULKER_BOX;
        }
        return switch (dyeColor) {
            case WHITE -> NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX;
            case ORANGE -> NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX;
            case MAGENTA -> NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX;
            case LIGHT_BLUE -> NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX;
            case YELLOW -> NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX;
            case LIME -> NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX;
            case PINK -> NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX;
            case GRAY -> NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX;
            case LIGHT_GRAY -> NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX;
            case CYAN -> NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX;
            case PURPLE -> NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX;
            case BLUE -> NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX;
            case BROWN -> NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX;
            case GREEN -> NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX;
            case RED -> NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX;
            case BLACK -> NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX;
        };
    }

    @Environment(EnvType.CLIENT)
    public static DyeColor getColor(Block block) {
        return block instanceof NetheriteShulkerBoxBlock ? ((NetheriteShulkerBoxBlock) block).getColor() : null;
    }

    public static DyeColor getColor(Item item) {
        return getColor(Block.getBlockFromItem(item));
    }

    public static ItemStack getItemStack(DyeColor color) {
        return new ItemStack(get(color));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        NbtCompound compoundTag = stack.getSubNbt("BlockEntityTag");
        if (compoundTag != null) {
            if (compoundTag.contains("LootTable", 8)) {
                tooltip.add(Text.literal("???????"));
            }

            if (compoundTag.contains("Items", 9)) {
                DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(27, ItemStack.EMPTY);
                Inventories.readNbt(compoundTag, defaultedList);
                int i = 0;
                int j = 0;
                Iterator<ItemStack> var9 = defaultedList.iterator();

                while (var9.hasNext()) {
                    ItemStack itemStack = var9.next();
                    if (!itemStack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableText mutableText = itemStack.getName().copyContentOnly();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
                            tooltip.add(mutableText);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add(Text.translatable("container.shulkerBox.more", j - i).formatted(Formatting.ITALIC));
                }
            }
        }

    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetheriteShulkerBoxBlockEntity(this.color, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, NetheriteShulkerBoxBlockEntity::tick);
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
        return ScreenHandler.calculateComparatorOutput((Inventory) world.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS, (consumer) -> {
                for (int i = 0; i < shulkerBoxBlockEntity.size(); ++i) {
                    consumer.accept(shulkerBoxBlockEntity.getStack(i));
                }
            });
        }

        return super.getDroppedStacks(state, builder);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NetheriteShulkerBoxBlockEntity ? VoxelShapes.cuboid(((NetheriteShulkerBoxBlockEntity) blockEntity).getBoundingBox(state)) : VoxelShapes.fullCube();
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getPickStack(world, pos, state);
        NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) world.getBlockEntity(pos);
        NbtCompound compoundTag = shulkerBoxBlockEntity.serializeInventory(new NbtCompound());
        if (!compoundTag.isEmpty()) {
            itemStack.setSubNbt("BlockEntityTag", compoundTag);
        }

        return itemStack;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getSide());
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ANIMATED;
    }

    @Override
    public boolean hasComparatorOutput(BlockState state) {
        return true;
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity netheriteShulkerBoxBlockEntity) {
            if (!world.isClient && player.isCreative() && !netheriteShulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack(this.getColor());
                NbtCompound compoundTag = netheriteShulkerBoxBlockEntity.serializeInventory(new NbtCompound());
                if (!compoundTag.isEmpty()) {
                    itemStack.setSubNbt("BlockEntityTag", compoundTag);
                }

                if (netheriteShulkerBoxBlockEntity.hasCustomName()) {
                    itemStack.setCustomName(netheriteShulkerBoxBlockEntity.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                netheriteShulkerBoxBlockEntity.checkLootInteraction(player);
            }
        }

        super.onBreak(world, pos, state, player);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
                ((NetheriteShulkerBoxBlockEntity) blockEntity).setCustomName(itemStack.getName());
            }
        }

    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
                world.updateComparators(pos, state.getBlock());
            }

            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResult.CONSUME;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                    player.openHandledScreen(shulkerBoxBlockEntity);
                    player.incrementStat(Stats.OPEN_SHULKER_BOX);
                    PiglinBrain.onGuardedBlockInteracted(player, true);
                }

                return ActionResult.CONSUME;
            } else {
                return ActionResult.PASS;
            }
        }
    }

    private static boolean canOpen(BlockState state, World world, BlockPos pos, NetheriteShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != AnimationStage.CLOSED) {
            return true;
        } else {
            Box box = ShulkerEntity.getOpeningDeltaBoundingBox(state.get(FACING), 0.0F, 0.5F).offset(pos).contract(1.0E-6);
            return world.isSpaceEmpty(box);
        }
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

}
