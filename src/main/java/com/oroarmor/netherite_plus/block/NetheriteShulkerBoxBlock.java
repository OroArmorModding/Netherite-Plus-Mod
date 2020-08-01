package com.oroarmor.netherite_plus.block;

import java.util.Iterator;
import java.util.List;

import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity.AnimationStage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.FacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.mob.ShulkerLidCollisions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class NetheriteShulkerBoxBlock extends BlockWithEntity {

	public static final EnumProperty<Direction> FACING;
	public static final Identifier CONTENTS;
	private final DyeColor color;

	public static int numberOfRows = 6;
	public static int numberOfSlots = numberOfRows * 9;

	public NetheriteShulkerBoxBlock(DyeColor color, AbstractBlock.Settings settings) {
		super(settings);
		this.color = color;
		setDefaultState(stateManager.getDefaultState().with(FACING, Direction.UP));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else if (player.isSpectator()) {
			return ActionResult.CONSUME;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
				NetheriteShulkerBoxBlockEntity netheriteShulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
				boolean bl2;
				if (netheriteShulkerBoxBlockEntity.getAnimationStage() == AnimationStage.CLOSED) {
					Direction direction = state.get(FACING);
					bl2 = world.doesNotCollide(ShulkerLidCollisions.getLidCollisionBox(pos, direction));
				} else {
					bl2 = true;
				}

				if (bl2) {
					player.openHandledScreen(netheriteShulkerBoxBlockEntity);
					player.incrementStat(Stats.OPEN_SHULKER_BOX);
					PiglinBrain.onGuardedBlockBroken(player, true);
				}

				return ActionResult.CONSUME;
			}
			return ActionResult.PASS;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(FACING, ctx.getSide());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
			NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
			if (!world.isClient && player.isCreative() && !shulkerBoxBlockEntity.isEmpty()) {
				ItemStack itemStack = getItemStack(this.getColor());
				CompoundTag compoundTag = shulkerBoxBlockEntity.serializeInventory(new CompoundTag());
				if (!compoundTag.isEmpty()) {
					itemStack.putSubTag("BlockEntityTag", compoundTag);
				}

				if (shulkerBoxBlockEntity.hasCustomName()) {
					itemStack.setCustomName(shulkerBoxBlockEntity.getCustomName());
				}

				ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
						itemStack);
				itemEntity.setToDefaultPickupDelay();
				world.spawnEntity(itemEntity);
			} else {
				shulkerBoxBlockEntity.checkLootInteraction(player);
			}
		}

		super.onBreak(world, pos, state, player);
	}

	@Override
	public List<ItemStack> getDroppedStacks(BlockState state, LootContext.Builder builder) {
		BlockEntity blockEntity = builder.getNullable(LootContextParameters.BLOCK_ENTITY);
		if (blockEntity instanceof NetheriteShulkerBoxBlockEntity) {
			NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
			builder = builder.putDrop(CONTENTS, (lootContext, consumer) -> {
				for (int i = 0; i < shulkerBoxBlockEntity.size(); ++i) {
					consumer.accept(shulkerBoxBlockEntity.getStack(i));
				}

			});
		}

		return super.getDroppedStacks(state, builder);
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
	@Environment(EnvType.CLIENT)
	public void buildTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		super.buildTooltip(stack, world, tooltip, options);
		CompoundTag compoundTag = stack.getSubTag("BlockEntityTag");
		if (compoundTag != null) {
			if (compoundTag.contains("LootTable", 8)) {
				tooltip.add(new LiteralText("???????"));
			}

			if (compoundTag.contains("Items", 9)) {
				DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(numberOfSlots, ItemStack.EMPTY);
				Inventories.fromTag(compoundTag, defaultedList);
				int i = 0;
				int j = 0;
				Iterator<ItemStack> var9 = defaultedList.iterator();

				while (var9.hasNext()) {
					ItemStack itemStack = var9.next();
					if (!itemStack.isEmpty()) {
						++j;
						if (i <= 4) {
							++i;
							MutableText mutableText = itemStack.getName().shallowCopy();
							mutableText.append(" x").append(String.valueOf(itemStack.getCount()));
							tooltip.add(mutableText);
						}
					}
				}

				if (j - i > 0) {
					tooltip.add(new TranslatableText("container.shulkerBox.more", new Object[] { j - i })
							.formatted(Formatting.ITALIC));
				}
			}
		}

	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		return blockEntity instanceof NetheriteShulkerBoxBlockEntity
				? VoxelShapes.cuboid(((NetheriteShulkerBoxBlockEntity) blockEntity).getBoundingBox(state))
				: VoxelShapes.fullCube();
	}

	@Override
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput((Inventory) world.getBlockEntity(pos));
	}

	@Override
	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		ItemStack itemStack = super.getPickStack(world, pos, state);
		NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) world
				.getBlockEntity(pos);
		CompoundTag compoundTag = shulkerBoxBlockEntity.serializeInventory(new CompoundTag());
		if (!compoundTag.isEmpty()) {
			itemStack.putSubTag("BlockEntityTag", compoundTag);
		}

		return itemStack;
	}

	@Environment(EnvType.CLIENT)
	public static DyeColor getColor(Item item) {
		return getColor(Block.getBlockFromItem(item));
	}

	@Environment(EnvType.CLIENT)
	public static DyeColor getColor(Block block) {
		return block instanceof NetheriteShulkerBoxBlock ? ((NetheriteShulkerBoxBlock) block).getColor() : null;
	}

	public DyeColor getColor() {
		return color;
	}

	public static ItemStack getItemStack(DyeColor color) {
		return new ItemStack(get(color));
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	static {
		FACING = FacingBlock.FACING;
		CONTENTS = new Identifier("contents");
	}

	public static Block get(DyeColor dyeColor) {
		if (dyeColor == null) {
			return NetheritePlusModBlocks.NETHERITE_SHULKER_BOX;
		}
		switch (dyeColor) {
			case WHITE:
				return NetheritePlusModBlocks.NETHERITE_WHITE_SHULKER_BOX;
			case ORANGE:
				return NetheritePlusModBlocks.NETHERITE_ORANGE_SHULKER_BOX;
			case MAGENTA:
				return NetheritePlusModBlocks.NETHERITE_MAGENTA_SHULKER_BOX;
			case LIGHT_BLUE:
				return NetheritePlusModBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX;
			case YELLOW:
				return NetheritePlusModBlocks.NETHERITE_YELLOW_SHULKER_BOX;
			case LIME:
				return NetheritePlusModBlocks.NETHERITE_LIME_SHULKER_BOX;
			case PINK:
				return NetheritePlusModBlocks.NETHERITE_PINK_SHULKER_BOX;
			case GRAY:
				return NetheritePlusModBlocks.NETHERITE_GRAY_SHULKER_BOX;
			case LIGHT_GRAY:
				return NetheritePlusModBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX;
			case CYAN:
				return NetheritePlusModBlocks.NETHERITE_CYAN_SHULKER_BOX;
			case PURPLE:
			default:
				return NetheritePlusModBlocks.NETHERITE_PURPLE_SHULKER_BOX;
			case BLUE:
				return NetheritePlusModBlocks.NETHERITE_BLUE_SHULKER_BOX;
			case BROWN:
				return NetheritePlusModBlocks.NETHERITE_BROWN_SHULKER_BOX;
			case GREEN:
				return NetheritePlusModBlocks.NETHERITE_GREEN_SHULKER_BOX;
			case RED:
				return NetheritePlusModBlocks.NETHERITE_RED_SHULKER_BOX;
			case BLACK:
				return NetheritePlusModBlocks.NETHERITE_BLACK_SHULKER_BOX;
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new NetheriteShulkerBoxBlockEntity(this.getColor());
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

}
