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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Tuple;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

public class NetheriteBeaconBlockEntity extends BlockEntity implements MenuProvider {
    public static final MobEffect[][] EFFECTS_BY_LEVEL = new MobEffect[][]{{MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED}, {MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP}, {MobEffects.DAMAGE_BOOST}, {MobEffects.REGENERATION}, {MobEffects.GLOWING}};
    private static final Set<MobEffect> EFFECTS = Arrays.stream(EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());

    private final ContainerData propertyDelegate = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> beaconLevel;
                case 1 -> MobEffect.getId(primary);
                case 2 -> MobEffect.getId(secondary);
                case 3 -> MobEffect.getId(tertiary);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case 0:
                    beaconLevel = value;
                    break;
                case 1:
                    if (!NetheriteBeaconBlockEntity.this.level.isClientSide && !beamSegments.isEmpty()) {
                        playSound(NetheriteBeaconBlockEntity.this.level, NetheriteBeaconBlockEntity.this.worldPosition, SoundEvents.BEACON_POWER_SELECT);
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
        public int getCount() {
            return 4;
        }
    };
    private List<BeamSegment> beamSegments = Lists.newArrayList();
    private List<BeamSegment> beamSegmentsToCheck = Lists.newArrayList();
    private int beaconLevel;
    private int netheriteLevel;
    private int minY = -1;
    @Nullable
    private MobEffect primary;
    @Nullable
    private MobEffect secondary;
    @Nullable
    private MobEffect tertiary;
    @Nullable
    private Component customName;
    private LockCode lock = LockCode.NO_LOCK;

