package com.oroarmor.netherite_plus.entity;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.FluidTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class NetheriteFishingBobberEntity extends ProjectileEntity {
	private final Random velocityRandom;
	private boolean caughtFish;
	private int outOfOpenWaterTicks;
	private static final TrackedData<Integer> HOOK_ENTITY_ID;
	private static final TrackedData<Boolean> CAUGHT_FISH;
	private int removalTimer;
	private int hookCountdown;
	private int waitCountdown;
	private int fishTravelCountdown;
	private float fishAngle;
	private boolean inOpenWater;
	private Entity hookedEntity;
	private NetheriteFishingBobberEntity.State state;
	private final int luckOfTheSeaLevel;
	private final int lureLevel;

	private NetheriteFishingBobberEntity(World world, PlayerEntity owner, int lureLevel, int luckOfTheSeaLevel) {
		super(EntityType.FISHING_BOBBER, world);
		this.velocityRandom = new Random();
		this.inOpenWater = true;
		this.state = NetheriteFishingBobberEntity.State.FLYING;
		this.ignoreCameraFrustum = true;
		this.setOwner(owner);
		owner.fishHook = this;
		this.luckOfTheSeaLevel = Math.max(0, lureLevel);
		this.lureLevel = Math.max(0, luckOfTheSeaLevel);
	}

	@Environment(EnvType.CLIENT)
	public NetheriteFishingBobberEntity(World world, PlayerEntity thrower, double x, double y, double z) {
		this(world, thrower, 0, 0);
		this.updatePosition(x, y, z);
		this.prevX = this.getX();
		this.prevY = this.getY();
		this.prevZ = this.getZ();
	}

	public NetheriteFishingBobberEntity(PlayerEntity thrower, World world, int lureLevel, int luckOfTheSeaLevel) {
		this(world, thrower, lureLevel, luckOfTheSeaLevel);
		float f = thrower.pitch;
		float g = thrower.yaw;
		float h = MathHelper.cos(-g * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-g * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-f * 0.017453292F);
		float k = MathHelper.sin(-f * 0.017453292F);
		double d = thrower.getX() - i * 0.3D;
		double e = thrower.getEyeY();
		double l = thrower.getZ() - h * 0.3D;
		this.refreshPositionAndAngles(d, e, l, g, f);
		Vec3d vec3d = new Vec3d((-i), MathHelper.clamp(-(k / j), -5.0F, 5.0F), (-h));
		double m = vec3d.length();
		vec3d = vec3d.multiply(0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D,
				0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D,
				0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D);
		this.setVelocity(vec3d);
		this.yaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
		this.pitch = (float) (MathHelper.atan2(vec3d.y, MathHelper.sqrt(squaredHorizontalLength(vec3d)))
				* 57.2957763671875D);
		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(HOOK_ENTITY_ID, 0);
		this.getDataTracker().startTracking(CAUGHT_FISH, false);
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (HOOK_ENTITY_ID.equals(data)) {
			int i = this.getDataTracker().get(HOOK_ENTITY_ID);
			this.hookedEntity = i > 0 ? this.world.getEntityById(i - 1) : null;
		}

		if (CAUGHT_FISH.equals(data)) {
			this.caughtFish = this.getDataTracker().get(CAUGHT_FISH);
			if (this.caughtFish) {
				this.setVelocity(this.getVelocity().x, -0.4F * MathHelper.nextFloat(this.velocityRandom, 0.6F, 1.0F),
						this.getVelocity().z);
			}
		}

		super.onTrackedDataSet(data);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		double d = 64.0D;
		return distance < 4096.0D;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch,
			int interpolationSteps, boolean interpolate) {
	}

	@Override
	public void tick() {
		this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.world.getTime());
		super.tick();
		PlayerEntity playerEntity = this.getOwner();
		if (playerEntity == null) {
			this.remove();
		} else if (this.world.isClient || !this.removeIfInvalid(playerEntity)) {
			if (this.onGround) {
				++this.removalTimer;
				if (this.removalTimer >= 1200) {
					this.remove();
					return;
				}
			} else {
				this.removalTimer = 0;
			}

			float f = 0.0F;
			BlockPos blockPos = this.getBlockPos();
			FluidState fluidState = this.world.getFluidState(blockPos);
			if (fluidState.isIn(FluidTags.WATER)) {
				f = fluidState.getHeight(this.world, blockPos);
			}

			boolean bl = f > 0.0F;
			if (this.state == NetheriteFishingBobberEntity.State.FLYING) {
				if (this.hookedEntity != null) {
					this.setVelocity(Vec3d.ZERO);
					this.state = NetheriteFishingBobberEntity.State.HOOKED_IN_ENTITY;
					return;
				}

				if (bl) {
					this.setVelocity(this.getVelocity().multiply(0.3D, 0.2D, 0.3D));
					this.state = NetheriteFishingBobberEntity.State.BOBBING;
					return;
				}

				this.checkForCollision();
			} else {
				if (this.state == NetheriteFishingBobberEntity.State.HOOKED_IN_ENTITY) {
					if (this.hookedEntity != null) {
						if (this.hookedEntity.removed) {
							this.hookedEntity = null;
							this.state = NetheriteFishingBobberEntity.State.FLYING;
						} else {
							this.updatePosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D),
									this.hookedEntity.getZ());
						}
					}

					return;
				}

				if (this.state == NetheriteFishingBobberEntity.State.BOBBING) {
					Vec3d vec3d = this.getVelocity();
					double d = this.getY() + vec3d.y - blockPos.getY() - f;
					if (Math.abs(d) < 0.01D) {
						d += Math.signum(d) * 0.1D;
					}

					this.setVelocity(vec3d.x * 0.9D, vec3d.y - d * this.random.nextFloat() * 0.2D, vec3d.z * 0.9D);
					if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
						this.inOpenWater = true;
					} else {
						this.inOpenWater = this.inOpenWater && this.outOfOpenWaterTicks < 10
								&& this.isOpenOrWaterAround(blockPos);
					}

					if (bl) {
						this.outOfOpenWaterTicks = Math.max(0, this.outOfOpenWaterTicks - 1);
						if (this.caughtFish) {
							this.setVelocity(this.getVelocity().add(0.0D,
									-0.1D * this.velocityRandom.nextFloat() * this.velocityRandom.nextFloat(), 0.0D));
						}

						if (!this.world.isClient) {
							this.tickFishingLogic(blockPos);
						}
					} else {
						this.outOfOpenWaterTicks = Math.min(10, this.outOfOpenWaterTicks + 1);
					}
				}
			}

			if (!fluidState.isIn(FluidTags.WATER)) {
				this.setVelocity(this.getVelocity().add(0.0D, -0.03D, 0.0D));
			}

			this.move(MovementType.SELF, this.getVelocity());
			this.method_26962();
			if (this.state == NetheriteFishingBobberEntity.State.FLYING
					&& (this.onGround || this.horizontalCollision)) {
				this.setVelocity(Vec3d.ZERO);
			}

			double e = 0.92D;
			this.setVelocity(this.getVelocity().multiply(0.92D));
			this.refreshPosition();
		}
	}

	private boolean removeIfInvalid(PlayerEntity playerEntity) {
		ItemStack itemStack = playerEntity.getMainHandStack();
		ItemStack itemStack2 = playerEntity.getOffHandStack();
		boolean bl = itemStack.getItem() == Items.FISHING_ROD;
		boolean bl2 = itemStack2.getItem() == Items.FISHING_ROD;
		if (!playerEntity.removed && playerEntity.isAlive() && (bl || bl2)
				&& this.squaredDistanceTo(playerEntity) <= 1024.0D) {
			return false;
		} else {
			this.remove();
			return true;
		}
	}

	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getCollision(this, this::method_26958, RayTraceContext.ShapeType.COLLIDER);
		this.onCollision(hitResult);
	}

	@Override
	protected boolean method_26958(Entity entity) {
		return super.method_26958(entity) || entity.isAlive() && entity instanceof ItemEntity;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!this.world.isClient) {
			this.hookedEntity = entityHitResult.getEntity();
			this.updateHookedEntityId();
		}

	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		super.onBlockHit(blockHitResult);
		this.setVelocity(this.getVelocity().normalize().multiply(blockHitResult.squaredDistanceTo(this)));
	}

	private void updateHookedEntityId() {
		this.getDataTracker().set(HOOK_ENTITY_ID, this.hookedEntity.getEntityId() + 1);
	}

	private void tickFishingLogic(BlockPos pos) {
		ServerWorld serverWorld = (ServerWorld) this.world;
		int i = 1;
		BlockPos blockPos = pos.up();
		if (this.random.nextFloat() < 0.25F && this.world.hasRain(blockPos)) {
			++i;
		}

		if (this.random.nextFloat() < 0.5F && !this.world.isSkyVisible(blockPos)) {
			--i;
		}

		if (this.hookCountdown > 0) {
			--this.hookCountdown;
			if (this.hookCountdown <= 0) {
				this.waitCountdown = 0;
				this.fishTravelCountdown = 0;
				this.getDataTracker().set(CAUGHT_FISH, false);
			}
		} else {
			float n;
			float o;
			float p;
			double q;
			double r;
			double s;
			BlockState blockState2;
			if (this.fishTravelCountdown > 0) {
				this.fishTravelCountdown -= i;
				if (this.fishTravelCountdown > 0) {
					this.fishAngle = (float) (this.fishAngle + this.random.nextGaussian() * 4.0D);
					n = this.fishAngle * 0.017453292F;
					o = MathHelper.sin(n);
					p = MathHelper.cos(n);
					q = this.getX() + o * this.fishTravelCountdown * 0.1F;
					r = MathHelper.floor(this.getY()) + 1.0F;
					s = this.getZ() + p * this.fishTravelCountdown * 0.1F;
					blockState2 = serverWorld.getBlockState(new BlockPos(q, r - 1.0D, s));
					if (blockState2.isOf(Blocks.WATER)) {
						if (this.random.nextFloat() < 0.15F) {
							serverWorld.spawnParticles(ParticleTypes.BUBBLE, q, r - 0.10000000149011612D, s, 1,
									(double) o, 0.1D, (double) p, 0.0D);
						}

						float k = o * 0.04F;
						float l = p * 0.04F;
						serverWorld.spawnParticles(ParticleTypes.FISHING, q, r, s, 0, (double) l, 0.01D, (double) (-k),
								1.0D);
						serverWorld.spawnParticles(ParticleTypes.FISHING, q, r, s, 0, (double) (-l), 0.01D, (double) k,
								1.0D);
					}
				} else {
					this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F,
							1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
					double m = this.getY() + 0.5D;
					serverWorld.spawnParticles(ParticleTypes.BUBBLE, this.getX(), m, this.getZ(),
							(int) (1.0F + this.getWidth() * 20.0F), (double) this.getWidth(), 0.0D,
							(double) this.getWidth(), 0.20000000298023224D);
					serverWorld.spawnParticles(ParticleTypes.FISHING, this.getX(), m, this.getZ(),
							(int) (1.0F + this.getWidth() * 20.0F), (double) this.getWidth(), 0.0D,
							(double) this.getWidth(), 0.20000000298023224D);
					this.hookCountdown = MathHelper.nextInt(this.random, 20, 40);
					this.getDataTracker().set(CAUGHT_FISH, true);
				}
			} else if (this.waitCountdown > 0) {
				this.waitCountdown -= i;
				n = 0.15F;
				if (this.waitCountdown < 20) {
					n = (float) (n + (20 - this.waitCountdown) * 0.05D);
				} else if (this.waitCountdown < 40) {
					n = (float) (n + (40 - this.waitCountdown) * 0.02D);
				} else if (this.waitCountdown < 60) {
					n = (float) (n + (60 - this.waitCountdown) * 0.01D);
				}

				if (this.random.nextFloat() < n) {
					o = MathHelper.nextFloat(this.random, 0.0F, 360.0F) * 0.017453292F;
					p = MathHelper.nextFloat(this.random, 25.0F, 60.0F);
					q = this.getX() + MathHelper.sin(o) * p * 0.1F;
					r = MathHelper.floor(this.getY()) + 1.0F;
					s = this.getZ() + MathHelper.cos(o) * p * 0.1F;
					blockState2 = serverWorld.getBlockState(new BlockPos(q, r - 1.0D, s));
					if (blockState2.isOf(Blocks.WATER)) {
						serverWorld.spawnParticles(ParticleTypes.SPLASH, q, r, s, 2 + this.random.nextInt(2),
								0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
					}
				}

				if (this.waitCountdown <= 0) {
					this.fishAngle = MathHelper.nextFloat(this.random, 0.0F, 360.0F);
					this.fishTravelCountdown = MathHelper.nextInt(this.random, 20, 80);
				}
			} else {
				this.waitCountdown = MathHelper.nextInt(this.random, 100, 600);
				this.waitCountdown -= this.lureLevel * 20 * 5;
			}
		}

	}

	private boolean isOpenOrWaterAround(BlockPos pos) {
		NetheriteFishingBobberEntity.PositionType positionType = NetheriteFishingBobberEntity.PositionType.INVALID;

		for (int i = -1; i <= 2; ++i) {
			NetheriteFishingBobberEntity.PositionType positionType2 = this.getPositionType(pos.add(-2, i, -2),
					pos.add(2, i, 2));
			switch (positionType2) {
				case INVALID:
					return false;
				case ABOVE_WATER:
					if (positionType == NetheriteFishingBobberEntity.PositionType.INVALID) {
						return false;
					}
					break;
				case INSIDE_WATER:
					if (positionType == NetheriteFishingBobberEntity.PositionType.ABOVE_WATER) {
						return false;
					}
			}

			positionType = positionType2;
		}

		return true;
	}

	private NetheriteFishingBobberEntity.PositionType getPositionType(BlockPos start, BlockPos end) {
		return BlockPos.stream(start, end).map(this::getPositionType).reduce((positionType, positionType2) -> {
			return positionType == positionType2 ? positionType : NetheriteFishingBobberEntity.PositionType.INVALID;
		}).orElse(NetheriteFishingBobberEntity.PositionType.INVALID);
	}

	private NetheriteFishingBobberEntity.PositionType getPositionType(BlockPos pos) {
		BlockState blockState = this.world.getBlockState(pos);
		if (!blockState.isAir() && !blockState.isOf(Blocks.LILY_PAD)) {
			FluidState fluidState = blockState.getFluidState();
			return fluidState.isIn(FluidTags.WATER) && fluidState.isStill()
					&& blockState.getCollisionShape(this.world, pos).isEmpty()
							? NetheriteFishingBobberEntity.PositionType.INSIDE_WATER
							: NetheriteFishingBobberEntity.PositionType.INVALID;
		} else {
			return NetheriteFishingBobberEntity.PositionType.ABOVE_WATER;
		}
	}

	public boolean isInOpenWater() {
		return this.inOpenWater;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
	}

	public int use(ItemStack usedItem) {
		PlayerEntity playerEntity = this.getOwner();
		if (!this.world.isClient && playerEntity != null) {
			int i = 0;
			if (this.hookedEntity != null) {
				this.pullHookedEntity();
				Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this,
						Collections.emptyList());
				this.world.sendEntityStatus(this, (byte) 31);
				i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
			} else if (this.hookCountdown > 0) {
				LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world))
						.parameter(LootContextParameters.POSITION, this.getBlockPos())
						.parameter(LootContextParameters.TOOL, usedItem)
						.parameter(LootContextParameters.THIS_ENTITY, this).random(this.random)
						.luck(this.luckOfTheSeaLevel + playerEntity.getLuck());
				LootTable lootTable = this.world.getServer().getLootManager().getTable(LootTables.FISHING_GAMEPLAY);
				List<ItemStack> list = lootTable.generateLoot(builder.build(LootContextTypes.FISHING));
				Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, list);
				Iterator var7 = list.iterator();

				while (var7.hasNext()) {
					ItemStack itemStack = (ItemStack) var7.next();
					ItemEntity itemEntity = new ItemEntity(this.world, this.getX(), this.getY(), this.getZ(),
							itemStack);
					double d = playerEntity.getX() - this.getX();
					double e = playerEntity.getY() - this.getY();
					double f = playerEntity.getZ() - this.getZ();
					double g = 0.1D;
					itemEntity.setVelocity(d * 0.1D, e * 0.1D + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D,
							f * 0.1D);
					this.world.spawnEntity(itemEntity);
					playerEntity.world.spawnEntity(new ExperienceOrbEntity(playerEntity.world, playerEntity.getX(),
							playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, this.random.nextInt(6) + 1));
					if (itemStack.getItem().isIn(ItemTags.FISHES)) {
						playerEntity.increaseStat(Stats.FISH_CAUGHT, 1);
					}
				}

				i = 1;
			}

			if (this.onGround) {
				i = 2;
			}

			this.remove();
			return i;
		} else {
			return 0;
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handleStatus(byte status) {
		if (status == 31 && this.world.isClient && this.hookedEntity instanceof PlayerEntity
				&& ((PlayerEntity) this.hookedEntity).isMainPlayer()) {
			this.pullHookedEntity();
		}

		super.handleStatus(status);
	}

	protected void pullHookedEntity() {
		Entity entity = this.getOwner();
		if (entity != null) {
			Vec3d vec3d = (new Vec3d(entity.getX() - this.getX(), entity.getY() - this.getY(),
					entity.getZ() - this.getZ())).multiply(0.1D);
			this.hookedEntity.setVelocity(this.hookedEntity.getVelocity().add(vec3d));
		}
	}

	@Override
	protected boolean canClimb() {
		return false;
	}

	@Override
	public void remove() {
		super.remove();
		PlayerEntity playerEntity = this.getOwner();
		if (playerEntity != null) {
			playerEntity.fishHook = null;
		}

	}

	@Override
	public PlayerEntity getOwner() {
		Entity entity = this.getOwner();
		return entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
	}

	public Entity getHookedEntity() {
		return this.hookedEntity;
	}

	@Override
	public boolean canUsePortals() {
		return false;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		Entity entity = this.getOwner();
		return new EntitySpawnS2CPacket(this, entity == null ? this.getEntityId() : entity.getEntityId());
	}

	static {
		HOOK_ENTITY_ID = DataTracker.registerData(NetheriteFishingBobberEntity.class,
				TrackedDataHandlerRegistry.INTEGER);
		CAUGHT_FISH = DataTracker.registerData(NetheriteFishingBobberEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	}

	static enum PositionType {
		ABOVE_WATER, INSIDE_WATER, INVALID;
	}

	static enum State {
		FLYING, HOOKED_IN_ENTITY, BOBBING;
	}
}
