package com.oroarmor.netherite_plus.block;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.block.entity.NetheriteBeaconBlockEntity;
import com.oroarmor.netherite_plus.block.entity.NetheriteShulkerBoxBlockEntity;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

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
			FAKE_NETHERITE_BLOCK = register("fake_netherite_block", new FakeNetheriteBlock(AbstractBlock.Settings.of(Material.METAL, MaterialColor.BLACK).requiresTool().strength(5.0F, 6.0F).sounds(BlockSoundGroup.NETHERITE)));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_ANVIL.getValue()) {
			NETHERITE_ANVIL_BLOCK = register("netherite_anvil", new NetheriteAnvilBlock(AbstractBlock.Settings.of(Material.REPAIR_STATION, MaterialColor.IRON).requiresTool().strength(5.0F, 1200.0F).sounds(BlockSoundGroup.ANVIL)));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
			NETHERITE_BEACON = register("netherite_beacon", new NetheriteBeaconBlock(AbstractBlock.Settings.of(Material.GLASS, MaterialColor.DIAMOND).strength(3.0F).luminance((state) -> 15).nonOpaque()));

			NETHERITE_BEACON_BLOCK_ENTITY = NetheritePlusMod.REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_KEY).registerSupplied(id("netherite_beacon"), () -> BlockEntityType.Builder.create(NetheriteBeaconBlockEntity::new, NETHERITE_BEACON.get()).build(null));
		}
	}

	private static NetheriteShulkerBoxBlock createShulkerBoxBlock(DyeColor color, AbstractBlock.Settings settings) {
		AbstractBlock.ContextPredicate contextPredicate = (blockState, blockView, blockPos) -> {
			BlockEntity blockEntity = blockView.getBlockEntity(blockPos);
			if (!(blockEntity instanceof NetheriteShulkerBoxBlockEntity)) {
				return true;
			}
			NetheriteShulkerBoxBlockEntity shulkerBoxBlockEntity = (NetheriteShulkerBoxBlockEntity) blockEntity;
			return shulkerBoxBlockEntity.suffocates();
		};
		return new NetheriteShulkerBoxBlock(color, settings.strength(2.0F).dynamicBounds().nonOpaque().suffocates(contextPredicate).blockVision(contextPredicate));
	}

	private static RegistrySupplier<Block> register(String id, Block block) {
		return NetheritePlusMod.REGISTRIES.get().get(Registry.BLOCK_KEY).registerSupplied(id(id), () -> block);
	}

	private static void registerShulkerBoxBlocks() {
		NETHERITE_SHULKER_BOX = register("netherite_shulker_box", createShulkerBoxBlock(null, AbstractBlock.Settings.of(Material.SHULKER_BOX).strength(2f, 1200f)));
		NETHERITE_WHITE_SHULKER_BOX = register("netherite_white_shulker_box", createShulkerBoxBlock(DyeColor.WHITE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.WHITE)));
		NETHERITE_ORANGE_SHULKER_BOX = register("netherite_orange_shulker_box", createShulkerBoxBlock(DyeColor.ORANGE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.ORANGE)));
		NETHERITE_MAGENTA_SHULKER_BOX = register("netherite_magenta_shulker_box", createShulkerBoxBlock(DyeColor.MAGENTA, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.MAGENTA)));
		NETHERITE_LIGHT_BLUE_SHULKER_BOX = register("netherite_light_blue_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.LIGHT_BLUE)));
		NETHERITE_YELLOW_SHULKER_BOX = register("netherite_yellow_shulker_box", createShulkerBoxBlock(DyeColor.YELLOW, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.YELLOW)));
		NETHERITE_LIME_SHULKER_BOX = register("netherite_lime_shulker_box", createShulkerBoxBlock(DyeColor.LIME, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.LIME)));
		NETHERITE_PINK_SHULKER_BOX = register("netherite_pink_shulker_box", createShulkerBoxBlock(DyeColor.PINK, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.PINK)));
		NETHERITE_GRAY_SHULKER_BOX = register("netherite_gray_shulker_box", createShulkerBoxBlock(DyeColor.GRAY, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.GRAY)));
		NETHERITE_LIGHT_GRAY_SHULKER_BOX = register("netherite_light_gray_shulker_box", createShulkerBoxBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.LIGHT_GRAY)));
		NETHERITE_CYAN_SHULKER_BOX = register("netherite_cyan_shulker_box", createShulkerBoxBlock(DyeColor.CYAN, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.CYAN)));
		NETHERITE_PURPLE_SHULKER_BOX = register("netherite_purple_shulker_box", createShulkerBoxBlock(DyeColor.PURPLE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.PURPLE_TERRACOTTA)));
		NETHERITE_BLUE_SHULKER_BOX = register("netherite_blue_shulker_box", createShulkerBoxBlock(DyeColor.BLUE, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.BLUE)));
		NETHERITE_BROWN_SHULKER_BOX = register("netherite_brown_shulker_box", createShulkerBoxBlock(DyeColor.BROWN, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.BROWN)));
		NETHERITE_GREEN_SHULKER_BOX = register("netherite_green_shulker_box", createShulkerBoxBlock(DyeColor.GREEN, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.GREEN)));
		NETHERITE_RED_SHULKER_BOX = register("netherite_red_shulker_box", createShulkerBoxBlock(DyeColor.RED, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.RED)));
		NETHERITE_BLACK_SHULKER_BOX = register("netherite_black_shulker_box", createShulkerBoxBlock(DyeColor.BLACK, AbstractBlock.Settings.of(Material.SHULKER_BOX, MaterialColor.BLACK)));

		NETHERITE_SHULKER_BOX_ENTITY = NetheritePlusMod.REGISTRIES.get().get(Registry.BLOCK_ENTITY_TYPE_KEY).registerSupplied(id("netherite_shulker_box"), () -> BlockEntityType.Builder.create(NetheriteShulkerBoxBlockEntity::new, NETHERITE_SHULKER_BOX.get(), NETHERITE_BLACK_SHULKER_BOX.get(), NETHERITE_BLUE_SHULKER_BOX.get(), NETHERITE_BROWN_SHULKER_BOX.get(), NETHERITE_CYAN_SHULKER_BOX.get(), NETHERITE_GRAY_SHULKER_BOX.get(), NETHERITE_GREEN_SHULKER_BOX.get(), NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), NETHERITE_LIME_SHULKER_BOX.get(), NETHERITE_MAGENTA_SHULKER_BOX.get(), NETHERITE_ORANGE_SHULKER_BOX.get(), NETHERITE_PINK_SHULKER_BOX.get(), NETHERITE_PURPLE_SHULKER_BOX.get(), NETHERITE_RED_SHULKER_BOX.get(), NETHERITE_WHITE_SHULKER_BOX.get(), NETHERITE_YELLOW_SHULKER_BOX.get()).build(null));
	}

}
