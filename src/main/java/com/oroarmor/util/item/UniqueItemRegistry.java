package com.oroarmor.util.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public final class UniqueItemRegistry {

	public static UniqueItemRegistry ELYTRA = new UniqueItemRegistry(Items.ELYTRA), FISHING_ROD = new UniqueItemRegistry(Items.FISHING_ROD), SHIELD = new UniqueItemRegistry(Items.SHIELD), BOW = new UniqueItemRegistry(Items.BOW), CROSSBOW = new UniqueItemRegistry(Items.CROSSBOW), HORSE_ARMOR = new UniqueItemRegistry(Items.DIAMOND_HORSE_ARMOR), TRIDENT = new UniqueItemRegistry(Items.TRIDENT);
	private final List<Item> itemList;

	private final Item defaultItem;

	private UniqueItemRegistry(Item defaultItem) {
		this.defaultItem = defaultItem;
		itemList = new ArrayList<>();
	}

	public void addItemToRegistry(Item item) {
		itemList.add(item);
	}

	public Item getDefaultItem(Item item) {
		if (isItemInRegistry(item)) {
			return defaultItem;
		}
		return item;
	}

	public boolean isItemInRegistry(Item item) {
		return itemList.contains(item);
	}
}
