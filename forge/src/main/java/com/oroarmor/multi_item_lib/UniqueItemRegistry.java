/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

package com.oroarmor.multi_item_lib;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

public final class UniqueItemRegistry {
	public static UniqueItemRegistry ELYTRA = new UniqueItemRegistry(Items.ELYTRA);
	public static UniqueItemRegistry FISHING_ROD = new UniqueItemRegistry(Items.FISHING_ROD);
	public static UniqueItemRegistry SHIELD = new UniqueItemRegistry(Items.SHIELD);
	public static UniqueItemRegistry BOW = new UniqueItemRegistry(Items.BOW);
	public static UniqueItemRegistry CROSSBOW = new UniqueItemRegistry(Items.CROSSBOW);
	public static UniqueItemRegistry TRIDENT = new UniqueItemRegistry(Items.TRIDENT);
	public static UniqueItemRegistry SHEARS = new UniqueItemRegistry(Items.SHEARS);

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
