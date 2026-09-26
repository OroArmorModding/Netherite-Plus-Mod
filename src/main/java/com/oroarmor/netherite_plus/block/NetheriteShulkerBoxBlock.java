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
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

//import net.minecraft.block.*;

public class NetheriteShulkerBoxBlock extends BaseEntityBlock {
    public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    @Nullable
    private final DyeColor color;

    public NetheriteShulkerBoxBlock(@Nullable DyeColor color, Properties settings) {
        super(settings);
        this.color = color;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
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
        return getColor(Block.byItem(item));
    }

    public static ItemStack getItemStack(DyeColor color) {
        return new ItemStack(get(color));
    }

    private static boolean canOpen(BlockState state, Level world, BlockPos pos, NetheriteShulkerBoxBlockEntity entity) {
        if (entity.getAnimationStage() != AnimationStage.CLOSED) {
            return true;
        } else {
            AABB box = Shulker.getProgressDeltaAabb(state.getValue(FACING), 0.0F, 0.5F).move(pos).deflate(1.0E-6);
            return world.noCollision(box);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        super.appendHoverText(stack, world, tooltip, options);
        CompoundTag compoundTag = stack.getTagElement("BlockEntityTag");
        if (compoundTag != null) {
            if (compoundTag.contains("LootTable", 8)) {
                tooltip.add(Component.literal("???????"));
            }

            if (compoundTag.contains("Items", 9)) {
                NonNullList<ItemStack> defaultedList = NonNullList.withSize(27, ItemStack.EMPTY);
                ContainerHelper.loadAllItems(compoundTag, defaultedList);
                int i = 0;
                int j = 0;
                Iterator<ItemStack> var9 = defaultedList.iterator();

                while (var9.hasNext()) {
                    ItemStack itemStack = var9.next();
                    if (!itemStack.isEmpty()) {
                        ++j;
                        if (i <= 4) {
                            ++i;
                            MutableComponent mutableText = itemStack.getHoverName().plainCopy();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
                            tooltip.add(mutableText);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC));
                }
            }
        }

    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new NetheriteShulkerBoxBlockEntity(this.color, pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, NetheriteShulkerBoxBlockEntity::tick);
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) world.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
            builder = builder.withDynamicDrop(CONTENTS, (consumer) -> {
                for (int i = 0; i < shulkerBoxBlockEntity.getContainerSize(); ++i) {
                    consumer.accept(shulkerBoxBlockEntity.getItem(i));
                }
            });
        }

        return super.getDrops(state, builder);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        return blockEntity instanceof NetheriteShulkerBoxBlockEntity ? Shapes.create(((NetheriteShulkerBoxBlockEntity) blockEntity).getBoundingBox(state)) : Shapes.block();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        ItemStack itemStack = super.getCloneItemStack(world, pos, state);
        NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) world.getBlockEntity(pos);
        CompoundTag compoundTag = shulkerBoxBlockEntity.serializeInventory(new CompoundTag());
        if (!compoundTag.isEmpty()) {
            itemStack.addTagElement("BlockEntityTag", compoundTag);
        }

        return itemStack;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getClickedFace());
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity netheriteShulkerBoxBlockEntity) {
            if (!world.isClientSide && player.isCreative() && !netheriteShulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = getItemStack(this.getColor());
                CompoundTag compoundTag = netheriteShulkerBoxBlockEntity.serializeInventory(new CompoundTag());
                if (!compoundTag.isEmpty()) {
                    itemStack.addTagElement("BlockEntityTag", compoundTag);
                }

                if (netheriteShulkerBoxBlockEntity.hasCustomName()) {
                    itemStack.setHoverName(netheriteShulkerBoxBlockEntity.getCustomName());
                }

                ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
                itemEntity.setDefaultPickUpDelay();
                world.addFreshEntity(itemEntity);
            } else {
                netheriteShulkerBoxBlockEntity.unpackLootTable(player);
            }
        }

        super.playerWillDestroy(world, pos, state, player);
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
                ((NetheriteShulkerBoxBlockEntity) blockEntity).setCustomName(itemStack.getHoverName());
            }
        }

    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
                world.updateNeighbourForOutputSignal(pos, state.getBlock());
            }

            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity) {
                if (canOpen(state, world, pos, shulkerBoxBlockEntity)) {
                    player.openMenu(shulkerBoxBlockEntity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return InteractionResult.CONSUME;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

}
