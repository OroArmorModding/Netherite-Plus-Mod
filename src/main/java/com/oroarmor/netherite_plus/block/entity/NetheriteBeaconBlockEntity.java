package com.oroarmor.netherite_plus.block.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;

public class NetheriteBeaconBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Tickable {

	public static final StatusEffect[][] EFFECTS_BY_LEVEL;
	private static final Set<StatusEffect> EFFECTS;
	private List<NetheriteBeaconBlockEntity.BeamSegment> beamSegments = Lists.newArrayList();
	private List<NetheriteBeaconBlockEntity.BeamSegment> field_19178 = Lists.newArrayList();
	private int level;
	private int netheriteLevel;

	public int getNetheriteLevel() {
		return netheriteLevel;
	}

	private int field_19179 = -1;
	@Nullable
	private StatusEffect primary;
	@Nullable
	private StatusEffect secondary;
	@Nullable
	private StatusEffect tertiary;
	@Nullable
	private Text customName;
	private ContainerLock lock;
	private final PropertyDelegate propertyDelegate;

	public NetheriteBeaconBlockEntity() {
		super(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY);
		lock = ContainerLock.EMPTY;
		propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				switch (index) {
					case 0:
						return level;
					case 1:
						return StatusEffect.getRawId(primary);
					case 2:
						return StatusEffect.getRawId(secondary);
					case 3:
						return StatusEffect.getRawId(tertiary);
					default:
						return 0;
				}
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0:
						level = value;
						break;
					case 1:
						if (!NetheriteBeaconBlockEntity.this.world.isClient && !beamSegments.isEmpty()) {
							NetheriteBeaconBlockEntity.this.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT);
						}

