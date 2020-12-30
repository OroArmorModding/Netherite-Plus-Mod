package com.oroarmor.netherite_plus.block.entity;

import java.util.List;
import java.util.stream.IntStream;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class NetheriteShulkerBoxBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, TickableBlockEntity {
    public enum AnimationStage {
        CLOSED, OPENING, OPENED, CLOSING
    }

    private static final int[] AVAILABLE_SLOTS = IntStream.range(0, 27).toArray();
    private NonNullList<ItemStack> inventory;
    private int viewerCount;
    private AnimationStage animationStage;
    private float animationProgress;

    private float prevAnimationProgress;
    private DyeColor cachedColor;

    private boolean cachedColorUpdateNeeded;

    public NetheriteShulkerBoxBlockEntity() {
        this(null);
        cachedColorUpdateNeeded = true;
    }

    public NetheriteShulkerBoxBlockEntity(DyeColor color) {
        super(NetheritePlusBlocks.NETHERITE_SHULKER_BOX_ENTITY.get());
        inventory = NonNullList.withSize(27, ItemStack.EMPTY);
        animationStage = AnimationStage.CLOSED;
        cachedColor = color;
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
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
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

    @Environment(EnvType.CLIENT)
    public DyeColor getColor() {
        if (cachedColorUpdateNeeded) {
            cachedColor = NetheriteShulkerBoxBlock.getColor(getBlockState().getBlock());
            cachedColorUpdateNeeded = false;
        }

        return cachedColor;
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.netheriteShulkerBox");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return inventory;
    }

    @Override
    public void stopOpen(Player player) {
        if (!player.isSpectator()) {
            --viewerCount;
            level.blockEvent(worldPosition, getBlockState().getBlock(), 1, viewerCount);
            if (viewerCount <= 0) {
                level.playSound(null, worldPosition, SoundEvents.SHULKER_BOX_CLOSE, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    @Override
    public void startOpen(Player player) {
        if (!player.isSpectator()) {
            if (viewerCount < 0) {
                viewerCount = 0;
            }

            ++viewerCount;
            level.blockEvent(worldPosition, getBlockState().getBlock(), 1, viewerCount);
            if (viewerCount == 1) {
                level.playSound(null, worldPosition, SoundEvents.SHULKER_BOX_OPEN, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
            }
        }

    }

    @Override
    public boolean triggerEvent(int type, int data) {
        if (type == 1) {
            viewerCount = data;
            if (data == 0) {
                animationStage = AnimationStage.CLOSING;
                updateNeighborStates();
            }

            if (data == 1) {
                animationStage = AnimationStage.OPENING;
                updateNeighborStates();
            }

            return true;
        }
        return super.triggerEvent(type, data);
    }

    private void pushEntities() {
        BlockState blockState = level.getBlockState(getBlockPos());
        if (blockState.getBlock() instanceof NetheriteShulkerBoxBlock) {
            Direction direction = blockState.getValue(NetheriteShulkerBoxBlock.FACING);
            AABB box = getCollisionBox(direction).move(worldPosition);
            List<Entity> list = level.getEntities(null, box);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = list.get(i);
                    if (entity.getPistonPushReaction() != PushReaction.IGNORE) {
                        double d = 0.0D;
                        double e = 0.0D;
                        double f = 0.0D;
                        AABB box2 = entity.getBoundingBox();
                        switch (direction.getAxis()) {
                            case X:
                                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    d = box.maxX - box2.minX;
                                } else {
                                    d = box2.maxX - box.minX;
                                }

                                d += 0.01D;
                                break;
                            case Y:
                                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    e = box.maxY - box2.minY;
                                } else {
                                    e = box2.maxY - box.minY;
                                }

                                e += 0.01D;
                                break;
                            case Z:
                                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                                    f = box.maxZ - box2.minZ;
                                } else {
                                    f = box2.maxZ - box.minZ;
                                }

                                f += 0.01D;
                        }

                        entity.move(MoverType.SHULKER_BOX, new Vec3(d * direction.getStepX(), e * direction.getStepY(), f * direction.getStepZ()));
                    }
                }

            }
        }
    }

    public CompoundTag serializeInventory(CompoundTag tag) {
        if (!trySaveLootTable(tag)) {
            ContainerHelper.saveAllItems(tag, inventory, false);
        }

        return tag;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> list) {
        inventory = list;
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
    public void tick() {
        updateAnimation();
        if (animationStage == AnimationStage.OPENING || animationStage == AnimationStage.CLOSING) {
            pushEntities();
        }

    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        return serializeInventory(tag);
    }

    protected void updateAnimation() {
        prevAnimationProgress = animationProgress;
        switch (animationStage) {
            case CLOSED:
                animationProgress = 0.0F;
                break;
            case OPENING:
                animationProgress += 0.1F;
                if (animationProgress >= 1.0F) {
                    pushEntities();
                    animationStage = AnimationStage.OPENED;
                    animationProgress = 1.0F;
                    updateNeighborStates();
                }
                break;
            case CLOSING:
                animationProgress -= 0.1F;
                if (animationProgress <= 0.0F) {
                    animationStage = AnimationStage.CLOSED;
                    animationProgress = 0.0F;
                    updateNeighborStates();
                }
                break;
            case OPENED:
                animationProgress = 1.0F;
        }

    }

    private void updateNeighborStates() {
        getBlockState().updateNeighbourShapes(getLevel(), getBlockPos(), 3);
    }
}
