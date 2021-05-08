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
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.Heightmap;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteBeaconBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Tickable {
    public static final StatusEffect[][] EFFECTS_BY_LEVEL;
    private static final Set<StatusEffect> EFFECTS;

    static {
        EFFECTS_BY_LEVEL = new StatusEffect[][]{{StatusEffects.SPEED, StatusEffects.HASTE}, {StatusEffects.RESISTANCE, StatusEffects.JUMP_BOOST}, {StatusEffects.STRENGTH}, {StatusEffects.REGENERATION}, {StatusEffects.GLOWING}};
        EFFECTS = Arrays.stream(EFFECTS_BY_LEVEL).flatMap(Arrays::stream).collect(Collectors.toSet());
    }

    private final PropertyDelegate propertyDelegate;
    private List<NetheriteBeaconBlockEntity.BeamSegment> beamSegments = Lists.newArrayList();
    private List<NetheriteBeaconBlockEntity.BeamSegment> field_19178 = Lists.newArrayList();
    private int beaconLevel;
    private int netheriteLevel;
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

    public NetheriteBeaconBlockEntity() {
        super(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get());
        lock = ContainerLock.EMPTY;
        propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return beaconLevel;
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
                        beaconLevel = value;
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

    @Nullable
    private static StatusEffect getPotionEffectById(int id) {
        StatusEffect statusEffect = StatusEffect.byRawId(id);
        return EFFECTS.contains(statusEffect) ? statusEffect : null;
    }

    public int getNetheriteLevel() {
        return netheriteLevel;
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
        int l = this.world.getTopY(Heightmap.Type.WORLD_SURFACE, i, k);

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
                        beamSegment = new BeamSegment(new float[]{(beamSegment.color[0] + fs[0]) / 2.0F, (beamSegment.color[1] + fs[1]) / 2.0F, (beamSegment.color[2] + fs[2]) / 2.0F});
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

        n = beaconLevel;
        if (world.getTime() % 80L == 0L) {
            if (!beamSegments.isEmpty()) {
                updateLevel(i, j, k);
                if (netheriteLevel == 164) {
                    List<ServerPlayerEntity> var14 = world.getNonSpectatingEntities(ServerPlayerEntity.class, new Box(i, j, k, i, j - 4, k).expand(10.0D, 5.0D, 10.0D));

                    for (ServerPlayerEntity serverPlayerEntity : var14) {
                        NetheritePlusCriteria.FULL_NETHERITE_NETHERITE_BEACON.trigger(serverPlayerEntity, this);
                    }

                }

                if (beaconLevel == 4) {
                    List<ServerPlayerEntity> var14 = world.getNonSpectatingEntities(ServerPlayerEntity.class, new Box(i, j, k, i, j - 4, k).expand(10.0D, 5.0D, 10.0D));

                    for (ServerPlayerEntity serverPlayerEntity : var14) {
                        NetheritePlusCriteria.CONSTRUCT_NETHERITE_BEACON.trigger(serverPlayerEntity, this);
                    }
                }
            }

            if (beaconLevel > 0 && !beamSegments.isEmpty()) {
                applyPlayerEffects();
                playSound(SoundEvents.BLOCK_BEACON_AMBIENT);
            }
        }

        if (field_19179 >= l) {
            field_19179 = -1;
            boolean bl = n > 0;
            beamSegments = field_19178;
            if (!world.isClient) {
                boolean bl2 = beaconLevel > 0;
                if (!bl && bl2) {
                    playSound(SoundEvents.BLOCK_BEACON_ACTIVATE);
                } else if (bl && !bl2) {
                    playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
                }
            }
        }

        this.getWorld().setBlockState(this.pos, this.getWorld().getBlockState(this.pos).with(Properties.POWERED, this.beaconLevel > 0), 2);
    }

    private void updateLevel(int x, int y, int z) {
        beaconLevel = 0;
        netheriteLevel = 0;

        for (int i = 1; i <= 4; beaconLevel = i++) {
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
                player.addStatusEffect(new StatusEffectInstance(NetheritePlusStatusEffects.LAVA_VISION.get(), effectLength, Math.min(netheriteLevel, 127), true, true));

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

    public void playSound(SoundEvent soundEvent) {
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }

    public List<NetheriteBeaconBlockEntity.BeamSegment> getBeamSegments() {
        return beaconLevel == 0 ? ImmutableList.of() : beamSegments;
    }

    public int getBeaconLevel() {
        return beaconLevel;
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
        tag.putInt("Levels", beaconLevel);
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
        return LockableContainerBlockEntity.checkUnlocked(playerEntity, lock, getDisplayName()) ? new NetheriteBeaconScreenHandler(i, playerInventory, propertyDelegate, ScreenHandlerContext.create(world, getPos())) : null;
    }

    @Override
    public Text getDisplayName() {
        return customName != null ? customName : new TranslatableText("container.netherite_beacon");
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
