package com.oroarmor.netherite_plus.item;

import com.oroarmor.netherite_plus.NetheritePlusConfigManager;
import com.oroarmor.netherite_plus.block.NetheritePlusModBlocks;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class NetheritePlusModItems {

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

	public static Item NETHERITE_BEACON = register(new BlockItem(NetheritePlusModBlocks.NETHERITE_BEACON,
			new Item.Settings().maxCount(64).group(ItemGroup.MISC).fireproof()));

	static {
		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_SHULKER_BOXES.getBooleanValue()) {
			registerShulkerBoxes();
		}

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_ELYTRA.getBooleanValue()) {
			registerElytra();
		}

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_SHIELDS.getBooleanValue()) {
			registerShield();
		}

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_BOWS_AND_CROSSBOWS.getBooleanValue()) {
			registerBowAndCrossbow();
		}

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_FISHING_ROD.getBooleanValue()) {
			registerFishingRod();
		}
	}

	private static void registerFishingRod() {
		NETHERITE_FISHING_ROD = register(new Identifier("netherite_plus", "netherite_fishing_rod"),
				new NetheriteFishingRodItem(new Item.Settings().maxDamage(128).group(ItemGroup.TOOLS).fireproof()));

		UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD);
	}

	private static void registerBowAndCrossbow() {
		NETHERITE_BOW = register(new Identifier("netherite_plus", "netherite_bow"),
				new NetheriteBowItem(new Item.Settings().maxDamage(768).group(ItemGroup.COMBAT).fireproof()));

		NETHERITE_CROSSBOW = register(new Identifier("netherite_plus", "netherite_crossbow"),
				new NetheriteCrossbowItem(new Item.Settings().maxDamage(652).group(ItemGroup.COMBAT).fireproof()));

		UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW);
		UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW);
	}

	private static void registerShield() {
		NETHERITE_SHIELD = register(new Identifier("netherite_plus", "netherite_shield"),
				new NetheriteShieldItem(new Item.Settings().maxDamage(672).group(ItemGroup.COMBAT).fireproof()));

		UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD);
	}

	private static void registerElytra() {
		NETHERITE_ELYTRA = register(new Identifier("netherite_plus", "netherite_elytra"),
				new NetheriteElytraItem(new Item.Settings().maxDamage(864).group(ItemGroup.TRANSPORTATION)
						.rarity(Rarity.UNCOMMON).fireproof()));

		UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA);
	}

	public static void registerItems() {
	}

	private static void registerShulkerBoxes() {
		NETHERITE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_WHITE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_WHITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_ORANGE_SHULKER_BOX = register(new BlockItem(NetheritePlusModBlocks.NETHERITE_ORANGE_SHULKER_BOX,
				NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_MAGENTA_SHULKER_BOX = register(new BlockItem(NetheritePlusModBlocks.NETHERITE_MAGENTA_SHULKER_BOX,
				NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(new BlockItem(
				NetheritePlusModBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_YELLOW_SHULKER_BOX = register(new BlockItem(NetheritePlusModBlocks.NETHERITE_YELLOW_SHULKER_BOX,
				NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_LIME_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_LIME_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_PINK_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_GRAY_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(new BlockItem(
				NetheritePlusModBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_CYAN_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_CYAN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_PURPLE_SHULKER_BOX = register(new BlockItem(NetheritePlusModBlocks.NETHERITE_PURPLE_SHULKER_BOX,
				NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_BLUE_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_BROWN_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_BROWN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_GREEN_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_GREEN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_RED_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_RED_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
		NETHERITE_BLACK_SHULKER_BOX = register(
				new BlockItem(NetheritePlusModBlocks.NETHERITE_BLACK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	}

	public static void registerItemsWithModelProvider() {

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_ELYTRA.getBooleanValue())
			registerElytraModels();

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_SHIELDS.getBooleanValue())
			registerShieldModels();

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_FISHING_ROD.getBooleanValue())
			registerFishingRodModels();

		if (NetheritePlusConfigManager.NetheritePlusConfiguration.ENABLED_BOWS_AND_CROSSBOWS.getBooleanValue()) {
			registerBowModels();
			registerCrossbowModels();
		}
	}

	private static void registerElytraModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_ELYTRA, new Identifier("broken"),
				(itemStack, clientWorld, livingEntity) -> {
					return ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F;
				});
	}

	private static void registerShieldModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_SHIELD, new Identifier("blocking"),
				(itemStack, clientWorld, livingEntity) -> {
					return livingEntity != null && livingEntity.isUsingItem()
							&& livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
				});
	}

	private static void registerFishingRodModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_FISHING_ROD, new Identifier("cast"),
				(itemStack, clientWorld, livingEntity) -> {
					if (livingEntity == null) {
						return 0.0F;
					}
					boolean bl = livingEntity.getMainHandStack() == itemStack;
					boolean bl2 = livingEntity.getOffHandStack() == itemStack;
					if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) {
						bl2 = false;
					}

					return (bl || bl2) && livingEntity instanceof PlayerEntity
							&& ((PlayerEntity) livingEntity).fishHook != null ? 1.0F : 0.0F;
				});
	}

	private static void registerBowModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pull"),
				(itemStack, clientWorld, livingEntity) -> {
					if (livingEntity == null) {
						return 0.0F;
					}
					return livingEntity.getActiveItem() != itemStack ? 0.0F
							: (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
				});

		FabricModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pulling"),
				(itemStack, clientWorld, livingEntity) -> {
					if (livingEntity == null) {
						return 0.0F;
					}
					return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
				});
	}

	private static void registerCrossbowModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pull"),
				(itemStack4, clientWorld3, livingEntity2) -> {
					if (livingEntity2 == null) {
						return 0.0F;
					}
					return NetheriteCrossbowItem.isCharged(itemStack4) ? 0.0F
							: (float) (itemStack4.getMaxUseTime() - livingEntity2.getItemUseTimeLeft())
									/ (float) NetheriteCrossbowItem.getPullTime(itemStack4);
				});
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pulling"),
				(itemStack3, clientWorld1, livingEntity1) -> {
					if (livingEntity1 == null) {
						return 0.0F;
					}
					return livingEntity1.isUsingItem() && livingEntity1.getActiveItem() == itemStack3
							&& !NetheriteCrossbowItem.isCharged(itemStack3) ? 1.0F : 0.0F;
				});
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("charged"),
				(itemStack2, clientWorld4, livingEntity3) -> {
					if (livingEntity3 == null) {
						return 0.0F;
					}
					return NetheriteCrossbowItem.isCharged(itemStack2) ? 1.0F : 0.0F;
				});

		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("firework"),
				(itemStack1, clientWorld2, livingEntity4) -> {
					if (livingEntity4 == null) {
						return 0.0F;
					}
					return NetheriteCrossbowItem.isCharged(itemStack1)
							&& NetheriteCrossbowItem.hasProjectile(itemStack1, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
				});
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
