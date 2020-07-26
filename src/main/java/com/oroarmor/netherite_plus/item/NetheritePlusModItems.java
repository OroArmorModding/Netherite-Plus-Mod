package com.oroarmor.netherite_plus.item;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.Maps;
import com.oroarmor.netherite_plus.block.NetheritePlusModBlocks;
import com.oroarmor.util.item.UniqueItemRegistry;

import net.minecraft.block.Block;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
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

	public static final Item NETHERITE_ELYTRA = register(new Identifier("netherite_plus", "netherite_elytra"),
			new NetheriteElytraItem(new Item.Settings().maxDamage(864).group(ItemGroup.TRANSPORTATION)
					.rarity(Rarity.UNCOMMON).fireproof()));

	public static final Item NETHERITE_FISHING_ROD = register(new Identifier("netherite_plus", "netherite_fishing_rod"),
			new NetheriteFishingRodItem(new Item.Settings().maxDamage(128).group(ItemGroup.TOOLS).fireproof()));

	public static final Item NETHERITE_SHIELD = register(new Identifier("netherite_plus", "netherite_shield"),
			new NetheriteShieldItem(new Item.Settings().maxDamage(672).group(ItemGroup.COMBAT).fireproof()));

	public static final Item.Settings NETHERITE_SHULKER_BOX_ITEM_SETTINGS = new Item.Settings().maxCount(1)
			.group(ItemGroup.DECORATIONS).fireproof();

	public static final Item NETHERITE_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_WHITE_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_WHITE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_ORANGE_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_ORANGE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_MAGENTA_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_MAGENTA_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(new BlockItem(
			NetheritePlusModBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_YELLOW_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_YELLOW_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_LIME_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_LIME_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_PINK_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_PINK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_GRAY_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(new BlockItem(
			NetheritePlusModBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_CYAN_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_CYAN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_PURPLE_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_PURPLE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_BLUE_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_BLUE_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_BROWN_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_BROWN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_GREEN_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_GREEN_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_RED_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_RED_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
	public static final Item NETHERITE_BLACK_SHULKER_BOX = register(
			new BlockItem(NetheritePlusModBlocks.NETHERITE_BLACK_SHULKER_BOX, NETHERITE_SHULKER_BOX_ITEM_SETTINGS));

	static {
		UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA);
		UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD);
		UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD);
	}

	public static void registerItems() {
	}

	public static void registerItemsAsDamagable() {

		try {
			Field f = ModelPredicateProviderRegistry.class.getDeclaredField("ITEM_SPECIFIC");
			f.setAccessible(true);
			registerElytraAsDamageable(f);
			registerFishingRodAsDamageable(f);
			// TODO get this working
			registerShieldAsDamageable(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void registerElytraAsDamageable(Field f) throws IllegalAccessException {
		((Map<Item, Map<Identifier, ModelPredicateProvider>>) f.get(new Object()))
				.computeIfAbsent(NetheritePlusModItems.NETHERITE_ELYTRA, (itemx) -> {
					return Maps.newHashMap();
				}).put(new Identifier("broken"), (itemStack, clientWorld, livingEntity) -> {
					return ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F;
				});
	}

	@SuppressWarnings("unchecked")
	public static void registerShieldAsDamageable(Field f) throws IllegalAccessException {
		((Map<Item, Map<Identifier, ModelPredicateProvider>>) f.get(new Object()))
				.computeIfAbsent(NETHERITE_SHIELD, (itemx) -> {
					return Maps.newHashMap();
				}).put(new Identifier("blocking"), (itemStack, clientWorld, livingEntity) -> {
					return livingEntity != null && livingEntity.isUsingItem()
							&& livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
				});
	}

	@SuppressWarnings("unchecked")
	public static void registerFishingRodAsDamageable(Field f) throws IllegalAccessException {
		((Map<Item, Map<Identifier, ModelPredicateProvider>>) f.get(new Object()))
				.computeIfAbsent(NetheritePlusModItems.NETHERITE_FISHING_ROD, (itemx) -> {
					return Maps.newHashMap();
				}).put(new Identifier("cast"), (itemStack, clientWorld, livingEntity) -> {
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
