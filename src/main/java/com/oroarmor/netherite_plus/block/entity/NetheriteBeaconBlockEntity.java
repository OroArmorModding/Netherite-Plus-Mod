package com.oroarmor.netherite_plus.block.entity;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Stainable;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ContainerLock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.BeaconScreenHandler;
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

public class NetheriteBeaconBlockEntity extends BeaconBlockEntity implements NamedScreenHandlerFactory, Tickable {
	public static final StatusEffect[][] EFFECTS_BY_LEVEL;
	private static final Set<StatusEffect> EFFECTS;
	private List<NetheriteBeaconBlockEntity.BeamSegment> beamSegments = Lists.newArrayList();
	private List<NetheriteBeaconBlockEntity.BeamSegment> field_19178 = Lists.newArrayList();
	private int level;
	private int field_19179 = -1;

	private StatusEffect primary;

	private StatusEffect secondary;

	private StatusEffect tertiary = getPotionEffectById(12);
	private Text customName;
	private ContainerLock lock;

	private final PropertyDelegate propertyDelegate;

	public NetheriteBeaconBlockEntity() {
		this.lock = ContainerLock.EMPTY;
		this.propertyDelegate = new PropertyDelegate() {
			@Override
			public int get(int index) {
				switch (index) {
					case 0:
						return NetheriteBeaconBlockEntity.this.level;
					case 1:
						return StatusEffect.getRawId(NetheriteBeaconBlockEntity.this.primary);
					case 2:
						return StatusEffect.getRawId(NetheriteBeaconBlockEntity.this.secondary);
					default:
						return 0;
				}
			}

			@Override
			public void set(int index, int value) {
				switch (index) {
					case 0:
						NetheriteBeaconBlockEntity.this.level = value;
						break;
					case 1:
						if (!NetheriteBeaconBlockEntity.this.world.isClient
								&& !NetheriteBeaconBlockEntity.this.beamSegments.isEmpty()) {
							NetheriteBeaconBlockEntity.this.playSound(SoundEvents.BLOCK_BEACON_POWER_SELECT);
						}

						NetheriteBeaconBlockEntity.this.primary = NetheriteBeaconBlockEntity.getPotionEffectById(value);
						break;
					case 2:
						NetheriteBeaconBlockEntity.this.secondary = NetheriteBeaconBlockEntity
								.getPotionEffectById(value);
				}

			}

			@Override
			public int size() {
				return 3;
			}
		};
	}

	@Override
	public void tick() {
		int i = this.pos.getX();
		int j = this.pos.getY();
		int k = this.pos.getZ();
		BlockPos blockPos2;
		if (this.field_19179 < j) {
			blockPos2 = this.pos;
			this.field_19178 = Lists.newArrayList();
			this.field_19179 = blockPos2.getY() - 1;
		} else {
			blockPos2 = new BlockPos(i, this.field_19179 + 1, k);
		}

		NetheriteBeaconBlockEntity.BeamSegment beamSegment = this.field_19178.isEmpty() ? null
				: this.field_19178.get(this.field_19178.size() - 1);
		int l = this.world.getTopY(Heightmap.Type.WORLD_SURFACE, i, k);

		int n;
		for (n = 0; n < 10 && blockPos2.getY() <= l; ++n) {
			BlockState blockState = this.world.getBlockState(blockPos2);
			Block block = blockState.getBlock();
			if (block instanceof Stainable) {
				float[] fs = ((Stainable) block).getColor().getColorComponents();
				if (this.field_19178.size() <= 1) {
					beamSegment = new NetheriteBeaconBlockEntity.BeamSegment(fs);
					this.field_19178.add(beamSegment);
				} else if (beamSegment != null) {
					if (Arrays.equals(fs, beamSegment.color)) {
						beamSegment.increaseHeight();
					} else {
						beamSegment = new NetheriteBeaconBlockEntity.BeamSegment(
								new float[] { (beamSegment.color[0] + fs[0]) / 2.0F,
										(beamSegment.color[1] + fs[1]) / 2.0F, (beamSegment.color[2] + fs[2]) / 2.0F });
						this.field_19178.add(beamSegment);
					}
				}
			} else {
				if (beamSegment == null
						|| blockState.getOpacity(this.world, blockPos2) >= 15 && block != Blocks.BEDROCK) {
					this.field_19178.clear();
					this.field_19179 = l;
					break;
				}

				beamSegment.increaseHeight();
			}

			blockPos2 = blockPos2.up();
			++this.field_19179;
		}

		n = this.level;
		if (this.world.getTime() % 80L == 0L) {
			if (!this.beamSegments.isEmpty()) {
				this.updateLevel(i, j, k);
			}

			if (this.level > 0 && !this.beamSegments.isEmpty()) {
				this.applyPlayerEffects();
				this.playSound(SoundEvents.BLOCK_BEACON_AMBIENT);
			}
		}

		if (this.field_19179 >= l) {
			this.field_19179 = -1;
			boolean bl = n > 0;
			this.beamSegments = this.field_19178;
			if (!this.world.isClient) {
				boolean bl2 = this.level > 0;
				if (!bl && bl2) {
					this.playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
					Iterator<ServerPlayerEntity> var14 = this.world.getNonSpectatingEntities(ServerPlayerEntity.class,
							(new Box(i, j, k, i, j - 4, k)).expand(10.0D, 5.0D, 10.0D)).iterator();

					while (var14.hasNext()) {
						ServerPlayerEntity serverPlayerEntity = var14.next();
						Criteria.CONSTRUCT_BEACON.trigger(serverPlayerEntity, this);
					}
				} else if (bl && !bl2) {
					this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
				}
			}
		}

	}

