package com.oroarmor.util.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

public class UniqueItemRegistry {

	private List<Item> itemList;
	private Item defaultItem;

	public static UniqueItemRegistry ELYTRA = new UniqueItemRegistry(Items.ELYTRA),
			FISHING_ROD = new UniqueItemRegistry(Items.FISHING_ROD),
			SHIELD = new UniqueItemRegistry(Items.SHIELD),
			BOW = new UniqueItemRegistry(Items.BOW),
			CROSSBOW = new UniqueItemRegistry(Items.CROSSBOW);

	private UniqueItemRegistry(Item defaultItem) {
		this.defaultItem = defaultItem;
		this.itemList = new ArrayList<Item>();
	}

	public void addItemToRegistry(Item item) {
		itemList.add(item);
	}

	public boolean isItemInRegistry(Item item) {
		return itemList.contains(item);
	}

	public Item getDefaultItem(Item item) {
		if (isItemInRegistry(item)) {
			return defaultItem;
		}
		return item;
	}
}
