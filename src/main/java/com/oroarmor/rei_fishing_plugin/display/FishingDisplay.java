package com.oroarmor.rei_fishing_plugin.display;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.oroarmor.rei_fishing_plugin.recipes.FishingOutcome;
import com.oroarmor.rei_fishing_plugin.recipes.FishingOutcome.FishingOutcomeCategory;

import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.EntryStack.Settings;
import me.shedaniel.rei.api.RecipeDisplay;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class FishingDisplay implements RecipeDisplay {

	private static final DecimalFormat format = new DecimalFormat("#.##");

	public static final Identifier ID = new Identifier("netherite_plus:fishing_category");

	private FishingOutcomeCategory outcomes;

	public FishingDisplay(FishingOutcomeCategory outcomes) {
		this.outcomes = outcomes;
	}

	@Override
	public List<List<EntryStack>> getInputEntries() {
		return ImmutableList.of(outcomes.getOutcomes().stream().map(FishingOutcome::getOutput).map(EntryStack::create).map(this::generateEntrySettings).collect(Collectors.toList()));
	}

	@Override
	public Identifier getRecipeCategory() {
		return ID;
	}

	private EntryStack generateEntrySettings(EntryStack stack) {
		FishingOutcome outcome = outcomes.getOutcomes().stream().filter(o -> o.getOutput() == stack.getItemStack()).findFirst().get();

		return stack.addSetting(Settings.TOOLTIP_ENABLED, () -> true).addSetting(Settings.TOOLTIP_APPEND_EXTRA, (entry) -> ImmutableList.of(new LiteralText("Open water: " + format.format(outcome.getOpenWaterChance()) + "%"), new LiteralText("Normal: " + format.format(outcome.getNormalChance(outcomes.getType())) + "%")));

	}

	public List<EntryStack> getEntries() {
		return outcomes.getOutcomes().stream().map(FishingOutcome::getOutput).map(EntryStack::create).map(this::generateEntrySettings).collect(Collectors.toList());
	}

	public FishingOutcomeCategory getOutcomes() {
		return outcomes;
	}
}
