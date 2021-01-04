package com.oroarmor.multi_item_lib;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class UniqueItemRegistry {
    public static UniqueItemRegistry ELYTRA = new UniqueItemRegistry(Items.ELYTRA);
    public static UniqueItemRegistry FISHING_ROD = new UniqueItemRegistry(Items.FISHING_ROD);
    public static UniqueItemRegistry SHIELD = new UniqueItemRegistry(Items.SHIELD);
    public static UniqueItemRegistry BOW = new UniqueItemRegistry(Items.BOW);
    public static UniqueItemRegistry CROSSBOW = new UniqueItemRegistry(Items.CROSSBOW);
    public static UniqueItemRegistry TRIDENT = new UniqueItemRegistry(Items.TRIDENT);

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
