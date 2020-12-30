package com.oroarmor.netherite_plus.block;

import java.util.function.Supplier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import me.shedaniel.architectury.registry.BlockEntityRenderers;
import me.shedaniel.architectury.registry.Registries;
import me.shedaniel.architectury.registry.RegistrySupplier;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Registry;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class NetheritePlusBlocks {
    public static RegistrySupplier<Block> NETHERITE_WHITE_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_ORANGE_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_MAGENTA_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_LIGHT_BLUE_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_YELLOW_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_BLACK_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_BLUE_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_BROWN_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_CYAN_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_GRAY_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_GREEN_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_LIGHT_GRAY_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_LIME_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_PINK_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_PURPLE_SHULKER_BOX;
    public static RegistrySupplier<Block> NETHERITE_RED_SHULKER_BOX;

    public static RegistrySupplier<Block> FAKE_NETHERITE_BLOCK;

    public static RegistrySupplier<Block> NETHERITE_ANVIL_BLOCK;

    public static RegistrySupplier<Block> NETHERITE_BEACON;

    public static RegistrySupplier<BlockEntityType<?>> NETHERITE_SHULKER_BOX_ENTITY;
    public static RegistrySupplier<BlockEntityType<?>> NETHERITE_BEACON_BLOCK_ENTITY;

    static {
        if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
            registerShulkerBoxBlocks();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_FAKE_NETHERITE_BLOCKS.getValue()) {
            FAKE_NETHERITE_BLOCK = register("fake_netherite_block", new FakeNetheriteBlock(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.COLOR_BLACK).requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.NETHERITE_BLOCK)));
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_ANVIL.getValue()) {
            NETHERITE_ANVIL_BLOCK = register("netherite_anvil", new NetheriteAnvilBlock(BlockBehaviour.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F, 1200.0F).sound(SoundType.ANVIL)));
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
            NETHERITE_BEACON = register("netherite_beacon", new NetheriteBeaconBlock(BlockBehaviour.Properties.of(Material.GLASS, MaterialColor.DIAMOND).strength(3.0F).lightLevel((state) -> {
                return 15;
            }).noOcclusion()));

            NETHERITE_BEACON_BLOCK_ENTITY = NetheritePlusMod.REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_REGISTRY).registerSupplied(id("netherite_beacon"), () -> BlockEntityType.Builder.of(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON.get()).build(null));
        }
    }

    private static NetheriteShulkerBoxBlock createShulkerBoxBlock(DyeColor color, BlockBehaviour.Properties settings) {
        BlockBehaviour.StatePredicate contextPredicate = (blockState, blockView, blockPos) -> {
            BlockEntity blockEntity = blockView.getBlockEntity(blockPos);
            if (!(blockEntity instanceof NetheriteShulkerBoxBlockEntity)) {
                return true;
            }
            NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
            return shulkerBoxBlockEntity.suffocates();
        };
        return new NetheriteShulkerBoxBlock(color, settings.strength(2.0F).dynamicShape().noOcclusion().isSuffocating(contextPredicate).isViewBlocking(contextPredicate));
    }

    private static RegistrySupplier<Block> register(String id, Block block) {
        return NetheritePlusMod.REGISTRIES.get().get(Registry.BLOCK_REGISTRY).registerSupplied(id(id), () -> block);
    }

    private static void registerShulkerBoxBlocks() {
        NETHERITE_SHULKER_BOX = register("netherite_shulker_box", createShulkerBoxBlock(null, BlockBehaviour.Properties.of(Material.SHULKER_SHELL).strength(2f, 1200f)));
        NETHERITE_WHITE_SHULKER_BOX = register("netherite_white_shulker_box", createShulkerBoxBlock(DyeColor.WHITE, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.SNOW)));
        NETHERITE_ORANGE_SHULKER_BOX = register("netherite_orange_shulker_box", createShulkerBoxBlock(DyeColor.ORANGE, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_ORANGE)));
        NETHERITE_MAGENTA_SHULKER_BOX = register("netherite_magenta_shulker_box", createShulkerBoxBlock(DyeColor.MAGENTA, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_MAGENTA)));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register("netherite_light_blue_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_BLUE, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_LIGHT_BLUE)));
        NETHERITE_YELLOW_SHULKER_BOX = register("netherite_yellow_shulker_box", createShulkerBoxBlock(DyeColor.YELLOW, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_YELLOW)));
        NETHERITE_LIME_SHULKER_BOX = register("netherite_lime_shulker_box", createShulkerBoxBlock(DyeColor.LIME, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_LIGHT_GREEN)));
        NETHERITE_PINK_SHULKER_BOX = register("netherite_pink_shulker_box", createShulkerBoxBlock(DyeColor.PINK, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_PINK)));
        NETHERITE_GRAY_SHULKER_BOX = register("netherite_gray_shulker_box", createShulkerBoxBlock(DyeColor.GRAY, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_GRAY)));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register("netherite_light_gray_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_GRAY, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_LIGHT_GRAY)));
        NETHERITE_CYAN_SHULKER_BOX = register("netherite_cyan_shulker_box", createShulkerBoxBlock(DyeColor.CYAN, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_CYAN)));
        NETHERITE_PURPLE_SHULKER_BOX = register("netherite_purple_shulker_box", createShulkerBoxBlock(DyeColor.PURPLE, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.TERRACOTTA_PURPLE)));
        NETHERITE_BLUE_SHULKER_BOX = register("netherite_blue_shulker_box", createShulkerBoxBlock(DyeColor.BLUE, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_BLUE)));
        NETHERITE_BROWN_SHULKER_BOX = register("netherite_brown_shulker_box", createShulkerBoxBlock(DyeColor.BROWN, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_BROWN)));
        NETHERITE_GREEN_SHULKER_BOX = register("netherite_green_shulker_box", createShulkerBoxBlock(DyeColor.GREEN, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_GREEN)));
        NETHERITE_RED_SHULKER_BOX = register("netherite_red_shulker_box", createShulkerBoxBlock(DyeColor.RED, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_RED)));
        NETHERITE_BLACK_SHULKER_BOX = register("netherite_black_shulker_box", createShulkerBoxBlock(DyeColor.BLACK, BlockBehaviour.Properties.of(Material.SHULKER_SHELL, MaterialColor.COLOR_BLACK)));

        NETHERITE_SHULKER_BOX_ENTITY = NetheritePlusMod.REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_REGISTRY).registerSupplied(id("netherite_shulker_box"), () -> BlockEntityType.Builder.of(NetheriteShulkerBoxBlockEntity::new, NETHERITE_SHULKER_BOX.get(), NETHERITE_BLACK_SHULKER_BOX.get(), NETHERITE_BLUE_SHULKER_BOX.get(), NETHERITE_BROWN_SHULKER_BOX.get(), NETHERITE_CYAN_SHULKER_BOX.get(), NETHERITE_GRAY_SHULKER_BOX.get(), NETHERITE_GREEN_SHULKER_BOX.get(), NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), NETHERITE_LIME_SHULKER_BOX.get(), NETHERITE_MAGENTA_SHULKER_BOX.get(), NETHERITE_ORANGE_SHULKER_BOX.get(), NETHERITE_PINK_SHULKER_BOX.get(), NETHERITE_PURPLE_SHULKER_BOX.get(), NETHERITE_RED_SHULKER_BOX.get(), NETHERITE_WHITE_SHULKER_BOX.get(), NETHERITE_YELLOW_SHULKER_BOX.get()).build(null));
    }

}
