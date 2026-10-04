/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.world.level.block.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.oroarmor.netherite_plus.advancements.triggers.NetheritePlusCriteriaTriggers;
import com.oroarmor.netherite_plus.world.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.world.inventory.NetheriteBeaconMenu;
import com.oroarmor.netherite_plus.world.level.block.NetheritePlusBlocks;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ARGB;
import net.minecraft.world.LockCode;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BeaconMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BeaconBeamOwner;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class NetheriteBeaconBlockEntity extends BlockEntity implements MenuProvider, Nameable, BeaconBeamOwner {
    public static final List<List<Holder<MobEffect>>> BEACON_EFFECTS = List.of(
            List.of(MobEffects.SPEED, MobEffects.HASTE),
            List.of(MobEffects.RESISTANCE, MobEffects.JUMP_BOOST),
            List.of(MobEffects.STRENGTH),
            List.of(MobEffects.REGENERATION),
            List.of(MobEffects.GLOWING)
    );
    public static final int DATA_LEVELS = 0;
    public static final int DATA_PRIMARY = 1;
    public static final int DATA_SECONDARY = 2;
    public static final int DATA_TERTIARY = 3;
    public static final int NUM_DATA_VALUES = 4;
    private static final int MAX_LEVELS = 5;
    private static final int LEVELS_NEEDED_FOR_SECONDARY = MAX_LEVELS - 1;
    private static final int LEVELS_NEEDED_FOR_TERTIARY = MAX_LEVELS;
    private static final Set<Holder<MobEffect>> VALID_EFFECTS = BEACON_EFFECTS.stream().flatMap(Collection::stream).collect(Collectors.toSet());
    private static final int BLOCKS_CHECK_PER_TICK = 10;
    private static final Component DEFAULT_NAME = Component.translatable("container.netherite_beacon");
    private static final String TAG_PRIMARY = "primary_effect";
    private static final String TAG_SECONDARY = "secondary_effect";
    private static final String TAG_TERTIARY = "tertiary_effect";
    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int index) {
            return switch (index) {
                case DATA_LEVELS -> NetheriteBeaconBlockEntity.this.levels;
                case DATA_PRIMARY -> BeaconMenu.encodeEffect(primaryPower);
                case DATA_SECONDARY -> BeaconMenu.encodeEffect(secondaryPower);
                case DATA_TERTIARY -> BeaconMenu.encodeEffect(tertiaryPower);
                default -> 0;
            };
        }

        @Override
        public void set(int index, int value) {
            switch (index) {
                case DATA_LEVELS -> NetheriteBeaconBlockEntity.this.levels = value;
                case DATA_PRIMARY -> {
                    //noinspection DataFlowIssue
                    if (!NetheriteBeaconBlockEntity.this.level.isClientSide() && !NetheriteBeaconBlockEntity.this.beamSections.isEmpty()) {
                        playSound(NetheriteBeaconBlockEntity.this.level, NetheriteBeaconBlockEntity.this.worldPosition, SoundEvents.BEACON_POWER_SELECT);
                    }

                    primaryPower = NetheriteBeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
                }
                case DATA_SECONDARY -> secondaryPower = NetheriteBeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
                case DATA_TERTIARY -> tertiaryPower = NetheriteBeaconBlockEntity.filterEffect(BeaconMenu.decodeEffect(value));
            }
        }

        @Override
        public int getCount() {
            return NUM_DATA_VALUES;
        }
    };
    private List<BeaconBeamOwner.Section> beamSections = new ArrayList<>();
    private List<BeaconBeamOwner.Section> checkingBeamSections = new ArrayList<>();
    private int levels;
    private int netheriteLevel;
    private int lastCheckY;
    private @Nullable Holder<MobEffect> primaryPower;
    private @Nullable Holder<MobEffect> secondaryPower;
    private @Nullable Holder<MobEffect> tertiaryPower;
    private @Nullable Component name;
    private LockCode lockKey = LockCode.NO_LOCK;

    public NetheriteBeaconBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY, blockPos, blockState);
    }

    private static @Nullable Holder<MobEffect> filterEffect(final @Nullable Holder<MobEffect> effect) {
        return VALID_EFFECTS.contains(effect) ? effect : null;
    }

    public static void tick(final Level level, final BlockPos pos, final BlockState selfState, final NetheriteBeaconBlockEntity entity) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        BlockPos checkPos;
        if (entity.lastCheckY < y) {
            checkPos = pos;
            entity.checkingBeamSections = Lists.newArrayList();
            entity.lastCheckY = checkPos.getY() - 1;
        } else {
            checkPos = new BlockPos(x, entity.lastCheckY + 1, z);
        }

        BeaconBeamOwner.Section lastBeamSection = entity.checkingBeamSections.isEmpty() ? null : entity.checkingBeamSections.getLast();
        int lastSetBlock = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

        for (int i = 0; i < BLOCKS_CHECK_PER_TICK && checkPos.getY() <= lastSetBlock; i++) {
            BlockState state = level.getBlockState(checkPos);
            if (state.getBlock() instanceof BeaconBeamBlock beaconBeamBlock) {
                int color = beaconBeamBlock.getColor().getTextureDiffuseColor();
                if (entity.checkingBeamSections.size() <= 1) {
                    lastBeamSection = new BeaconBeamOwner.Section(color);
                    entity.checkingBeamSections.add(lastBeamSection);
                } else if (lastBeamSection != null) {
                    if (color == lastBeamSection.getColor()) {
                        lastBeamSection.increaseHeight();
                    } else {
                        lastBeamSection = new BeaconBeamOwner.Section(ARGB.average(lastBeamSection.getColor(), color));
                        entity.checkingBeamSections.add(lastBeamSection);
                    }
                }
            } else {
                if (lastBeamSection == null || state.getLightDampening() >= 15 && !state.is(Blocks.BEDROCK)) {
                    entity.checkingBeamSections.clear();
                    entity.lastCheckY = lastSetBlock;
                    break;
                }

                lastBeamSection.increaseHeight();
            }

            checkPos = checkPos.above();
            entity.lastCheckY++;
        }

        int previousLevels = entity.levels;
        AABB effectBox = new AABB(x, y, z, x, y - MAX_LEVELS, z).inflate(10.0D, 5.0D, 10.0D);
        if (level.getGameTime() % 80L == 0L) {
            if (!entity.beamSections.isEmpty()) {
                Pair<Integer, Integer> levels = updateLevels(level, x, y, z);
                entity.levels = levels.getFirst();
                entity.netheriteLevel = levels.getSecond();
                if (entity.netheriteLevel == 3 * 3 + 5 * 5 + 7 * 7 + 9 * 9 + 11 * 11) {
                    List<ServerPlayer> var14 = level.getEntitiesOfClass(ServerPlayer.class, effectBox);

                    for (ServerPlayer serverPlayerEntity : var14) {
                        NetheritePlusCriteriaTriggers.FULL_NETHERITE_NETHERITE_BEACON.trigger(serverPlayerEntity, entity);
                    }
                }
            }

            if (entity.levels > 0 && !entity.beamSections.isEmpty()) {
                applyEffects(level, pos, entity.levels, entity.netheriteLevel, entity.primaryPower, entity.secondaryPower, entity.tertiaryPower);
                playSound(level, pos, SoundEvents.BEACON_AMBIENT);
            }
        }

        if (entity.lastCheckY >= lastSetBlock) {
            entity.lastCheckY = level.getMinY() - 1;
            boolean wasActive = previousLevels > 0;
            entity.beamSections = entity.checkingBeamSections;
            if (!level.isClientSide()) {
                boolean isActive = entity.levels > 0;
                if (!wasActive && isActive) {
                    playSound(level, pos, SoundEvents.BEACON_ACTIVATE);

                    for (ServerPlayer player : level.getEntitiesOfClass(ServerPlayer.class, effectBox)) {
                        NetheritePlusCriteriaTriggers.CONSTRUCT_NETHERITE_BEACON.trigger(player, entity);
                    }
                } else if (wasActive && !isActive) {
                    playSound(level, pos, SoundEvents.BEACON_DEACTIVATE);
                }

                level.setBlock(pos, selfState.setValue(BlockStateProperties.POWERED, entity.levels > 0), Block.UPDATE_CLIENTS);
            }
        }
    }

    private static Pair<Integer, Integer> updateLevels(Level level, int x, int y, int z) {
        int beaconLevel = 0;
        int netheriteLevel = 0;

        for (int step = 1; step <= MAX_LEVELS; beaconLevel = step++) {
            int ly = y - step;
            if (ly < level.getMinY()) {
                break;
            }

            boolean isOk = true;

            for (int lx = x - step; lx <= x + step && isOk; ++lx) {
                for (int lz = z - step; lz <= z + step; ++lz) {
                    if (level.getBlockState(new BlockPos(lx, ly, lz)).getBlock() == Blocks.NETHERITE_BLOCK) {
                        netheriteLevel++;
                    }
                    if (!level.getBlockState(new BlockPos(lx, ly, lz)).is(BlockTags.BEACON_BASE_BLOCKS)) {
                        isOk = false;
                        break;
                    }
                }
            }

            if (!isOk) {
                break;
            }
        }

        return new Pair<>(beaconLevel, netheriteLevel);
    }

    public static boolean validateEffects(final @Nullable Holder<MobEffect> primary, final @Nullable Holder<MobEffect> secondary, final @Nullable Holder<MobEffect> tertiary, final int levels) {
        if (secondary != null && levels < LEVELS_NEEDED_FOR_SECONDARY) {
            return false;
        } else if (tertiary != null && levels < LEVELS_NEEDED_FOR_TERTIARY) {
            return false;
        }

        int primaryLevel = getRequiredLevelsFor(primary);
        int secondaryLevel = getRequiredLevelsFor(secondary);
        int tertiaryLevel = getRequiredLevelsFor(tertiary);

        // Needed level for effect is higher than current level
        if (primaryLevel > levels || secondaryLevel > levels || tertiaryLevel > levels) {
            return false;
        }

        // primary has something higher selected
        if (primaryLevel >= LEVELS_NEEDED_FOR_SECONDARY) return false;

        // secondary has something higher selected
        if (secondaryLevel >= LEVELS_NEEDED_FOR_TERTIARY) return false;

        // No secondary, secondary level must be level 4, or the secondary is the same as primary
        boolean secondaryValid = secondaryLevel == 0 || secondaryLevel == LEVELS_NEEDED_FOR_SECONDARY || Objects.equals(primary, secondary);
        // No tertiary, tertiary level must be level 5, or the secondary is the same as primary or secondary
        boolean tertiaryValid = tertiaryLevel == 0 || tertiaryLevel == LEVELS_NEEDED_FOR_TERTIARY || Objects.equals(primary, tertiary) || Objects.equals(secondary, tertiary);

        return secondaryValid && tertiaryValid;
    }

    private static int getRequiredLevelsFor(final @Nullable Holder<MobEffect> effect) {
        if (effect == null) {
            return 0;
        }

        for (int i = 0; i < BEACON_EFFECTS.size(); i++) {
            List<Holder<MobEffect>> effectsForLevel = BEACON_EFFECTS.get(i);
            if (effectsForLevel.contains(effect)) {
                return i + 1;
            }
        }

        return Integer.MAX_VALUE;
    }

    private static void applyEffects(
            final Level level,
            final BlockPos worldPosition,
            final int levels,
            final int netheriteLevel,
            final @Nullable Holder<MobEffect> primaryPower,
            final @Nullable Holder<MobEffect> secondaryPower,
            final @Nullable Holder<MobEffect> tertiaryPower
    ) {
        if (!level.isClientSide() && primaryPower != null) {
            double effectBoundingBox = levels * 10 + 10;
            int primaryEffectLevel = 0;
            int secondaryEffectLevel = 0;
            if (levels >= LEVELS_NEEDED_FOR_SECONDARY) {
                if (primaryPower == secondaryPower) {
                    primaryEffectLevel++;
                }

                if (primaryPower == tertiaryPower) {
                    primaryEffectLevel++;
                }

                if (secondaryPower == tertiaryPower) {
                    secondaryEffectLevel++;
                }

            }

            int effectLength = (9 + levels * 3) * 20;
            AABB box = new AABB(worldPosition).inflate(effectBoundingBox).expandTowards(0.0D, level.getHeight(), 0.0D);
            List<Player> list = level.getEntitiesOfClass(Player.class, box);

            for (Player player : list) {
                player.addEffect(new MobEffectInstance(primaryPower, effectLength, primaryEffectLevel, true, true));
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, effectLength, 0, true, true));
                player.addEffect(new MobEffectInstance(NetheritePlusStatusEffects.LAVA_VISION, effectLength, Math.min(netheriteLevel, 127), true, true));

                // regeneration case
                if (levels >= LEVELS_NEEDED_FOR_SECONDARY && primaryPower != secondaryPower && secondaryPower != null) {
                    player.addEffect(new MobEffectInstance(secondaryPower, effectLength, secondaryEffectLevel, true, true));
                }
            }

            if (tertiaryPower == MobEffects.GLOWING) {
                List<Mob> entities = level.getEntitiesOfClass(Mob.class, box);
                for (LivingEntity entity : entities) {
                    entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, effectLength, 0, true, true));
                }
            }

        }
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private static void storeEffect(final ValueOutput output, final String field, final @Nullable Holder<MobEffect> effect) {
        if (effect != null) {
            effect.unwrapKey().ifPresent(key -> output.putString(field, key.identifier().toString()));
        }
    }

    private static @Nullable Holder<MobEffect> loadEffect(final ValueInput input, final String field) {
        return input.read(field, BuiltInRegistries.MOB_EFFECT.holderByNameCodec()).filter(VALID_EFFECTS::contains).orElse(null);
    }

    public int getNetheriteLevel() {
        return netheriteLevel;
    }

    @Override
    public void setRemoved() {
        //noinspection DataFlowIssue
        playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
        super.setRemoved();
    }

    public List<BeaconBeamOwner.Section> getBeamSections() {
        return levels == 0 ? ImmutableList.of() : beamSections;
    }

    public int getBeaconLevel() {
        return levels;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(final HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.primaryPower = loadEffect(input, TAG_PRIMARY);
        this.secondaryPower = loadEffect(input, TAG_SECONDARY);
        this.tertiaryPower = loadEffect(input, TAG_TERTIARY);
        this.name = parseCustomNameSafe(input, "CustomName");
        this.lockKey = LockCode.fromTag(input);
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        storeEffect(output, TAG_PRIMARY, this.primaryPower);
        storeEffect(output, TAG_SECONDARY, this.secondaryPower);
        storeEffect(output, TAG_TERTIARY, this.tertiaryPower);
        output.storeNullable("CustomName", ComponentSerialization.CODEC, this.name);
        this.lockKey.addToTag(output);
    }

    @Override
    public @Nullable Component getCustomName() {
        return this.name;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(final int containerId, final Inventory inventory, final Player player) {
        if (this.lockKey.canUnlock(player)) {
            //noinspection DataFlowIssue
            return new NetheriteBeaconMenu(containerId, inventory, this.dataAccess, ContainerLevelAccess.create(this.level, this.getBlockPos()));
        }

        BaseContainerBlockEntity.sendChestLockedNotifications(Vec3.atCenterOf(this.getBlockPos()), player, this.getDisplayName());
        return null;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }

    @Override
    public Component getName() {
        return this.name != null ? this.name : DEFAULT_NAME;
    }

    @Override
    protected void applyImplicitComponents(final DataComponentGetter components) {
        super.applyImplicitComponents(components);
        this.name = components.get(DataComponents.CUSTOM_NAME);
        this.lockKey = components.getOrDefault(DataComponents.LOCK, LockCode.NO_LOCK);
    }

    @Override
    protected void collectImplicitComponents(final DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.name);
        if (!this.lockKey.equals(LockCode.NO_LOCK)) {
            components.set(DataComponents.LOCK, this.lockKey);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeComponentsFromTag(final ValueOutput output) {
        output.discard("CustomName");
        output.discard("lock");
    }

    @Override
    public void setLevel(final Level level) {
        super.setLevel(level);
        this.lastCheckY = level.getMinY() - 1;
    }
}
