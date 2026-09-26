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

package com.oroarmor.netherite_plus.entity;

import static com.oroarmor.netherite_plus.loot.NetheritePlusLootManager.LAVA_FISHING_LOOT_TABLE;

import java.util.List;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NetheriteFishingBobberEntity extends FishingHook {
    public NetheriteFishingBobberEntity(Player thrower, Level world, int lureLevel, int luckOfTheSeaLevel) {
        super(thrower, world, lureLevel, luckOfTheSeaLevel);
    }

    private void checkCollision() {
        HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        onHit(hitResult);
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    private FishingHook.OpenWaterType getOpenWaterTypeForBlock(BlockPos pos) {
        BlockState blockState = this.level().getBlockState(pos);
        if (!blockState.isAir()) {
            FluidState fluidState = blockState.getFluidState();
            return fluidState.is(FluidTags.LAVA) && fluidState.isSource() && blockState.getCollisionShape(this.level(), pos).isEmpty() ? FishingHook.OpenWaterType.INSIDE_WATER : FishingHook.OpenWaterType.INVALID;
        } else {
            return FishingHook.OpenWaterType.ABOVE_WATER;
        }
    }

    private FishingHook.OpenWaterType getOpenWaterTypeForArea(BlockPos start, BlockPos end) {
        return BlockPos.betweenClosedStream(start, end).map(this::getOpenWaterTypeForBlock).reduce((positionType, positionType2) -> positionType == positionType2 ? positionType : OpenWaterType.INVALID).orElse(FishingHook.OpenWaterType.INVALID);
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    private boolean isOpenOrLavaAround(BlockPos pos) {
        FishingHook.OpenWaterType positionType = FishingHook.OpenWaterType.INVALID;

        for (int i = -1; i <= 2; ++i) {
            FishingHook.OpenWaterType positionType2 = this.getOpenWaterTypeForArea(pos.offset(-2, i, -2), pos.offset(2, i, 2));
            switch (positionType2) {
                case INVALID:
                    return false;
                case ABOVE_WATER:
                    if (positionType == FishingHook.OpenWaterType.INVALID) {
                        return false;
                    }
                    break;
                case INSIDE_WATER:
                    if (positionType == FishingHook.OpenWaterType.ABOVE_WATER) {
                        return false;
                    }
            }

            positionType = positionType2;
        }

        return true;
    }

    private boolean shouldStopFishing(Player playerEntity) {
        ItemStack itemStack = playerEntity.getMainHandItem();
        ItemStack itemStack2 = playerEntity.getOffhandItem();
        boolean bl = itemStack.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD;
        boolean bl2 = itemStack2.getItem() == NetheritePlusItems.NETHERITE_FISHING_ROD;
        if (!playerEntity.isRemoved() && playerEntity.isAlive() && (bl || bl2) && this.distanceToSqr(playerEntity) <= 1024.0D) {
            return false;
        } else {
            this.discard();
            return true;
        }
    }

    @Override
    public void tick() {
        BlockPos blockPos = blockPosition();
        FluidState fluidState = this.level().getFluidState(blockPos);
        if (fluidState.is(FluidTags.WATER)) {
            super.tick();
            return;
        }


        syncronizedRandom.setSeed(getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
        baseTick();

        Player playerEntity = getPlayerOwner();
        if (playerEntity == null) {
            this.discard();
        } else if (this.level().isClientSide || !shouldStopFishing(playerEntity)) {
            if (this.onGround()) {
                ++life;
                if (life >= 1200) {
                    this.discard();
                    return;
                }
            } else {
                life = 0;
            }

            float fluidHeight = 0.0F;
            if (fluidState.is(FluidTags.LAVA)) {
                fluidHeight = fluidState.getHeight(this.level(), blockPos);
            }

            boolean validFluid = fluidHeight > 0.0F;

            if (currentState == FishingHook.FishHookState.FLYING) {
                if (hookedIn != null) {
                    this.setDeltaMovement(Vec3.ZERO);
                    currentState = FishingHook.FishHookState.HOOKED_IN_ENTITY;
                    return;
                }

                if (validFluid) {
                    this.setDeltaMovement(getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
                    currentState = FishingHook.FishHookState.BOBBING;
                    return;
                }

                checkCollision();
            } else {
                if (currentState == FishingHook.FishHookState.HOOKED_IN_ENTITY) {
                    if (hookedIn != null) {
                        if (hookedIn.isRemoved() && this.hookedIn.level().dimension() == this.level().dimension()) {
                            hookedIn = null;
                            currentState = FishingHook.FishHookState.FLYING;
                        } else {
                            absMoveTo(hookedIn.getX(), hookedIn.getY(0.8D), hookedIn.getZ());
                        }
                    }

                    return;
                }

                if (currentState == FishingHook.FishHookState.BOBBING) {
                    Vec3 velocity = getDeltaMovement();
                    double d = getY() - blockPos.getY() + 0.01 * velocity.y - fluidHeight;
                    if (Math.abs(d) < 0.01D) {
                        d += Math.signum(d) * 0.1D;
                    }

                    this.setDeltaMovement(velocity.x * 0.9D, velocity.y - d * random.nextFloat() * 0.4D, velocity.z * 0.9D);
                    if (nibble <= 0 && timeUntilHooked <= 0) {
                        openWater = true;
                    } else {
                        openWater = openWater && outOfWaterTime < 10 && isOpenOrLavaAround(blockPos);
                    }

                    if (validFluid) {
                        outOfWaterTime = Math.max(0, outOfWaterTime - 1);
                        if (biting) {
                            this.setDeltaMovement(getDeltaMovement().add(0.0D, -0.1D * syncronizedRandom.nextFloat() * syncronizedRandom.nextFloat(), 0.0D));
                        }

                        if (!this.level().isClientSide) {
                            tickFishingLogic();
                        }

                    } else {
                        outOfWaterTime = Math.min(10, outOfWaterTime + 1);
                    }
                }
            }

            if (!fluidState.is(FluidTags.LAVA)) {
                this.setDeltaMovement(getDeltaMovement().add(0.0D, -0.06D, 0.0D));
            }

            move(MoverType.SELF, getDeltaMovement());
            updateRotation();
            if (currentState == FishingHook.FishHookState.FLYING && (this.onGround() || horizontalCollision)) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            double e = 0.92D;
            this.setDeltaMovement(getDeltaMovement().scale(e));
            reapplyPosition();
        }
    }

    private void tickFishingLogic() {
        ServerLevel serverWorld = (ServerLevel) this.level();
        int i = 1;

        if (nibble > 0) {
            --nibble;
            if (nibble <= 0) {
                timeUntilLured = 0;
                timeUntilHooked = 0;
                getEntityData().set(DATA_BITING, false);
            }
        } else {
            float n;
            float o;
            float p;
            double q;
            double r;
            double s;
            BlockState blockState2;
            if (timeUntilHooked > 0) {
                timeUntilHooked -= i;
                if (timeUntilHooked > 0) {
                    fishAngle = (float) (fishAngle + random.nextGaussian() * 4.0D);
                    n = fishAngle * 0.017453292F;
                    o = Mth.sin(n);
                    p = Mth.cos(n);
                    q = getX() + o * timeUntilHooked * 0.1F;
                    r = Mth.floor(getY()) + 1.0F;
                    s = getZ() + p * timeUntilHooked * 0.1F;
                    blockState2 = serverWorld.getBlockState(BlockPos.containing(q, r - 1.0D, s));
                    if (blockState2.is(Blocks.LAVA)) {
                        if (random.nextFloat() < 0.15F) {
                            serverWorld.sendParticles(ParticleTypes.LANDING_LAVA, q, r - 0.10000000149011612D, s, 1, o, 0.1D, p, 0.0D);
                        }

                        float k = o * 0.04F;
                        float l = p * 0.04F;
                        serverWorld.sendParticles(ParticleTypes.FLAME, q, r, s, 0, l, 0.01D, -k, 1.0D);
                        serverWorld.sendParticles(ParticleTypes.FLAME, q, r, s, 0, -l, 0.01D, k, 1.0D);
                    }
                } else {
                    playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (random.nextFloat() - random.nextFloat()) * 0.4F);
                    double m = getY() + 0.5D;
                    serverWorld.sendParticles(ParticleTypes.LANDING_LAVA, getX(), m, getZ(), (int) (1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
                    serverWorld.sendParticles(ParticleTypes.FLAME, getX(), m, getZ(), (int) (1.0F + getBbWidth() * 20.0F), getBbWidth(), 0.0D, getBbWidth(), 0.20000000298023224D);
                    nibble = Mth.nextInt(random, 20, 40);
                    getEntityData().set(DATA_BITING, true);
                }
            } else if (timeUntilLured > 0) {
                timeUntilLured -= i;
                n = 0.15F;
                if (timeUntilLured < 20) {
                    n = (float) (n + (20 - timeUntilLured) * 0.05D);
                } else if (timeUntilLured < 40) {
                    n = (float) (n + (40 - timeUntilLured) * 0.02D);
                } else if (timeUntilLured < 60) {
                    n = (float) (n + (60 - timeUntilLured) * 0.01D);
                }

                if (random.nextFloat() < n) {
                    o = Mth.nextFloat(random, 0.0F, 360.0F) * 0.017453292F;
                    p = Mth.nextFloat(random, 25.0F, 60.0F);
                    q = getX() + Mth.sin(o) * p * 0.1F;
                    r = Mth.floor(getY()) + 1.0F;
                    s = getZ() + Mth.cos(o) * p * 0.1F;
                    blockState2 = serverWorld.getBlockState(BlockPos.containing(q, r - 1.0D, s));
                    if (blockState2.is(Blocks.LAVA)) {
                        serverWorld.sendParticles(ParticleTypes.SMOKE, q, r, s, 2 + random.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                    }
                }

                if (timeUntilLured <= 0) {
                    fishAngle = Mth.nextFloat(random, 0.0F, 360.0F);
                    timeUntilHooked = Mth.nextInt(random, 20, 80);
                }
            } else {
                timeUntilLured = Mth.nextInt(random, 100, 600);
                timeUntilLured -= lureSpeed * 20 * 5;
            }
        }

    }

    @Override
    public int retrieve(ItemStack usedItem) {
        BlockPos blockPos = blockPosition();
        FluidState fluidState = this.level().getFluidState(blockPos);
        if (fluidState.is(FluidTags.WATER)) {
            return super.retrieve(usedItem);
        }

        Player playerEntity = getPlayerOwner();
        if (!this.level().isClientSide && playerEntity != null) {
            int i = 0;
            if (hookedIn != null) {
                pullEntity(hookedIn);
                this.level().broadcastEntityEvent(this, (byte) 31);
                i = hookedIn instanceof ItemEntity ? 3 : 5;
            } else if (nibble > 0) {
                LootParams lootContextParameterSet = new LootParams.Builder((ServerLevel) this.level())
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.TOOL, usedItem)
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .withLuck((float) this.luck + playerEntity.getLuck())
                        .create(LootContextParamSets.FISHING);
                LootTable lootTable = this.level().getServer().getLootData().getLootTable(LAVA_FISHING_LOOT_TABLE);
                List<ItemStack> list = lootTable.getRandomItems(lootContextParameterSet);
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayer) playerEntity, usedItem, this, list);

                for (ItemStack itemStack : list) {
                    ItemEntity itemEntity = new ItemEntity(this.level(), getX(), getY(), getZ(), itemStack);
                    double d = playerEntity.getX() - getX();
                    double e = playerEntity.getY() - getY();
                    double f = playerEntity.getZ() - getZ();
                    double g = 0.1D;
                    itemEntity.setDeltaMovement(d * g, e * g + Math.sqrt(Math.sqrt(d * d + e * e + f * f)) * 0.08D, f * g);
                    itemEntity.setInvulnerable(true);
                    this.level().addFreshEntity(itemEntity);
                    playerEntity.level().addFreshEntity(new ExperienceOrb(playerEntity.level(), playerEntity.getX(), playerEntity.getY() + 0.5D, playerEntity.getZ() + 0.5D, random.nextInt(6) + 1));
                }

                i = 1;
            }

            if (this.onGround()) {
                i = 2;
            }

            this.discard();
            return i;
        } else {
            return 0;
        }
    }
}
