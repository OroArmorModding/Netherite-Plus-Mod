package com.oroarmor.rei_fishing_plugin.recipes;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.google.common.collect.ImmutableList;

public class LavaFishingOutcome {
	public int weight, loottableWeight;
	public ItemStack output;

	public LavaFishingOutcome(ItemStack output, int weight, int loottableWeight) {
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

	public float getNormalChance(LavaFishingType type) {
		if (type == LavaFishingType.TREASURE) {
			return 0;
		}

		return loottableWeight * 1f / 0.6f * weight * 0.01f;
	}

	public enum LavaFishingType {
		JUNK, TREASURE
	}

	public static class Junk extends LavaFishingOutcomeCategory {
		public static final LavaFishingOutcome string = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:string"))), 25, 60);
		public static final LavaFishingOutcome gold_nugget = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:gold_nugget"))), 10, 60);
		public static final LavaFishingOutcome magma_cream = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:magma_cream"))), 15, 60);
		public static final LavaFishingOutcome bone = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:bone"))), 25, 60);
		public static final LavaFishingOutcome rotten_flesh = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:rotten_flesh"))), 25, 60);
		public static final List<LavaFishingOutcome> recipes = ImmutableList.of(string, gold_nugget, magma_cream, bone, rotten_flesh);

		public Junk() {
			super(LavaFishingType.JUNK, recipes);
		}
	}

	public static class Treasure extends LavaFishingOutcomeCategory {
		public static final LavaFishingOutcome gold_ingot = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:gold_ingot"))), 30, 60);
		public static final LavaFishingOutcome saddle = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:saddle"))), 15, 60);
		public static final LavaFishingOutcome golden_helmet = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:golden_helmet"))), 10, 60);
		public static final LavaFishingOutcome golden_chestplate = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:golden_chestplate"))), 10, 60);
		public static final LavaFishingOutcome golden_leggings = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:golden_leggings"))), 10, 60);
		public static final LavaFishingOutcome golden_boots = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:golden_boots"))), 10, 60);
		public static final LavaFishingOutcome golden_sword = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:golden_sword"))), 10, 60);
		public static final LavaFishingOutcome ancient_debris = new LavaFishingOutcome(new ItemStack(Registry.ITEM.get(new Identifier("minecraft:ancient_debris"))), 5, 60);
		public static final List<LavaFishingOutcome> recipes = ImmutableList.of(gold_ingot, saddle, golden_helmet, golden_chestplate, golden_leggings, golden_boots, golden_sword, ancient_debris);

		public Treasure() {
			super(LavaFishingType.TREASURE, recipes);
		}
	}

	public static class LavaFishingOutcomeCategory {

		private final List<LavaFishingOutcome> outcomes;
		private final LavaFishingType type;

		public LavaFishingOutcomeCategory(LavaFishingType type, List<LavaFishingOutcome> outcomes) {
			this.type = type;
			this.outcomes = outcomes;
		}

		public List<LavaFishingOutcome> getOutcomes() {
			return outcomes;
		}

		public LavaFishingType getType() {
			return type;
		}

	}
}
