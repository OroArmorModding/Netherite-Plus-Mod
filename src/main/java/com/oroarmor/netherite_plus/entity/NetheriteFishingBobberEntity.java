/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.entity;

import java.util.List;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.oroarmor.netherite_plus.loot.NetheritePlusLootManager.LAVA_FISHING_LOOT_TABLE;

public class NetheriteFishingBobberEntity extends FishingBobberEntity {
    public NetheriteFishingBobberEntity(PlayerEntity thrower, World world, int lureLevel, int luckOfTheSeaLevel) {
        super(thrower, world, lureLevel, luckOfTheSeaLevel);
    }

    private void checkForCollision() {
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        onCollision(hitResult);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    private FishingBobberEntity.PositionType getPositionType(BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        if (!blockState.isAir()) {
            FluidState fluidState = blockState.getFluidState();
            return fluidState.isIn(FluidTags.LAVA) && fluidState.isSource() && blockState.getCollisionShape(world, pos).isEmpty() ? FishingBobberEntity.PositionType.INSIDE_WATER : FishingBobberEntity.PositionType.INVALID;
        } else {
            return FishingBobberEntity.PositionType.ABOVE_WATER;
        }
    }

    private FishingBobberEntity.PositionType getPositionType(BlockPos start, BlockPos end) {
        return BlockPos.stream(start, end).map(this::getPositionType).reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : PositionType.INVALID).orElse(FishingBobberEntity.PositionType.INVALID);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
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

    private boolean removeIfInvalid(PlayerEntity playerEntity) {
        ItemStack itemStack = playerEntity.getMainHandStack();
        ItemStack itemStack2 = playerEntity.getOffHandStack();
        boolean bl = itemStack.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD;
        boolean bl2 = itemStack2.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD;
        if (!playerEntity.isRemoved() && playerEntity.isAlive() && (bl || bl2) && this.squaredDistanceTo(playerEntity) <= 1024.0D) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    public void tick() {
        BlockPos blockPos = getBlockPos();
        FluidState fluidState = world.getFluidState(blockPos);
        if (fluidState.isIn(FluidTags.WATER)) {
            super.tick();
            return;
        }


        velocityRandom.setSeed(getUuid().getLeastSignificantBits() ^ world.getTime());
        baseTick();

        PlayerEntity playerEntity = getPlayerOwner();
        if (playerEntity == null) {
            this.discard();
        } else if (world.isClient || !removeIfInvalid(playerEntity)) {
            if (onGround) {
                ++removalTimer;
                if (removalTimer >= 1200) {
                    this.discard();
                    return;
                }
            } else {
                removalTimer = 0;
            }

            float fluidHeight = 0.0F;
            if (fluidState.isIn(FluidTags.LAVA)) {
                fluidHeight = fluidState.getHeight(world, blockPos);
            }

            boolean validFluid = fluidHeight > 0.0F;

            if (state == FishingBobberEntity.State.FLYING) {
                if (hookedEntity != null) {
                    this.setVelocity(Vec3d.ZERO);
                    state = FishingBobberEntity.State.HOOKED_IN_ENTITY;
                    return;
                }

                if (validFluid) {
                    this.setVelocity(getVelocity().multiply(0.3D, 0.2D, 0.3D));
                    state = FishingBobberEntity.State.BOBBING;
                    return;
                }

                checkForCollision();
            } else {
                if (state == FishingBobberEntity.State.HOOKED_IN_ENTITY) {
                    if (hookedEntity != null) {
                        if (hookedEntity.isRemoved() && this.hookedEntity.world.getRegistryKey() == this.world.getRegistryKey()) {
                            hookedEntity = null;
                            state = FishingBobberEntity.State.FLYING;
                        } else {
                            updatePosition(hookedEntity.getX(), hookedEntity.getBodyY(0.8D), hookedEntity.getZ());
                        }
                    }

                    return;
                }

                if (state == FishingBobberEntity.State.BOBBING) {
                    Vec3d velocity = getVelocity();
                    double d = getY() - blockPos.getY() + 0.01 * velocity.y - fluidHeight;
                    if (Math.abs(d) < 0.01D) {
                        d += Math.signum(d) * 0.1D;
                    }

                    this.setVelocity(velocity.x * 0.9D, velocity.y - d * random.nextFloat() * 0.4D, velocity.z * 0.9D);
                    if (hookCountdown <= 0 && fishTravelCountdown <= 0) {
                        inOpenWater = true;
                    } else {
                        inOpenWater = inOpenWater && outOfOpenWaterTicks < 10 && isOpenOrLavaAround(blockPos);
                    }

                    if (validFluid) {
                        outOfOpenWaterTicks = Math.max(0, outOfOpenWaterTicks - 1);
                        if (caughtFish) {
                            this.setVelocity(getVelocity().add(0.0D, -0.1D * velocityRandom.nextFloat() * velocityRandom.nextFloat(), 0.0D));
                        }

                        if (!world.isClient) {
                            tickFishingLogic();
                        }

                    } else {
                        outOfOpenWaterTicks = Math.min(10, outOfOpenWaterTicks + 1);
                    }
                }
            }

            if (!fluidState.isIn(FluidTags.LAVA)) {
                this.setVelocity(getVelocity().add(0.0D, -0.06D, 0.0D));
            }

            move(MovementType.SELF, getVelocity());
            updateRotation();
            if (state == FishingBobberEntity.State.FLYING && (onGround || horizontalCollision)) {
                this.setVelocity(Vec3d.ZERO);
            }

            double e = 0.92D;
            this.setVelocity(getVelocity().multiply(e));
            refreshPosition();
        }
    }

    private void tickFishingLogic() {
        ServerWorld serverWorld = (ServerWorld) world;
        int i = 1;

        if (hookCountdown > 0) {
            --hookCountdown;
            if (hookCountdown <= 0) {
                waitCountdown = 0;
                fishTravelCountdown = 0;
                getDataTracker().set(CAUGHT_FISH, false);
            }
        } else {
            float n;
            float o;
            float p;
            double q;
            double r;
            double s;
            BlockState blockState2;
            if (fishTravelCountdown > 0) {
                fishTravelCountdown -= i;
                if (fishTravelCountdown > 0) {
                    fishAngle = (float) (fishAngle + random.nextGaussian() * 4.0D);
                    n = fishAngle * 0.017453292F;
                    o = MathHelper.sin(n);
                    p = MathHelper.cos(n);
                    q = getX() + o * fishTravelCountdown * 0.1F;
                    r = MathHelper.floor(getY()) + 1.0F;
                    s = getZ() + p * fishTravelCountdown * 0.1F;
                    blockState2 = serverWorld.getBlockState(new BlockPos(q, r - 1.0D, s));
                    if (blockState2.isOf(Blocks.LAVA)) {
                        if (random.nextFloat() < 0.15F) {
                            serverWorld.spawnParticles(ParticleTypes.LAVA_SPLASH, q, r - 0.10000000149011612D, s, 1, o, 0.1D, p, 0.0D);
                        }

                        float k = o * 0.04F;
                        float l = p * 0.04F;
                        serverWorld.spawnParticles(ParticleTypes.FLAME, q, r, s, 0, l, 0.01D, -k, 1.0D);
                        serverWorld.spawnParticles(ParticleTypes.FLAME, q, r, s, 0, -l, 0.01D, k, 1.0D);
                    }
                } else {
                    playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
                    double m = getY() + 0.5D;
                    serverWorld.spawnParticles(ParticleTypes.LAVA_SPLASH, getX(), m, getZ(), (int) (1.0F + getWidth() * 20.0F), getWidth(), 0.0D, getWidth(), 0.20000000298023224D);
                    serverWorld.spawnParticles(ParticleTypes.FLAME, getX(), m, getZ(), (int) (1.0F + getWidth() * 20.0F), getWidth(), 0.0D, getWidth(), 0.20000000298023224D);
                    hookCountdown = MathHelper.nextInt(random, 20, 40);
                    getDataTracker().set(CAUGHT_FISH, true);
                }
            } else if (waitCountdown > 0) {
                waitCountdown -= i;
                n = 0.15F;
                if (waitCountdown < 20) {
                    n = (float) (n + (20 - waitCountdown) * 0.05D);
                } else if (waitCountdown < 40) {
                    n = (float) (n + (40 - waitCountdown) * 0.02D);
                } else if (waitCountdown < 60) {
                    n = (float) (n + (60 - waitCountdown) * 0.01D);
                }

                if (random.nextFloat() < n) {
                    o = MathHelper.nextFloat(random, 0.0F, 360.0F) * 0.017453292F;
                    p = MathHelper.nextFloat(random, 25.0F, 60.0F);
                    q = getX() + MathHelper.sin(o) * p * 0.1F;
                    r = MathHelper.floor(getY()) + 1.0F;
                    s = getZ() + MathHelper.cos(o) * p * 0.1F;
                    blockState2 = serverWorld.getBlockState(new BlockPos(q, r - 1.0D, s));
                    if (blockState2.isOf(Blocks.LAVA)) {
                        serverWorld.spawnParticles(ParticleTypes.SMOKE, q, r, s, 2 + random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                    }
                }

                if (waitCountdown <= 0) {
                    fishAngle = MathHelper.nextFloat(random, 0.0F, 360.0F);
                    fishTravelCountdown = MathHelper.nextInt(random, 20, 80);
                }
            } else {
                waitCountdown = MathHelper.nextInt(random, 100, 600);
                waitCountdown -= lureLevel * 20 * 5;
            }
        }

    }

    @Override
    public int use(ItemStack usedItem) {
        BlockPos blockPos = getBlockPos();
        FluidState fluidState = world.getFluidState(blockPos);
        if (fluidState.isIn(FluidTags.WATER)) {
            return super.use(usedItem);
        }

        PlayerEntity playerEntity = getPlayerOwner();
        if (!world.isClient && playerEntity != null) {
            int i = 0;
            if (hookedEntity != null) {
                pullHookedEntity(hookedEntity);
                world.sendEntityStatus(this, (byte) 31);
                i = hookedEntity instanceof ItemEntity ? 3 : 5;
            } else if (hookCountdown > 0) {
                LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.ORIGIN, getPos()).parameter(LootContextParameters.TOOL, usedItem).parameter(LootContextParameters.THIS_ENTITY, this).random(random).luck(luckOfTheSeaLevel + playerEntity.getLuck());
                LootTable lootTable = world.getServer().getLootManager().getTable(LAVA_FISHING_LOOT_TABLE);
                List<ItemStack> list = lootTable.generateLoot(builder.build(LootContextTypes.FISHING));
                Criteria.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity) playerEntity, usedItem, this, list);

                for (ItemStack itemStack : list) {
                    ItemEntity itemEntity = new ItemEntity(world, getX(), getY(), getZ(), itemStack);
                    double d = playerEntity.getX() - getX();
                    double e = playerEntity.getY() - getY();
                    double f = playerEntity.getZ() - getZ();
                    double g = 0.1D;
                    itemEntity.setVelocity(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
                    itemEntity.setInvulnerable(true);
                    world.spawnEntity(itemEntity);
                    playerEntity.world.spawnEntity(new ExperienceOrbEntity(playerEntity.world, playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, random.nextInt(6) + 1));
                }

                i = 1;
            }

            if (onGround) {
                i = 2;
            }

            this.discard();
            return i;
        } else {
            return 0;
        }
    }
}
