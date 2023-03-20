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

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;


public class NetheriteShulkerBoxBlockEntity extends LootableContainerBlockEntity implements SidedInventory {
    public static final int COLUMNS = 9;
    public static final int ROWS = 3;
    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();
    private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
    private int viewerCount;
    private AnimationStage animationStage = AnimationStage.CLOSED;
    private float animationProgress;
    private float prevAnimationProgress;
    @Nullable
    private final DyeColor cachedColor;

    public NetheriteShulkerBoxBlockEntity(@Nullable DyeColor dyeColor, BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, blockPos, blockState);
        this.cachedColor = dyeColor;
    }

    public NetheriteShulkerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY, blockPos, blockState);
        this.cachedColor = ShulkerBoxBlock.getColor(blockState.getBlock());
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir) {
        Block block = Block.getBlockFromItem(stack.getItem());
        return !(block instanceof NetheriteShulkerBoxBlock) && !(block instanceof ShulkerBoxBlock);
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new ShulkerBoxScreenHandler(syncId, playerInventory, this);
    }

    public void deserializeInventory(NbtCompound tag) {
        inventory = DefaultedList.ofSize(size(), ItemStack.EMPTY);
        if (!deserializeLootTable(tag) && tag.contains("Items", 9)) {
            Inventories.readNbt(tag, inventory);
        }

    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        deserializeInventory(tag);
    }

    public float getAnimationProgress(float f) {
        return MathHelper.lerp(f, prevAnimationProgress, animationProgress);
    }

    public AnimationStage getAnimationStage() {
        return animationStage;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        return AVAILABLE_SLOTS;
    }

    public Box getBoundingBox(BlockState state) {
        return this.getBoundingBox(state.get(NetheriteShulkerBoxBlock.FACING));
    }

    public Box getBoundingBox(Direction openDirection) {
        float f = getAnimationProgress(1.0F);
        return VoxelShapes.fullCube().getBoundingBox().stretch(0.5F * f * openDirection.getOffsetX(), 0.5F * f * openDirection.getOffsetY(), 0.5F * f * openDirection.getOffsetZ());
    }

    private Box getCollisionBox(Direction facing) {
        Direction direction = facing.getOpposite();
        return this.getBoundingBox(facing).shrink(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ());
    }

    public DyeColor getColor() {
        return cachedColor;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("container.netheriteShulkerBox");
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
        return inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        inventory = list;
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!player.isSpectator()) {
            if (this.viewerCount < 0) {
                this.viewerCount = 0;
            }

            ++this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount == 1) {
                this.world.emitGameEvent(player, GameEvent.CONTAINER_OPEN, this.pos);
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!player.isSpectator()) {
            --this.viewerCount;
            this.world.addSyncedBlockEvent(this.pos, this.getCachedState().getBlock(), 1, this.viewerCount);
            if (this.viewerCount <= 0) {
                this.world.emitGameEvent(player, GameEvent.CONTAINER_CLOSE, this.pos);
                this.world.playSound(null, this.pos, SoundEvents.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    @Override
    public boolean onSyncedBlockEvent(int type, int data) {
        if (type == 1) {
            this.viewerCount = data;
            if (data == 0) {
                this.animationStage = AnimationStage.CLOSING;
                updateNeighborStates(this.getWorld(), this.pos, this.getCachedState());
            }

            if (data == 1) {
                this.animationStage = AnimationStage.OPENING;
                updateNeighborStates(this.getWorld(), this.pos, this.getCachedState());
            }

            return true;
        } else {
            return super.onSyncedBlockEvent(type, data);
        }
    }

    private void pushEntities(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() instanceof NetheriteShulkerBoxBlock) {
            Direction direction = state.get(NetheriteShulkerBoxBlock.FACING);
            Box box = ShulkerEntity.getOpeningDeltaBoundingBox(direction, this.prevAnimationProgress, this.animationProgress).offset(pos);
            List<Entity> list = world.getOtherEntities(null, box);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    if (entity.getPistonBehavior() != PistonBehavior.IGNORE) {
                        entity.move(
                                MovementType.SHULKER_BOX,
                                new Vec3d(
                                        (box.getXLength() + 0.01) * (double) direction.getOffsetX(),
                                        (box.getYLength() + 0.01) * (double) direction.getOffsetY(),
                                        (box.getZLength() + 0.01) * (double) direction.getOffsetZ()
                                )
                        );
                    }
                }

            }
        }
    }

    public NbtCompound serializeInventory(NbtCompound tag) {
        if (!serializeLootTable(tag)) {
            Inventories.readNbt(tag, inventory);
        }

        return tag;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
    }

    @Override
    public int size() {
        return inventory.size();
    }

    public boolean suffocates() {
        return animationStage == AnimationStage.CLOSED;
    }

    public static void tick(World world, BlockPos pos, BlockState state, NetheriteShulkerBoxBlockEntity blockEntity) {
        blockEntity.updateAnimation(world, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        serializeInventory(tag);
    }

    private void updateAnimation(World world, BlockPos pos, BlockState state) {
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

    private static void updateNeighborStates(World world, BlockPos pos, BlockState state) {
        state.updateNeighbors(world, pos, 3);
    }

    public enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING
    }
}
