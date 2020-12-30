package com.oroarmor.netherite_plus.block;

import java.util.Iterator;
import java.util.List;

import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity.AnimationStage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ShulkerSharedHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NetheriteShulkerBoxBlock extends BaseEntityBlock {

    public static final EnumProperty<Direction> FACING;
    public static final ResourceLocation CONTENTS;
    public static int numberOfRows = 6;

    public static int numberOfSlots = numberOfRows * 9;

    static {
        FACING = DirectionalBlock.FACING;
        CONTENTS = new ResourceLocation("contents");
    }

    public static Block get(DyeColor dyeColor) {
        if (dyeColor == null) {
            return NetheritePlusBlocks.NETHERITE_SHULKER_BOX.get();
        }
        switch (dyeColor) {
            case WHITE:
                return NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX.get();
            case ORANGE:
                return NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX.get();
            case MAGENTA:
                return NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX.get();
            case LIGHT_BLUE:
                return NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get();
            case YELLOW:
                return NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX.get();
            case LIME:
                return NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX.get();
            case PINK:
                return NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX.get();
            case GRAY:
                return NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX.get();
            case LIGHT_GRAY:
                return NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get();
            case CYAN:
                return NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX.get();
            case PURPLE:
            default:
                return NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX.get();
            case BLUE:
                return NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX.get();
            case BROWN:
                return NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX.get();
            case GREEN:
                return NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX.get();
            case RED:
                return NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX.get();
            case BLACK:
                return NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX.get();
        }
    }

    @Environment(EnvType.CLIENT)
    public static DyeColor getColor(Block block) {
        return block instanceof NetheriteShulkerBoxBlock ? ((NetheriteShulkerBoxBlock) block).getColor() : null;
    }

    @Environment(EnvType.CLIENT)
    public static DyeColor getColor(Item item) {
        return getColor(Block.byItem(item));
    }

    public static ItemStack getItemStack(DyeColor color) {
        return new ItemStack(get(color));
    }

    private final DyeColor color;

    public NetheriteShulkerBoxBlock(DyeColor color, BlockBehaviour.Properties settings) {
        super(settings);
        this.color = color;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.UP));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendHoverText(ItemStack stack, BlockGetter world, List<Component> tooltip, TooltipFlag options) {
        super.appendHoverText(stack, world, tooltip, options);
        CompoundTag compoundTag = stack.getTagElement("BlockEntityTag");
        if (compoundTag != null) {
            if (compoundTag.contains("LootTable", 8)) {
                tooltip.add(new TextComponent("???????"));
            }

            if (compoundTag.contains("Items", 9)) {
                NonNullList<ItemStack> defaultedList = NonNullList.withSize(numberOfSlots, ItemStack.EMPTY);
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
                            MutableComponent mutableText = itemStack.getHoverName().copy();
                            mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
                            tooltip.add(mutableText);
                        }
                    }
                }

                if (j - i > 0) {
                    tooltip.add(new TranslatableComponent("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC));
                }
            }
        }

    }

    @Override
    public BlockEntity newBlockEntity(BlockGetter world) {
        return new NetheriteShulkerBoxBlockEntity(this.getColor());
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromContainer((Container) world.getBlockEntity(pos));
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
            NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
            builder = builder.withDynamicDrop(CONTENTS, (lootContext, consumer) -> {
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
    @Environment(EnvType.CLIENT)
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
    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.DESTROY;
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
        if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
            NetheriteShulkerBoxBlockEntity netheriteShulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
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
            if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
                NetheriteShulkerBoxBlockEntity netheriteShulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
                boolean bl2;
                if (netheriteShulkerBoxBlockEntity.getAnimationStage() == AnimationStage.CLOSED) {
                    Direction direction = state.getValue(FACING);
                    bl2 = world.noCollision(ShulkerSharedHelper.openBoundingBox(pos, direction));
                } else {
                    bl2 = true;
                }

                if (bl2) {
                    player.openMenu(netheriteShulkerBoxBlockEntity);
                    player.awardStat(Stats.OPEN_SHULKER_BOX);
                    PiglinAi.angerNearbyPiglins(player, true);
                }

                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

}
