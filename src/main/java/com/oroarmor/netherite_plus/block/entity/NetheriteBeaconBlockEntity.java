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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteBeaconBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public static final StatusEffect[][] EFFECTS_BY_LEVEL = new StatusEffect[][]{{StatusEffects.SPEED, StatusEffects.HASTE}, {StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST}, {StatusEffects.STRENGTH}, {StatusEffects.REGENERATION}, {StatusEffects.GLOWING}};
    private static final Set<StatusEffect> EFFECTS = Arrays.stream(EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());

    private final PropertyDelegate propertyDelegate = new PropertyDelegate() {
        @Override
        public int get(int index) {
            return switch (index) {
                case 0 -> beaconLevel;
                case 1 -> StatusEffect.getRawId(primary);
                case 2 -> StatusEffect.getRawId(secondary);
                case 3 -> StatusEffect.getRawId(tertiary);
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
                    if (!NetheriteBeaconBlockEntity.this.world.isClient && !beamSegments.isEmpty()) {
                        playSound(NetheriteBeaconBlockEntity.this.world, NetheriteBeaconBlockEntity.this.pos, SoundEvents.BLOCK_BEACON_POWER_SELECT);
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
    private List<BeamSegment> beamSegments = Lists.newArrayList();
    private List<BeamSegment> beamSegmentsToCheck = Lists.newArrayList();
    private int beaconLevel;
    private int netheriteLevel;
    private int minY = -1;
    @Nullable
    private StatusEffect primary;
    @Nullable
    private StatusEffect secondary;
    @Nullable
    private StatusEffect tertiary;
    @Nullable
    private Text customName;
    private ContainerLock lock = ContainerLock.EMPTY;

    public NetheriteBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY, blockPos, blockState);
    }

    @Nullable
    private static StatusEffect getPotionEffectById(int id) {
        StatusEffect statusEffect = StatusEffect.byRawId(id);
        return EFFECTS.contains(statusEffect) ? statusEffect : null;
    }

    public int getNetheriteLevel() {
        return netheriteLevel;
    }

    public static void tick(World world, BlockPos pos, BlockState state, NetheriteBeaconBlockEntity blockEntity) {
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
        int l = world.getTopY(Heightmap.Type.WORLD_SURFACE, i, k);

        int n;
        for (n = 0; n < 10 && blockPos2.getY() <= l; ++n) {
            BlockState blockState = world.getBlockState(blockPos2);
            Block block = blockState.getBlock();
            if (block instanceof Stainable) {
                float[] fs = ((Stainable) block).getColor().getColorComponents();
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
                if (beamSegment == null || blockState.getOpacity(world, blockPos2) >= 15 && block != Blocks.BEDROCK) {
                    blockEntity.beamSegmentsToCheck.clear();
                    blockEntity.minY = l;
                    break;
                }

                beamSegment.increaseHeight();
            }

            blockPos2 = blockPos2.up();
            ++blockEntity.minY;
        }

        n = blockEntity.beaconLevel;
        if (world.getTime() % 80L == 0L) {
            if (!blockEntity.beamSegments.isEmpty()) {
                Pair<Integer, Integer> levels = updateLevel(world, i, j, k);
                blockEntity.beaconLevel = levels.getLeft();
                blockEntity.netheriteLevel = levels.getRight();
                if (blockEntity.netheriteLevel == 164) {
                    List<ServerPlayerEntity> var14 = world.getNonSpectatingEntities(ServerPlayerEntity.class, new Box(i, j, k, i, j - 4, k).expand(10.0D, 5.0D, 10.0D));

                    for (ServerPlayerEntity serverPlayerEntity : var14) {
                        NetheritePlusCriteria.FULL_NETHERITE_NETHERITE_BEACON.trigger(serverPlayerEntity, blockEntity);
                    }

                }

                if (blockEntity.beaconLevel == 4) {
                    List<ServerPlayerEntity> var14 = world.getNonSpectatingEntities(ServerPlayerEntity.class, new Box(i, j, k, i, j - 4, k).expand(10.0D, 5.0D, 10.0D));

                    for (ServerPlayerEntity serverPlayerEntity : var14) {
                        NetheritePlusCriteria.CONSTRUCT_NETHERITE_BEACON.trigger(serverPlayerEntity, blockEntity);
                    }
                }
            }

            if (blockEntity.beaconLevel > 0 && !blockEntity.beamSegments.isEmpty()) {
                blockEntity.applyPlayerEffects();
                playSound(world, pos, SoundEvents.BLOCK_BEACON_AMBIENT);
            }
        }

        if (blockEntity.minY >= l) {
            blockEntity.minY = -1;
            boolean bl = n > 0;
            blockEntity.beamSegments = blockEntity.beamSegmentsToCheck;
            if (!world.isClient) {
                boolean bl2 = blockEntity.beaconLevel > 0;
                if (!bl && bl2) {
                    playSound(world, pos, SoundEvents.BLOCK_BEACON_ACTIVATE);
                } else if (bl && !bl2) {
                    playSound(world, pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
                }
            }
        }

        world.setBlockState(pos, state.with(Properties.POWERED, blockEntity.beaconLevel > 0), 2);
    }

    private static Pair<Integer, Integer> updateLevel(World world, int x, int y, int z) {
        int beaconLevel = 0;
        int netheriteLevel = 0;

        for (int i = 1; i <= 4; beaconLevel = i++) {
            int j = y - i;
            if (j < world.getBottomY()) {
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

        return new Pair<>(beaconLevel, netheriteLevel);
    }

    @Override
    public void markRemoved() {
        playSound(this.world, this.pos, SoundEvents.BLOCK_BEACON_DEACTIVATE);
        super.markRemoved();
    }

    private void applyPlayerEffects() {
        if (!world.isClient && primary != null) {
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
            Box box = new Box(pos).expand(effectBoundingBox).stretch(0.0D, world.getHeight(), 0.0D);
            List<PlayerEntity> list = world.getNonSpectatingEntities(PlayerEntity.class, box);

            for (PlayerEntity player : list) {
                player.addStatusEffect(new StatusEffectInstance(primary, effectLength, primaryEffectLevel, true, true));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, effectLength, 0, true, true));
                player.addStatusEffect(new StatusEffectInstance(NetheritePlusStatusEffects.LAVA_VISION, effectLength, Math.min(netheriteLevel, 127), true, true));

                // regeneration case
                if (beaconLevel >= 4 && primary != secondary && secondary != null) {
                    player.addStatusEffect(new StatusEffectInstance(secondary, effectLength, secondaryEffectLevel, true, true));
                }
            }

            if (tertiary == StatusEffects.GLOWING) {
                List<MobEntity> entities = world.getNonSpectatingEntities(MobEntity.class, box);
                for (LivingEntity entity : entities) {
                    entity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, effectLength, 0, true, true));
                }
            }

        }
    }

    public static void playSound(World world, BlockPos pos, SoundEvent sound) {
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public List<BeamSegment> getBeamSegments() {
        return beaconLevel == 0 ? ImmutableList.of() : beamSegments;
    }

    public int getBeaconLevel() {
        return beaconLevel;
    }

    @Override
    @Nullable
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.of(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return this.toNbt();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        primary = getPotionEffectById(tag.getInt("Primary"));
        secondary = getPotionEffectById(tag.getInt("Secondary"));
        tertiary = getPotionEffectById(tag.getInt("Tertiary"));
        netheriteLevel = tag.getInt("NetheriteLevel");
        if (tag.contains("CustomName", 8)) {
            customName = Text.Serializer.fromJson(tag.getString("CustomName"));
        }

        lock = ContainerLock.fromNbt(tag);
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        tag.putInt("Primary", StatusEffect.getRawId(primary));
        tag.putInt("Secondary", StatusEffect.getRawId(secondary));
        tag.putInt("Tertiary", StatusEffect.getRawId(tertiary));
        tag.putInt("Levels", beaconLevel);
        tag.putInt("NetheriteLevel", netheriteLevel);
        if (customName != null) {
            tag.putString("CustomName", Text.Serializer.toJson(customName));
        }

        lock.writeNbt(tag);
    }

    public void setCustomName(@Nullable Text text) {
        customName = text;
    }

    @Override
    @Nullable
    public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return LockableContainerBlockEntity.checkUnlocked(playerEntity, lock, getDisplayName()) ? new NetheriteBeaconScreenHandler(i, playerInventory, propertyDelegate, ScreenHandlerContext.create(world, getPos())) : null;
    }

    @Override
    public Text getDisplayName() {
        return customName != null ? customName : Text.translatable("container.netherite_beacon");
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        this.minY = world.getBottomY() - 1;
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
