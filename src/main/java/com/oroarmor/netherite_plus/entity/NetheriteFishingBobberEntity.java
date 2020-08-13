package com.oroarmor.netherite_plus.entity;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class NetheriteFishingBobberEntity extends FishingBobberEntity {

	@Environment(EnvType.CLIENT)
	public NetheriteFishingBobberEntity(World world, PlayerEntity thrower, double x, double y, double z) {
		super(world, thrower, x, y, z);
	}

	public NetheriteFishingBobberEntity(PlayerEntity thrower, World world, int lureLevel, int luckOfTheSeaLevel) {
		super(thrower, world, lureLevel, luckOfTheSeaLevel);
	}

	@Override
	public void tick() {
		this.velocityRandom.setSeed(this.getUuid().getLeastSignificantBits() ^ this.world.getTime());

		if (!this.leftOwner) {
			this.leftOwner = this.method_26961();
		}

		if (!this.world.isClient) {
			this.setFlag(6, this.isGlowing());
		}

		this.baseTick();

		PlayerEntity playerEntity = this.getPlayerOwner();
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

			float fluidHeight = 0.0F;
			BlockPos blockPos = this.getBlockPos();
			FluidState fluidState = this.world.getFluidState(blockPos);
			if (fluidState.isIn(FluidTags.LAVA)) {
				fluidHeight = fluidState.getHeight(this.world, blockPos);
			}

			boolean validFluid = fluidHeight > 0.0F;

			if (this.state == FishingBobberEntity.State.FLYING) {
				if (this.hookedEntity != null) {
					this.setVelocity(Vec3d.ZERO);
					this.state = FishingBobberEntity.State.HOOKED_IN_ENTITY;
					return;
				}

				if (validFluid) {
					this.setVelocity(this.getVelocity().multiply(0.3D, 0.2D, 0.3D));
					this.state = FishingBobberEntity.State.BOBBING;
					return;
				}

				this.checkForCollision();
			} else {
				if (this.state == FishingBobberEntity.State.HOOKED_IN_ENTITY) {
					if (this.hookedEntity != null) {
						if (this.hookedEntity.removed) {
							this.hookedEntity = null;
							this.state = FishingBobberEntity.State.FLYING;
						} else {
							this.updatePosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D),
									this.hookedEntity.getZ());
						}
					}

					return;
				}

				if (this.state == FishingBobberEntity.State.BOBBING) {
					Vec3d velocity = this.getVelocity();
					double d = this.getY() - blockPos.getY() + 0.01 * velocity.y - fluidHeight;
					if (Math.abs(d) < 0.01D) {
						d += Math.signum(d) * 0.1D;
					}

					this.setVelocity(velocity.x * 0.9D, velocity.y - d * this.random.nextFloat() * 0.4D,
							velocity.z * 0.9D);
					if (this.hookCountdown <= 0 && this.fishTravelCountdown <= 0) {
						this.inOpenWater = true;
					} else {
						this.inOpenWater = this.inOpenWater && this.outOfOpenWaterTicks < 10
								&& this.isOpenOrLavaAround(blockPos);
					}

					if (validFluid) {
						this.outOfOpenWaterTicks = Math.max(0, this.outOfOpenWaterTicks - 1);
						if (this.caughtFish) {
							this.setVelocity(this.getVelocity().add(0.0D,
									-0.1D * this.velocityRandom.nextFloat() * this.velocityRandom.nextFloat(), 0.0D));
						}

						if (!world.isClient) {
							this.tickFishingLogic();
						}

					} else {
						this.outOfOpenWaterTicks = Math.min(10, this.outOfOpenWaterTicks + 1);
					}
				}
			}

			if (!fluidState.isIn(FluidTags.LAVA)) {
				this.setVelocity(this.getVelocity().add(0.0D, -0.06D, 0.0D));
			}

			this.move(MovementType.SELF, this.getVelocity());
			this.method_26962();
			if (this.state == FishingBobberEntity.State.FLYING && (this.onGround || this.horizontalCollision)) {
				this.setVelocity(Vec3d.ZERO);
			}

			double e = 0.92D;
			this.setVelocity(this.getVelocity().multiply(e));
			System.out.println(this.getVelocity().y);
			this.refreshPosition();
		}
	}

	private void tickFishingLogic() {
		ServerWorld serverWorld = (ServerWorld) this.world;
		int i = 1;

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
					if (blockState2.isOf(Blocks.LAVA)) {
						if (this.random.nextFloat() < 0.15F) {
							serverWorld.spawnParticles(ParticleTypes.LANDING_LAVA, q, r - 0.10000000149011612D, s, 1,
									(double) o, 0.1D, (double) p, 0.0D);
						}

						float k = o * 0.04F;
						float l = p * 0.04F;
						serverWorld.spawnParticles(ParticleTypes.FLAME, q, r, s, 0, (double) l, 0.01D, (double) (-k),
								1.0D);
						serverWorld.spawnParticles(ParticleTypes.FLAME, q, r, s, 0, (double) (-l), 0.01D, (double) k,
								1.0D);
					}
				} else {
					this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F,
							1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
					double m = this.getY() + 0.5D;
					serverWorld.spawnParticles(ParticleTypes.LANDING_LAVA, this.getX(), m, this.getZ(),
							(int) (1.0F + this.getWidth() * 20.0F), (double) this.getWidth(), 0.0D,
							(double) this.getWidth(), 0.20000000298023224D);
					serverWorld.spawnParticles(ParticleTypes.FLAME, this.getX(), m, this.getZ(),
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
					if (blockState2.isOf(Blocks.LAVA)) {
						serverWorld.spawnParticles(ParticleTypes.SMOKE, q, r, s, 2 + this.random.nextInt(2),
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

	private boolean removeIfInvalid(PlayerEntity playerEntity) {
		ItemStack itemStack = playerEntity.getMainHandStack();
		ItemStack itemStack2 = playerEntity.getOffHandStack();
		boolean bl = itemStack.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD;
		boolean bl2 = itemStack2.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD;
		if (!playerEntity.removed && playerEntity.isAlive() && (bl || bl2)
				&& this.squaredDistanceTo(playerEntity) <= 1024.0D) {
			return false;
		} else {
			remove();
			return true;
		}
	}

	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getCollision(this, this::method_26958);
		onCollision(hitResult);
	}

	private boolean isOpenOrLavaAround(BlockPos pos) {
		FishingBobberEntity.PositionType positionType = FishingBobberEntity.PositionType.INVALID;

		for (int i = -1; i <= 2; ++i) {
			FishingBobberEntity.PositionType positionType2 = this.getPositionType(pos.add(-2, i, -2), pos.add(2, i, 2));
			switch (positionType2) {
				case INVALID:
					return false;
				case ABOVE_WATER:
					if (positionType == FishingBobberEntity.PositionType.INVALID) {
						return false;
					}
					break;
				case INSIDE_WATER:
					if (positionType == FishingBobberEntity.PositionType.ABOVE_WATER) {
						return false;
					}
			}

			positionType = positionType2;
		}

		return true;
	}

	private FishingBobberEntity.PositionType getPositionType(BlockPos start, BlockPos end) {
		return BlockPos.stream(start, end).map(this::getPositionType).reduce((positionType, positionType2) -> {
			return positionType == positionType2 ? positionType : FishingBobberEntity.PositionType.INVALID;
		}).orElse(FishingBobberEntity.PositionType.INVALID);
	}

	private FishingBobberEntity.PositionType getPositionType(BlockPos pos) {
		BlockState blockState = this.world.getBlockState(pos);
		if (!blockState.isAir()) {
			FluidState fluidState = blockState.getFluidState();
			return fluidState.isIn(FluidTags.LAVA) && fluidState.isStill()
					&& blockState.getCollisionShape(this.world, pos).isEmpty()
							? FishingBobberEntity.PositionType.INSIDE_WATER
							: FishingBobberEntity.PositionType.INVALID;
		} else {
			return FishingBobberEntity.PositionType.ABOVE_WATER;
		}
	}

	@Override
	public int use(ItemStack usedItem) {
		PlayerEntity playerEntity = getPlayerOwner();
		if (!world.isClient && playerEntity != null) {
			int i = 0;
			if (hookedEntity != null) {
				pullHookedEntity();
				world.sendEntityStatus(this, (byte) 31);
				i = hookedEntity instanceof ItemEntity ? 3 : 5;
			} else if (hookCountdown > 0) {
				LootContext.Builder builder = (new LootContext.Builder((ServerWorld) this.world))
						.parameter(LootContextParameters.ORIGIN, this.getPos())
						.parameter(LootContextParameters.TOOL, usedItem)
						.parameter(LootContextParameters.THIS_ENTITY, this).random(this.random)
						.luck(this.luckOfTheSeaLevel + playerEntity.getLuck());
				LootTable lootTable = world.getServer().getLootManager()
						.getTable(new Identifier("netherite_plus", "gameplay/fishing"));
				List<ItemStack> list = lootTable.generateLoot(builder.build(LootContextTypes.FISHING));
				Iterator<ItemStack> var7 = list.iterator();

				while (var7.hasNext()) {
					ItemStack itemStack = var7.next();
					ItemEntity itemEntity = new ItemEntity(world, getX(), getY(), getZ(), itemStack);
					double d = playerEntity.getX() - getX();
					double e = playerEntity.getY() - getY();
					double f = playerEntity.getZ() - getZ();
					double g = 0.1D;
					itemEntity.setVelocity(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
					itemEntity.setInvulnerable(true);
					world.spawnEntity(itemEntity);
					playerEntity.world.spawnEntity(new ExperienceOrbEntity(playerEntity.world, playerEntity.getX(),
							playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, random.nextInt(6) + 1));
				}

				i = 1;
			}

			if (onGround) {
				i = 2;
			}

			remove();
			return i;
		} else {
			return 0;
		}
	}

	@Nullable
	public PlayerEntity getPlayerOwner() {
		Entity entity = this.getEntityOwner();
		return entity instanceof PlayerEntity ? (PlayerEntity) entity : null;
	}

	@Nullable
	public Entity getEntityOwner() {
		if (this.ownerUuid != null && this.world instanceof ServerWorld) {
			return ((ServerWorld) this.world).getEntity(this.ownerUuid);
		} else {
			return this.ownerEntityId != 0 ? this.world.getEntityById(this.ownerEntityId) : null;
		}
	}

	@Override
	public PlayerEntity getOwner() {
		return getPlayerOwner();
	}

	@Override
	public boolean isOnFire() {
		return false;
	}

	@Override
	public boolean doesRenderOnFire() {
		return false;
	}

	@Override
	public boolean isFireImmune() {
		return true;
	}

}
