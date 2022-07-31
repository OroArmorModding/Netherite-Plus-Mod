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

package com.oroarmor.rei_fishing_plugin.recipes;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FishingOutcome {
	public int weight, loottableWeight;
	public ItemStack output;

	public FishingOutcome(ItemStack output, int weight, int loottableWeight) {
		this.weight = weight;
		this.loottableWeight = loottableWeight;
		this.output = output;
	}

	public int getWeight() {
		return weight;
	}

	public int getLoottableWeight() {
		return loottableWeight;
	}

	public ItemStack getOutput() {
		return output;
	}

	public float getOpenWaterChance() {
		return loottableWeight * weight * 0.01f;
	}

	public float getNormalChance(FishingType type) {
		if (type == FishingType.TREASURE) {
			return 0;
		}

		return loottableWeight * 1f / 0.95f * weight * 0.01f;
	}

	public enum FishingType {
		JUNK, FISH, TREASURE
	}

	public static class Junk extends FishingOutcomeCategory {
		public static final FishingOutcome lily_pad = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:lily_pad"))), 17, 10);
		public static final FishingOutcome leather_boots = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:leather_boots"))), 10, 10);
		public static final FishingOutcome leather = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:leather"))), 10, 10);
		public static final FishingOutcome bone = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:bone"))), 10, 10);
		public static final FishingOutcome string = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:string"))), 5, 10);
		public static final FishingOutcome fishing_rod = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:fishing_rod"))), 2, 10);
		public static final FishingOutcome bowl = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:bowl"))), 10, 10);
		public static final FishingOutcome stick = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:stick"))), 5, 10);
		public static final FishingOutcome tripwire_hook = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:tripwire_hook"))), 10, 10);
		public static final FishingOutcome rotten_flesh = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:rotten_flesh"))), 10, 10);
		public static final FishingOutcome bamboo = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:bamboo"))), 10, 10);
		public static final FishingOutcome ink_sac = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:ink_sac")), 10), 1, 10);
		public static final FishingOutcome water_bottle;
		public static final List<FishingOutcome> recipes;

		static {
			ItemStack water_bottle_item = new ItemStack(Registry.ITEM.get(new Identifier("minecraft:potion")));
			water_bottle_item.setTag(new NbtCompound(ImmutableMap.of("Potion", StringTag.of("minecraft:water"))));
			water_bottle = new FishingOutcome(water_bottle_item, 10, 10);

			recipes = ImmutableList.of(lily_pad, leather_boots, leather, bone, string, fishing_rod, bowl, stick, tripwire_hook, rotten_flesh, bamboo, water_bottle, ink_sac);
		}

		public Junk() {
			super(FishingType.JUNK, recipes);
		}
	}

	public static class Fish extends FishingOutcomeCategory {
		public static final FishingOutcome cod = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:cod"))), 60, 85);
		public static final FishingOutcome salmon = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:salmon"))), 25, 85);
		public static final FishingOutcome tropical_fish = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:tropical_fish"))), 2, 85);
		public static final FishingOutcome pufferfish = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:pufferfish"))), 13, 85);
		public static final List<FishingOutcome> recipes = ImmutableList.of(cod, salmon, tropical_fish, pufferfish);

		public Fish() {
			super(FishingType.FISH, recipes);
		}
	}

	public static class Treasure extends FishingOutcomeCategory {
		public static final FishingOutcome name_tag = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:name_tag"))), 16, 5);
		public static final FishingOutcome saddle = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:saddle"))), 16, 5);
		public static final FishingOutcome bow = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:bow"))), 16, 5);
		public static final FishingOutcome fishing_rod = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:fishing_rod"))), 16, 5);
		public static final FishingOutcome book = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:book"))), 16, 5);
		public static final FishingOutcome nautilus_shell = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:nautilus_shell"))), 16, 5);
		public static final List<FishingOutcome> recipes = ImmutableList.of(name_tag, saddle, bow, fishing_rod, book, nautilus_shell);

		public Treasure() {
			super(FishingType.TREASURE, recipes);
		}
	}

	public static abstract class FishingOutcomeCategory {

		private final List<FishingOutcome> outcomes;
		private final FishingType type;

		public FishingOutcomeCategory(FishingType type, List<FishingOutcome> outcomes) {
			this.type = type;
			this.outcomes = outcomes;
		}

		public List<FishingOutcome> getOutcomes() {
			return outcomes;
		}

		public FishingType getType() {
			return type;
		}

	}
}
