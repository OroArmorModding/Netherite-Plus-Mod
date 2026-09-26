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

package com.oroarmor.netherite_plus.block.entity;

import java.util.List;
import java.util.stream.IntStream;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;


public class NetheriteShulkerBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    public static final int COLUMNS = 9;
    public static final int ROWS = 3;
    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();
    @Nullable
    private final DyeColor cachedColor;
    private NonNullList<ItemStack> inventory = NonNullList.withSize(27, ItemStack.EMPTY);
    private int viewerCount;
    private AnimationStage animationStage = AnimationStage.CLOSED;
    private float animationProgress;
    private float prevAnimationProgress;

    public NetheriteShulkerBoxBlockEntity(@Nullable DyeColor dyeColor, BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, blockPos, blockState);
        this.cachedColor = dyeColor;
    }

    public NetheriteShulkerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, blockPos, blockState);
        this.cachedColor = ShulkerBoxBlock.getColorFromBlock(blockState.getBlock());
    }

    public static void tick(Level world, BlockPos pos, BlockState state, NetheriteShulkerBoxBlockEntity blockEntity) {
        blockEntity.updateAnimation(world, pos, state);
    }

    private static void updateNeighborStates(Level world, BlockPos pos, BlockState state) {
        state.updateNeighbourShapes(world, pos, 3);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction dir) {
        Block block = Block.byItem(stack.getItem());
        return !(block instanceof NetheriteShulkerBoxBlock) && !(block instanceof ShulkerBoxBlock);
    }

    @Override
    protected AbstractContainerMenu createMenu(int syncId, Inventory playerInventory) {
        return new ShulkerBoxMenu(syncId, playerInventory, this);
    }

    public void deserializeInventory(CompoundTag tag) {
        inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
        if (!tryLoadLootTable(tag) && tag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(tag, inventory);
        }

    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        deserializeInventory(tag);
    }

    public float getAnimationProgress(float f) {
        return Mth.lerp(f, prevAnimationProgress, animationProgress);
    }

    public AnimationStage getAnimationStage() {
        return animationStage;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return AVAILABLE_SLOTS;
    }

    public AABB getBoundingBox(BlockState state) {
        return this.getBoundingBox(state.getValue(NetheriteShulkerBoxBlock.FACING));
    }

    public AABB getBoundingBox(Direction openDirection) {
        float f = getAnimationProgress(1.0F);
        return Shapes.block().bounds().expandTowards(0.5F * f * openDirection.getStepX(), 0.5F * f * openDirection.getStepY(), 0.5F * f * openDirection.getStepZ());
    }

    private AABB getCollisionBox(Direction facing) {
        Direction direction = facing.getOpposite();
        return this.getBoundingBox(facing).contract(direction.getStepX(), direction.getStepY(), direction.getStepZ());
    }

    public DyeColor getColor() {
        return cachedColor;
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.netheriteShulkerBox");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        inventory = list;
    }

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }

            ++this.viewerCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.level.gameEvent(player, GameEvent.CONTAINER_OPEN, this.worldPosition);
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.level.gameEvent(player, GameEvent.CONTAINER_CLOSE, this.worldPosition);
                this.level.playSound(null, this.worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    @Override
    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            if (data == 0) {
                this.animationStage = AnimationStage.CLOSING;
                updateNeighborStates(this.getLevel(), this.worldPosition, this.getBlockState());
            }

            if (data == 1) {
                this.animationStage = AnimationStage.OPENING;
                updateNeighborStates(this.getLevel(), this.worldPosition, this.getBlockState());
            }

            return true;
        } else {
            return super.triggerEvent(type, data);
        }
    }

    private void pushEntities(Level world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof NetheriteShulkerBoxBlock) {
            Direction direction = state.getValue(NetheriteShulkerBoxBlock.FACING);
            AABB box = Shulker.getProgressDeltaAabb(direction, this.prevAnimationProgress, this.animationProgress).move(pos);
            List<Entity> list = world.getEntities(null, box);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        entity.move(
                                MoverType.SHULKER_BOX,
                                new Vec3(
                                        (box.getXsize() + 0.01) * (double) direction.getStepX(),
                                        (box.getYsize() + 0.01) * (double) direction.getStepY(),
                                        (box.getZsize() + 0.01) * (double) direction.getStepZ()
                                )
                        );
                    }
                }

            }
        }
    }

    public CompoundTag serializeInventory(CompoundTag tag) {
        if (!trySaveLootTable(tag)) {
            ContainerHelper.loadAllItems(tag, inventory);
        }

        return tag;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
    }

    @Override
    public int getContainerSize() {
        return inventory.size();
    }

    public boolean suffocates() {
        return animationStage == AnimationStage.CLOSED;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        serializeInventory(tag);
    }

    private void updateAnimation(Level world, BlockPos pos, BlockState state) {
        this.prevAnimationProgress = this.animationProgress;
        switch (this.animationStage) {
            case CLOSED -> this.animationProgress = 0.0F;
            case OPENING -> {
                this.animationProgress += 0.1F;
                if (this.animationProgress >= 1.0F) {
                    this.animationStage = AnimationStage.OPENED;
                    this.animationProgress = 1.0F;
                    updateNeighborStates(world, pos, state);
                }
                this.pushEntities(world, pos, state);
            }
            case CLOSING -> {
                this.animationProgress -= 0.1F;
                if (this.animationProgress <= 0.0F) {
                    this.animationStage = AnimationStage.CLOSED;
                    this.animationProgress = 0.0F;
                    updateNeighborStates(world, pos, state);
                }
            }
            case OPENED -> this.animationProgress = 1.0F;
        }

    }

    public enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING
    }
}