						primary = NetheriteBeaconBlockEntity.getPotionEffectById(value);
						break;
					case 2:
						secondary = NetheriteBeaconBlockEntity.getPotionEffectById(value);
					case 3:
						tertiary = NetheriteBeaconBlockEntity.getPotionEffectById(value);
				}

			}

			@Override
			public int size() {
				return 4;
			}
		};
	}

	@Override
	public void tick() {
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();
		BlockPos blockPos2;
		if (field_19179 < j) {
			blockPos2 = pos;
			field_19178 = Lists.newArrayList();
			field_19179 = blockPos2.getY() - 1;
		} else {
			blockPos2 = new BlockPos(i, field_19179 + 1, k);
		}

		BeamSegment beamSegment = field_19178.isEmpty() ? null : field_19178.get(field_19178.size() - 1);
		int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, i, k);

		int n;
		for (n = 0; n < 10 && blockPos2.getY() <= l; ++n) {
			BlockState blockState = world.getBlockState(blockPos2);
			Block block = blockState.getBlock();
			if (block instanceof Stainable) {
				float[] fs = ((Stainable) block).getColor().getColorComponents();
				if (field_19178.size() <= 1) {
					beamSegment = new BeamSegment(fs);
					field_19178.add(beamSegment);
				} else if (beamSegment != null) {
					if (Arrays.equals(fs, beamSegment.color)) {
						beamSegment.increaseHeight();
					} else {
						beamSegment = new BeamSegment(new float[] { (beamSegment.color[0] + fs[0]) / 2.0F,
								(beamSegment.color[1] + fs[1]) / 2.0F, (beamSegment.color[2] + fs[2]) / 2.0F });
						field_19178.add(beamSegment);
					}
				}
			} else {
				if (beamSegment == null || blockState.getOpacity(world, blockPos2) >= 15 && block != Blocks.BEDROCK) {
					field_19178.clear();
					field_19179 = l;
					break;
				}

				beamSegment.increaseHeight();
			}

			blockPos2 = blockPos2.up();
			++field_19179;
		}

		n = level;
		if (world.getTime() % 80L == 0L) {
			if (!beamSegments.isEmpty()) {
				updateLevel(i, j, k);
				if (netheriteLevel == 164) {
					List<ServerPlayerEntity> var14 = this.world.getNonSpectatingEntities(ServerPlayerEntity.class,
							(new Box(i, j, k, i, j - 4, k)).expand(10.0D, 5.0D, 10.0D));

					for (ServerPlayerEntity serverPlayerEntity : var14) {
						NetheritePlusCriteria.FULL_NETHERITE_NETHERITE_BEACON.trigger(serverPlayerEntity, this);
					}

				}

				if (level == 4) {
					List<ServerPlayerEntity> var14 = this.world.getNonSpectatingEntities(ServerPlayerEntity.class,
							(new Box(i, j, k, i, j - 4, k)).expand(10.0D, 5.0D, 10.0D));

					for (ServerPlayerEntity serverPlayerEntity : var14) {
						NetheritePlusCriteria.CONSTRUCT_NETHERITE_BEACON.trigger(serverPlayerEntity, this);
					}
				}
			}

			if (level > 0 && !beamSegments.isEmpty()) {
				applyPlayerEffects();
				playSound(SoundEvents.BLOCK_BEACON_AMBIENT);
			}
		}

		if (field_19179 >= l) {
			field_19179 = -1;
			boolean bl = n > 0;
			beamSegments = field_19178;
			if (!world.isClient) {
				boolean bl2 = level > 0;
				if (!bl && bl2) {
					playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
				} else if (bl && !bl2) {
					playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
				}
			}
		}

	}

	private void updateLevel(int x, int y, int z) {
		level = 0;
		netheriteLevel = 0;

		for (int i = 1; i <= 4; level = i++) {
			int j = y - i;
			if (j < 0) {
				break;
			}

			boolean bl = true;

			for (int k = x - i; k <= x + i && bl; ++k) {
				for (int l = z - i; l <= z + i; ++l) {
					if (world.getBlockState(new BlockPos(k, j, l)).getBlock() == Blocks.NETHERITE_BLOCK) {
						netheriteLevel++;
					}
					if (!world.getBlockState(new BlockPos(k, j, l)).isIn(BlockTags.BEACON_BASE_BLOCKS)) {
						bl = false;
						break;
					}
				}
			}

			if (!bl) {
				break;
			}
		}

	}

	@Override
	public void markRemoved() {
		playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
		super.markRemoved();
	}

	private void applyPlayerEffects() {
		if (!world.isClient && primary != null) {
			double effectBoundingBox = level * 10 + 10;
			int primaryEffectLevel = 0;
			int secondaryEffectLevel = 0;
			if (level >= 4) {
				if (primary == secondary) {
					primaryEffectLevel++;
				}

				if (primary == tertiary) {
					primaryEffectLevel++;
				}

				if (secondary == tertiary) {
					secondaryEffectLevel++;
				}

			}

			int effectLength = (9 + level * 2) * 20;
			Box box = new Box(pos).expand(effectBoundingBox).stretch(0.0D, world.getHeight(), 0.0D);
			List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);

			for (PlayerEntity player : list) {
				player.addStatusEffect(new StatusEffectInstance(primary, effectLength, primaryEffectLevel, true, true));
				player.addStatusEffect(
						new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, effectLength, 0, true, true));
				player.addStatusEffect(new StatusEffectInstance(NetheritePlusStatusEffects.LAVA_VISION, effectLength,
						Math.min(netheriteLevel, 127), true, true));

				// regeneration case
				if (level >= 4 && primary != secondary && secondary != null) {
					player.addStatusEffect(
							new StatusEffectInstance(secondary, effectLength, secondaryEffectLevel, true, true));
				}
			}

			if (tertiary == StatusEffects.GLOWING) {
				List<MobEntity> entities = world.getNonSpectatingEntities(MobEntity.class, box);
				for (LivingEntity entity : entities) {
					entity.addStatusEffect(
							new StatusEffectInstance(StatusEffects.GLOWING, effectLength, 0, true, true));
				}
			}

		}
	}

	public void playSound(SoundEvent soundEvent) {
		world.playSound((PlayerEntity) null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	@Environment(EnvType.CLIENT)
	public List<NetheriteBeaconBlockEntity.BeamSegment> getBeamSegments() {
		return level == 0 ? ImmutableList.of() : beamSegments;
	}

	public int getLevel() {
		return level;
	}

	@Override
	@Nullable
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(pos, 3, toInitialChunkDataTag());
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return toTag(new CompoundTag());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public double getSquaredRenderDistance() {
		return 256.0D;
	}

	@Nullable
	private static StatusEffect getPotionEffectById(int id) {
		StatusEffect statusEffect = StatusEffect.byRawId(id);
		return EFFECTS.contains(statusEffect) ? statusEffect : null;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		primary = getPotionEffectById(tag.getInt("Primary"));
		secondary = getPotionEffectById(tag.getInt("Secondary"));
		tertiary = getPotionEffectById(tag.getInt("Tertiary"));
		netheriteLevel = tag.getInt("NetheriteLevel");
		if (tag.contains("CustomName", 8)) {
			customName = Text.Serializer.fromJson(tag.getString("CustomName"));
		}

		lock = ContainerLock.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("Primary", StatusEffect.getRawId(primary));
		tag.putInt("Secondary", StatusEffect.getRawId(secondary));
		tag.putInt("Tertiary", StatusEffect.getRawId(tertiary));
		tag.putInt("Levels", level);
		tag.putInt("NetheriteLevel", netheriteLevel);
		if (customName != null) {
			tag.putString("CustomName", Text.Serializer.toJson(customName));
		}

		lock.toTag(tag);
		return tag;
	}

	public void setCustomName(Text text) {
		customName = text;
	}

	@Override
	@Nullable
	public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return LockableContainerBlockEntity.checkUnlocked(playerEntity, lock, getDisplayName())
				? new NetheriteBeaconScreenHandler(i, playerInventory, propertyDelegate,
						ScreenHandlerContext.create(world, getPos()))
				: null;
	}

	@Override
	public Text getDisplayName() {
		return customName != null ? customName : new TranslatableText("container.netherite_beacon");
	}

	static {
		EFFECTS_BY_LEVEL = new StatusEffect[][] { { StatusEffects.SPEED, StatusEffects.HASTE },
				{ StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST }, { StatusEffects.STRENGTH },
				{ StatusEffects.REGENERATION }, { StatusEffects.GLOWING } };
		EFFECTS = Arrays.stream(EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());
	}

	public static class BeamSegment {
		private final float[] color;
		private int height;

		public BeamSegment(float[] color) {
			this.color = color;
			height = 1;
		}

		protected void increaseHeight() {
			++height;
		}

		@Environment(EnvType.CLIENT)
		public float[] getColor() {
			return color;
		}

		@Environment(EnvType.CLIENT)
		public int getHeight() {
			return height;
		}
	}
}
