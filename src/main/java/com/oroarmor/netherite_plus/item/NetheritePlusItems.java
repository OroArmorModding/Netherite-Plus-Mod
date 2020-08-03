package com.oroarmor.netherite_plus.item;

import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.compatibility.NetheritePlusTrinketsCompatibilty;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public final class NetheritePlusItems {

	public static Item NETHERITE_ELYTRA;

	public static Item NETHERITE_FISHING_ROD;

	public static Item NETHERITE_SHIELD;

	public static Item NETHERITE_BOW;

	public static Item NETHERITE_CROSSBOW;

	public static Item.Settings NETHERITE_SHULKER_BOX_ITEM_SETTINGS = new Item.Settings().maxCount(1)
			.group(ItemGroup.DECORATIONS).fireproof();

	public static Item NETHERITE_SHULKER_BOX;
	public static Item NETHERITE_WHITE_SHULKER_BOX;
	public static Item NETHERITE_ORANGE_SHULKER_BOX;
	public static Item NETHERITE_MAGENTA_SHULKER_BOX;
	public static Item NETHERITE_LIGHT_BLUE_SHULKER_BOX;
	public static Item NETHERITE_YELLOW_SHULKER_BOX;
	public static Item NETHERITE_LIME_SHULKER_BOX;
	public static Item NETHERITE_PINK_SHULKER_BOX;
	public static Item NETHERITE_GRAY_SHULKER_BOX;
	public static Item NETHERITE_LIGHT_GRAY_SHULKER_BOX;
	public static Item NETHERITE_CYAN_SHULKER_BOX;
	public static Item NETHERITE_PURPLE_SHULKER_BOX;
	public static Item NETHERITE_BLUE_SHULKER_BOX;
	public static Item NETHERITE_BROWN_SHULKER_BOX;
	public static Item NETHERITE_GREEN_SHULKER_BOX;
	public static Item NETHERITE_RED_SHULKER_BOX;
	public static Item NETHERITE_BLACK_SHULKER_BOX;

//	public static Item NETHERITE_BEACON = register(new BlockItem(NetheritePlusModBlocks.NETHERITE_BEACON,
//			new Item.Settings().maxCount(64).group(ItemGroup.MISC).fireproof()));

	public static Item NETHERITE_HORSE_ARMOR;

	public static Item FAKE_NETHERITE_BLOCK;

	public static Item NETHERITE_ANVIL_ITEM;

	private static void registerFishingRod() {
		NETHERITE_FISHING_ROD = register(new Identifier("netherite_plus", "netherite_fishing_rod"),
				new NetheriteFishingRodItem(new Item.Settings()
						.maxDamage(NetheritePlusConfig.DURABILITIES.FISHING_ROD_DURABILITY.getValue())
						.group(ItemGroup.TOOLS).fireproof()));

		UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD);
	}

	private static void registerHorseArmor() {
		NETHERITE_HORSE_ARMOR = register(new Identifier("netherite_plus", "netherite_horse_armor"),
				new NetheriteHorseArmorItem(15, new Item.Settings().maxCount(1).group(ItemGroup.MISC).fireproof()));

		UniqueItemRegistry.HORSE_ARMOR.addItemToRegistry(NETHERITE_HORSE_ARMOR);
	}

	private static void registerBowAndCrossbow() {
		NETHERITE_BOW = register(new Identifier("netherite_plus", "netherite_bow"),
				new NetheriteBowItem(
						new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.BOW_DURABILITY.getValue())
								.group(ItemGroup.COMBAT).fireproof()));

		NETHERITE_CROSSBOW = register(new Identifier("netherite_plus", "netherite_crossbow"),
				new NetheriteCrossbowItem(
						new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.CROSSBOW_DURABILITY.getValue())
								.group(ItemGroup.COMBAT).fireproof()));

		UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW);
		UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW);
	}

	private static void registerShield() {
		NETHERITE_SHIELD = register(new Identifier("netherite_plus", "netherite_shield"),
				new NetheriteShieldItem(
						new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.SHIELD_DURABILITY.getValue())
								.group(ItemGroup.COMBAT).fireproof()));

		UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD);
	}

	private static void registerElytra() {
		Item.Settings elytraSettings = new Item.Settings()
				.maxDamage(NetheritePlusConfig.DURABILITIES.ELYTRA_DURABILITY.getValue())
				.group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON).fireproof();

		NETHERITE_ELYTRA = register(new Identifier("netherite_plus", "netherite_elytra"),
				!FabricLoader.getInstance().isModLoaded("trinkets") ? new NetheriteElytraItem(elytraSettings)
						: NetheritePlusTrinketsCompatibilty.getTrinketsElytra(elytraSettings));

		UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA);
	}

	public static void registerItems() {
		if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
			registerShulkerBoxes();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue()) {
			registerElytra();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue()) {
			registerShield();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_BOWS_AND_CROSSBOWS.getValue()) {
			registerBowAndCrossbow();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_FISHING_ROD.getValue()) {
			registerFishingRod();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_HORSE_ARMOR.getValue()) {
			registerHorseArmor();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_FAKE_NETHERITE_BLOCKS.getValue()) {
			FAKE_NETHERITE_BLOCK = register(new Identifier("netherite_plus", "fake_netherite_block"),
					new BlockItem(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK,
							new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).fireproof()));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_ANVIL.getValue()) {
			NETHERITE_ANVIL_ITEM = register(new Identifier("netherite_plus", "netherite_anvil"),
					new BlockItem(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK,
							new Item.Settings().group(ItemGroup.DECORATIONS).fireproof()));
		}
	}

	private static void registerShulkerBoxes() {
		NETHERITE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_WHITE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_ORANGE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_MAGENTA_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX,
				NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_YELLOW_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_LIME_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_PINK_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_GRAY_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX,
				NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_CYAN_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_PURPLE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_BLUE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_BROWN_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_GREEN_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_RED_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_BLACK_SHULKER_BOX = register(
				new BlockItem(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	}

	private static Item register(BlockItem item) {
		return register(item.getBlock(), item);
	}

	protected static Item register(Block block, Item item) {
		return register(Registry.BLOCK.getId(block), item);
	}

	private static Item register(Identifier id, Item item) {
		if (item instanceof BlockItem) {
			((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
		}

		return Registry.register(Registry.ITEM, id, item);
	}

}