	private void updateLevel(int x, int y, int z) {
		this.level = 0;

		for (int i = 1; i <= 4; this.level = i++) {
			int j = y - i;
			if (j < 0) {
				break;
			}

			boolean bl = true;

			for (int k = x - i; k <= x + i && bl; ++k) {
				for (int l = z - i; l <= z + i; ++l) {
					if (!this.world.getBlockState(new BlockPos(k, j, l)).isIn(BlockTags.BEACON_BASE_BLOCKS)) {
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
		this.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
		super.markRemoved();
	}

	private void applyPlayerEffects() {
		if (!this.world.isClient && this.primary != null) {
			double d = this.level * 10 + 10;
			int i = 0;
			if (this.level >= 4 && this.primary == this.secondary) {
				i = 1;
			}

			int j = (9 + this.level * 2) * 20;
			Box box = (new Box(this.pos)).expand(d).stretch(0.0D, this.world.getHeight(), 0.0D);
			List<PlayerEntity> list = this.world.getNonSpectatingEntities(PlayerEntity.class, box);
			Iterator<PlayerEntity> var7 = list.iterator();

			PlayerEntity playerEntity2;
			while (var7.hasNext()) {
				playerEntity2 = var7.next();
				playerEntity2.addStatusEffect(new StatusEffectInstance(this.tertiary, j, 0, true, true));
			}
			while (var7.hasNext()) {
				playerEntity2 = var7.next();
				playerEntity2.addStatusEffect(new StatusEffectInstance(this.primary, j, i, true, true));
			}

			if (this.level >= 4 && this.primary != this.secondary && this.secondary != null) {
				var7 = list.iterator();

				while (var7.hasNext()) {
					playerEntity2 = var7.next();
					playerEntity2.addStatusEffect(new StatusEffectInstance(this.secondary, j, 0, true, true));
				}
			}

		}
	}

	@Override
	public void playSound(SoundEvent soundEvent) {
		this.world.playSound((PlayerEntity) null, this.pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	@SuppressWarnings("unchecked")
	@Environment(EnvType.CLIENT)
	@Override
	public List<BeaconBlockEntity.BeamSegment> getBeamSegments() {
		return (List<net.minecraft.block.entity.BeaconBlockEntity.BeamSegment>) (this.level == 0 ? ImmutableList.of()
				: this.beamSegments);
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return new BlockEntityUpdateS2CPacket(this.pos, 3, this.toInitialChunkDataTag());
	}

	@Override
	public CompoundTag toInitialChunkDataTag() {
		return this.toTag(new CompoundTag());
	}

	@Override
	@Environment(EnvType.CLIENT)
	public double getSquaredRenderDistance() {
		return 256.0D;
	}

	private static StatusEffect getPotionEffectById(int id) {
		StatusEffect statusEffect = StatusEffect.byRawId(id);
		return EFFECTS.contains(statusEffect) ? statusEffect : null;
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		this.primary = getPotionEffectById(tag.getInt("Primary"));
		this.secondary = getPotionEffectById(tag.getInt("Secondary"));
		if (tag.contains("CustomName", 8)) {
			this.customName = Text.Serializer.fromJson(tag.getString("CustomName"));
		}

		this.lock = ContainerLock.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putInt("Primary", StatusEffect.getRawId(this.primary));
		tag.putInt("Secondary", StatusEffect.getRawId(this.secondary));
		tag.putInt("Levels", this.level);
		if (this.customName != null) {
			tag.putString("CustomName", Text.Serializer.toJson(this.customName));
		}

		this.lock.toTag(tag);
		return tag;
	}

	@Override
	public void setCustomName(Text text) {
		this.customName = text;
	}

	@Override
	public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
		return LockableContainerBlockEntity.checkUnlocked(playerEntity, this.lock, this.getDisplayName())
				? new BeaconScreenHandler(i, playerInventory, this.propertyDelegate,
						ScreenHandlerContext.create(this.world, this.getPos()))
				: null;
	}

	@Override
	public Text getDisplayName() {
		return this.customName != null ? this.customName : new TranslatableText("container.beacon");
	}

	static {
		EFFECTS_BY_LEVEL = new StatusEffect[][] { { StatusEffects.SPEED, StatusEffects.HASTE },
				{ StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST }, { StatusEffects.STRENGTH },
				{ StatusEffects.REGENERATION } };
		EFFECTS = Arrays.stream(EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());
	}

	public static class BeamSegment {
		private final float[] color;
		private int height;

		public BeamSegment(float[] color) {
			this.color = color;
			this.height = 1;
		}

		protected void increaseHeight() {
			++this.height;
		}

		@Environment(EnvType.CLIENT)
		public float[] getColor() {
			return this.color;
		}

		@Environment(EnvType.CLIENT)
		public int getHeight() {
			return this.height;
		}
	}
}
