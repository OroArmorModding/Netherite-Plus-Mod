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

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.BeaconBeamBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

public class NetheriteBeaconBlockEntity extends BlockEntity implements MenuProvider, TickableBlockEntity {

    public static final MobEffect[][] EFFECTS_BY_LEVEL;
    private static final Set<MobEffect> EFFECTS;
    private List<NetheriteBeaconBlockEntity.BeamSegment> beamSegments = Lists.newArrayList();
    private List<NetheriteBeaconBlockEntity.BeamSegment> field_19178 = Lists.newArrayList();
    private int beaconLevel;
    private int netheriteLevel;

    public int getNetheriteLevel() {
        return netheriteLevel;
    }

    private int field_19179 = -1;
    @Nullable
    private MobEffect primary;
    @Nullable
    private MobEffect secondary;
    @Nullable
    private MobEffect tertiary;
    @Nullable
    private Component customName;
    private LockCode lock;
    private final ContainerData propertyDelegate;

    public NetheriteBeaconBlockEntity() {
        super(NetheritePlusBlocks.NETHERITE_BEACON_BLOCK_ENTITY.get());
        lock = LockCode.NO_LOCK;
        propertyDelegate = new ContainerData() {
            @Override
            public int get(int index) {
                switch (index) {
                    case 0:
                        return beaconLevel;
                    case 1:
                        return MobEffect.getId(primary);
                    case 2:
                        return MobEffect.getId(secondary);
                    case 3:
                        return MobEffect.getId(tertiary);
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
                        if (!NetheriteBeaconBlockEntity.this.level.isClientSide && !beamSegments.isEmpty()) {
                            NetheriteBeaconBlockEntity.this.playSound(SoundEvents.BEACON_POWER_SELECT);
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
    }

    @Override
    public void tick() {
        int i = worldPosition.getX();
        int j = worldPosition.getY();
        int k = worldPosition.getZ();
        BlockPos blockPos2;
        if (field_19179 < j) {
            blockPos2 = worldPosition;
            field_19178 = Lists.newArrayList();
            field_19179 = blockPos2.getY() - 1;
        } else {
            blockPos2 = new BlockPos(i, field_19179 + 1, k);
        }

        BeamSegment beamSegment = field_19178.isEmpty() ? null : field_19178.get(field_19178.size() - 1);
        int l = this.level.getHeight(Heightmap.Types.WORLD_SURFACE, i, k);

        int n;
        for (n = 0; n < 10 && blockPos2.getY() <= l; ++n) {
            BlockState blockState = level.getBlockState(blockPos2);
            Block block = blockState.getBlock();
            if (block instanceof BeaconBeamBlock) {
                float[] fs = ((BeaconBeamBlock) block).getColor().getTextureDiffuseColors();
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
                if (beamSegment == null || blockState.getLightBlock(level, blockPos2) >= 15 && block != Blocks.BEDROCK) {
                    field_19178.clear();
                    field_19179 = l;
                    break;
                }

                beamSegment.increaseHeight();
            }

            blockPos2 = blockPos2.above();
            ++field_19179;
        }

        n = beaconLevel;
        if (level.getGameTime() % 80L == 0L) {
            if (!beamSegments.isEmpty()) {
                updateLevel(i, j, k);
                if (netheriteLevel == 164) {
                    List<ServerPlayer> var14 = level.getEntitiesOfClass(ServerPlayer.class, new AABB(i, j, k, i, j - 4, k).inflate(10.0D, 5.0D, 10.0D));

                    for (ServerPlayer serverPlayerEntity : var14) {
                        NetheritePlusCriteria.FULL_NETHERITE_NETHERITE_BEACON.trigger(serverPlayerEntity, this);
                    }

                }

                if (beaconLevel == 4) {
                    List<ServerPlayer> var14 = level.getEntitiesOfClass(ServerPlayer.class, new AABB(i, j, k, i, j - 4, k).inflate(10.0D, 5.0D, 10.0D));

                    for (ServerPlayer serverPlayerEntity : var14) {
                        NetheritePlusCriteria.CONSTRUCT_NETHERITE_BEACON.trigger(serverPlayerEntity, this);
                    }
                }
            }

            if (beaconLevel > 0 && !beamSegments.isEmpty()) {
                applyPlayerEffects();
                playSound(SoundEvents.BEACON_AMBIENT);
            }
        }

        if (field_19179 >= l) {
            field_19179 = -1;
            boolean bl = n > 0;
            beamSegments = field_19178;
            if (!level.isClientSide) {
                boolean bl2 = beaconLevel > 0;
                if (!bl && bl2) {
                    playSound(SoundEvents.BEACON_ACTIVATE);
                } else if (bl && !bl2) {
                    playSound(SoundEvents.BEACON_DEACTIVATE);
                }
            }
        }

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
                    if (level.getBlockState(new BlockPos(k, j, l)).getBlock() == Blocks.NETHERITE_BLOCK) {
                        netheriteLevel++;
                    }
                    if (!level.getBlockState(new BlockPos(k, j, l)).is(BlockTags.BEACON_BASE_BLOCKS)) {
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
    public void setRemoved() {
        playSound(SoundEvents.BEACON_DEACTIVATE);
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

            int effectLength = (9 + beaconLevel * 2) * 20;
            AABB box = new AABB(worldPosition).inflate(effectBoundingBox).expandTowards(0.0D, level.getMaxBuildHeight(), 0.0D);
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

    public void playSound(SoundEvent soundEvent) {
        level.playSound(null, worldPosition, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Environment(EnvType.CLIENT)
    public List<NetheriteBeaconBlockEntity.BeamSegment> getBeamSegments() {
        return beaconLevel == 0 ? ImmutableList.of() : beamSegments;
    }

    public int getBeaconLevel() {
        return beaconLevel;
    }

    @Override
    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(worldPosition, 3, getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public double getViewDistance() {
        return 256.0D;
    }

    @Nullable
    private static MobEffect getPotionEffectById(int id) {
        MobEffect statusEffect = MobEffect.byId(id);
        return EFFECTS.contains(statusEffect) ? statusEffect : null;
    }

    @Override
    public void load(BlockState state, CompoundTag tag) {
        super.load(state, tag);
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
    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("Primary", MobEffect.getId(primary));
        tag.putInt("Secondary", MobEffect.getId(secondary));
        tag.putInt("Tertiary", MobEffect.getId(tertiary));
        tag.putInt("Levels", beaconLevel);
        tag.putInt("NetheriteLevel", netheriteLevel);
        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName));
        }

        lock.addToTag(tag);
        return tag;
    }

    public void setCustomName(Component text) {
        customName = text;
    }

    @Override
    @Nullable
    public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
        return BaseContainerBlockEntity.canUnlock(playerEntity, lock, getDisplayName()) ? new NetheriteBeaconScreenHandler(i, playerInventory, propertyDelegate, ContainerLevelAccess.create(level, getBlockPos())) : null;
    }

    @Override
    public Component getDisplayName() {
        return customName != null ? customName : new TranslatableComponent("container.netherite_beacon");
    }

    static {
        EFFECTS_BY_LEVEL = new MobEffect[][]{{MobEffects.MOVEMENT_SPEED, MobEffects.DIG_SPEED}, {MobEffects.DAMAGE_RESISTANCE, MobEffects.JUMP}, {MobEffects.DAMAGE_BOOST}, {MobEffects.REGENERATION}, {MobEffects.GLOWING}};
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