    public NetheriteBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY, blockPos, blockState);
    }

    @Nullable
    private static MobEffect getPotionEffectById(int id) {
        MobEffect statusEffect = MobEffect.byId(id);
        return EFFECTS.contains(statusEffect) ? statusEffect : null;
    }

    public static void tick(Level world, BlockPos pos, BlockState state, NetheriteBeaconBlockEntity blockEntity) {
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPos blockPos2;
        if (blockEntity.minY < j) {
            blockPos2 = pos;
            blockEntity.beamSegmentsToCheck = Lists.newArrayList();
            blockEntity.minY = blockPos2.getY() - 1;
        } else {
            blockPos2 = new BlockPos(i, blockEntity.minY + 1, k);
        }

        BeamSegment beamSegment = blockEntity.beamSegmentsToCheck.isEmpty() ? null : blockEntity.beamSegmentsToCheck.get(blockEntity.beamSegmentsToCheck.size() - 1);
        int l = world.getHeight(Heightmap.Types.WORLD_SURFACE, i, k);

        int n;
        for (n = 0; n < 10 && blockPos2.getY() <= l; ++n) {
            BlockState blockState = world.getBlockState(blockPos2);
            Block block = blockState.getBlock();
            if (block instanceof BeaconBeamBlock) {
                float[] fs = ((BeaconBeamBlock) block).getColor().getTextureDiffuseColors();
                if (blockEntity.beamSegmentsToCheck.size() <= 1) {
                    beamSegment = new BeamSegment(fs);
                    blockEntity.beamSegmentsToCheck.add(beamSegment);
                } else if (beamSegment != null) {
                    if (Arrays.equals(fs, beamSegment.color)) {
                        beamSegment.increaseHeight();
                    } else {
                        beamSegment = new BeamSegment(new float[]{(beamSegment.color[0] + fs[0]) / 2.0F, (beamSegment.color[1] + fs[1]) / 2.0F, (beamSegment.color[2] + fs[2]) / 2.0F});
                        blockEntity.beamSegmentsToCheck.add(beamSegment);
                    }
                }
            } else {
                if (beamSegment == null || blockState.getLightBlock(world, blockPos2) >= 15 && block != Blocks.BEDROCK) {
                    blockEntity.beamSegmentsToCheck.clear();
                    blockEntity.minY = l;
                    break;
                }

                beamSegment.increaseHeight();
            }

            blockPos2 = blockPos2.above();
            ++blockEntity.minY;
        }

        n = blockEntity.beaconLevel;
        if (world.getGameTime() % 80L == 0L) {
            if (!blockEntity.beamSegments.isEmpty()) {
                Tuple<Integer, Integer> levels = updateLevel(world, i, j, k);
                blockEntity.beaconLevel = levels.getA();
                blockEntity.netheriteLevel = levels.getB();
                if (blockEntity.netheriteLevel == 164) {
                    List<ServerPlayer> var14 = world.getEntitiesOfClass(ServerPlayer.class, new AABB(i, j, k, i, j - 4, k).inflate(10.0D, 5.0D, 10.0D));

                    for (ServerPlayer serverPlayerEntity : var14) {
                        NetheritePlusCriteria.FULL_NETHERITE_NETHERITE_BEACON.trigger(serverPlayerEntity, blockEntity);
                    }

                }

                if (blockEntity.beaconLevel == 4) {
                    List<ServerPlayer> var14 = world.getEntitiesOfClass(ServerPlayer.class, new AABB(i, j, k, i, j - 4, k).inflate(10.0D, 5.0D, 10.0D));

                    for (ServerPlayer serverPlayerEntity : var14) {
                        NetheritePlusCriteria.CONSTRUCT_NETHERITE_BEACON.trigger(serverPlayerEntity, blockEntity);
                    }
                }
            }

            if (blockEntity.beaconLevel > 0 && !blockEntity.beamSegments.isEmpty()) {
                blockEntity.applyPlayerEffects();
                playSound(world, pos, SoundEvents.BEACON_AMBIENT);
            }
        }

        if (blockEntity.minY >= l) {
            blockEntity.minY = -1;
            boolean bl = n > 0;
            blockEntity.beamSegments = blockEntity.beamSegmentsToCheck;
            if (!world.isClientSide) {
                boolean bl2 = blockEntity.beaconLevel > 0;
                if (!bl && bl2) {
                    playSound(world, pos, SoundEvents.BEACON_ACTIVATE);
                } else if (bl && !bl2) {
                    playSound(world, pos, SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }

        world.setBlock(pos, state.setValue(BlockStateProperties.POWERED, blockEntity.beaconLevel > 0), 2);
    }

    private static Tuple<Integer, Integer> updateLevel(Level world, int x, int y, int z) {
        int beaconLevel = 0;
        int netheriteLevel = 0;

        for (int i = 1; i <= 4; beaconLevel = i++) {
            int j = y - i;
            if (j < world.getMinBuildHeight()) {
                break;
            }

            boolean bl = true;

            for (int k = x - i; k <= x + i && bl; ++k) {
                for (int l = z - i; l <= z + i; ++l) {
                    if (world.getBlockState(new BlockPos(k, j, l)).getBlock() == Blocks.NETHERITE_BLOCK) {
                        netheriteLevel++;
                    }
                    if (!world.getBlockState(new BlockPos(k, j, l)).is(BlockTags.BEACON_BASE_BLOCKS)) {
                        bl = false;
                        break;
                    }
                }
            }

            if (!bl) {
                break;
            }
        }

        return new Tuple<>(beaconLevel, netheriteLevel);
    }

    public static void playSound(Level world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public int getNetheriteLevel() {
        return netheriteLevel;
    }

    @Override
    public void setRemoved() {
        playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
        super.setRemoved();
    }

    private void applyPlayerEffects() {
        if (!level.isClientSide && primary != null) {
            double effectBoundingBox = beaconLevel * 10 + 10;
            int primaryEffectLevel = 0;
            int secondaryEffectLevel = 0;
            if (beaconLevel >= 4) {
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

            int effectLength = (9 + beaconLevel * 3) * 20;
            AABB box = new AABB(worldPosition).inflate(effectBoundingBox).expandTowards(0.0D, level.getHeight(), 0.0D);
            List<Player> list = level.getEntitiesOfClass(Player.class, box);

            for (Player player : list) {
                player.addEffect(new MobEffectInstance(primary, effectLength, primaryEffectLevel, true, true));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, effectLength, 0, true, true));
                player.addEffect(new MobEffectInstance(NetheritePlusStatusEffects.LAVA_VISION, effectLength, Math.min(netheriteLevel, 127), true, true));

                // regeneration case
                if (beaconLevel >= 4 && primary != secondary && secondary != null) {
                    player.addEffect(new MobEffectInstance(secondary, effectLength, secondaryEffectLevel, true, true));
                }
            }

            if (tertiary == MobEffects.GLOWING) {
                List<Mob> entities = level.getEntitiesOfClass(Mob.class, box);
                for (LivingEntity entity : entities) {
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, effectLength, 0, true, true));
                }
            }

        }
    }

    public List<BeamSegment> getBeamSegments() {
        return beaconLevel == 0 ? ImmutableList.of() : beamSegments;
    }

    public int getBeaconLevel() {
        return beaconLevel;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        primary = getPotionEffectById(tag.getInt("Primary"));
        secondary = getPotionEffectById(tag.getInt("Secondary"));
        tertiary = getPotionEffectById(tag.getInt("Tertiary"));
        netheriteLevel = tag.getInt("NetheriteLevel");
        if (tag.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"));
        }

        lock = LockCode.fromTag(tag);
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("Primary", MobEffect.getId(primary));
        tag.putInt("Secondary", MobEffect.getId(secondary));
        tag.putInt("Tertiary", MobEffect.getId(tertiary));
        tag.putInt("Levels", beaconLevel);
        tag.putInt("NetheriteLevel", netheriteLevel);
        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName));
        }

        lock.addToTag(tag);
    }

    public void setCustomName(@Nullable Component text) {
        customName = text;
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return BaseContainerBlockEntity.canUnlock(playerEntity, lock, getDisplayName()) ? new NetheriteBeaconScreenHandler(i, playerInventory, propertyDelegate, ContainerLevelAccess.create(level, getBlockPos())) : null;
    }

    @Override
    public Component getDisplayName() {
        return customName != null ? customName : Component.translatable("container.netherite_beacon");
    }

    @Override
    public void setLevel(Level world) {
        super.setLevel(world);
        this.minY = world.getMinBuildHeight() - 1;
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

        public float[] getColor() {
            return color;
        }

        public int getHeight() {
            return height;
        }
    }
}
