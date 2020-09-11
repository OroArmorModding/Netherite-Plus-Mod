package com.oroarmor.rei_fishing_plugin.recipes;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class FishingOutcome {
	public enum FishingType {
		JUNK, FISH, TREASURE
	}

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

	public static class Junk extends FishingOutcomeCategory {
		public Junk() {
			super(FishingType.JUNK, recipes);
		}

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
			water_bottle_item.setTag(new CompoundTag(ImmutableMap.of("Potion", StringTag.of("minecraft:water"))));
			water_bottle = new FishingOutcome(water_bottle_item, 10, 10);

			recipes = ImmutableList.of(lily_pad, leather_boots, leather, bone, string, fishing_rod, bowl, stick, tripwire_hook, rotten_flesh, bamboo, water_bottle, ink_sac);
		}
	}

	public static class Fish extends FishingOutcomeCategory {
		public Fish() {
			super(FishingType.FISH, recipes);
		}

		public static final FishingOutcome cod = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:cod"))), 60, 85);
		public static final FishingOutcome salmon = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:salmon"))), 25, 85);
		public static final FishingOutcome tropical_fish = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:tropical_fish"))), 2, 85);
		public static final FishingOutcome pufferfish = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:pufferfish"))), 13, 85);

		public static final List<FishingOutcome> recipes = ImmutableList.of(cod, salmon, tropical_fish, pufferfish);
	}

	public static class Treasure extends FishingOutcomeCategory {
		public Treasure() {
			super(FishingType.TREASURE, recipes);
		}

		public static final FishingOutcome name_tag = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:name_tag"))), 16, 5);
		public static final FishingOutcome saddle = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:saddle"))), 16, 5);
		public static final FishingOutcome bow = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:bow"))), 16, 5);
		public static final FishingOutcome fishing_rod = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:fishing_rod"))), 16, 5);
		public static final FishingOutcome book = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:book"))), 16, 5);
		public static final FishingOutcome nautilus_shell = new FishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:nautilus_shell"))), 16, 5);

		public static final List<FishingOutcome> recipes = ImmutableList.of(name_tag, saddle, bow, fishing_rod, book, nautilus_shell);
	}

	public static class FishingOutcomeCategory {

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

	public float getOpenWaterChance() {
		return loottableWeight * weight * 0.01f;
	}

	public float getNormalChance(FishingType type) {
		if (type == FishingType.TREASURE) {
			return 0;
		}

		return loottableWeight * 1f / 0.95f * weight * 0.01f;
	}
}
