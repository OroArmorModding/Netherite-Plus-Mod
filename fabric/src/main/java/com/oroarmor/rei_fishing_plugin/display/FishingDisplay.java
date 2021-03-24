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
	public static final Identifier ID = new Identifier("netherite_plus:fishing_category");
	private static final DecimalFormat format = new DecimalFormat("#.##");
	private final FishingOutcomeCategory outcomes;

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
