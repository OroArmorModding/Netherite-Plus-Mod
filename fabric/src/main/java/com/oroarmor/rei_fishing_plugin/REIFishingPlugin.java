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

package com.oroarmor.rei_fishing_plugin;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.rei_fishing_plugin.categories.FishingCategory;
import com.oroarmor.rei_fishing_plugin.categories.LavaFishingCategory;
import com.oroarmor.rei_fishing_plugin.display.FishingDisplay;
import com.oroarmor.rei_fishing_plugin.display.LavaFishingDisplay;
import com.oroarmor.rei_fishing_plugin.recipes.FishingOutcome;
import com.oroarmor.rei_fishing_plugin.recipes.LavaFishingOutcome;
import me.shedaniel.rei.api.DisplayHelper;
import me.shedaniel.rei.api.EntryRegistry;
import me.shedaniel.rei.api.EntryStack;
import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class REIFishingPlugin implements REIPluginV0 {

	@Override
	public Identifier getPluginIdentifier() {
		return new Identifier("netherite_plus:fishing_plugin");
	}

	@Override
	public void registerPluginCategories(RecipeHelper recipeHelper) {
		recipeHelper.registerCategory(new FishingCategory());
		recipeHelper.registerCategory(new LavaFishingCategory());
	}

	@Override
	public void registerEntries(EntryRegistry entryRegistry) {
	}

	@Override
	public void registerRecipeDisplays(RecipeHelper recipeHelper) {
		recipeHelper.registerDisplay(new FishingDisplay(new FishingOutcome.Fish()));
		recipeHelper.registerDisplay(new FishingDisplay(new FishingOutcome.Treasure()));
		recipeHelper.registerDisplay(new FishingDisplay(new FishingOutcome.Junk()));

		recipeHelper.registerDisplay(new LavaFishingDisplay(new LavaFishingOutcome.Treasure()));
		recipeHelper.registerDisplay(new LavaFishingDisplay(new LavaFishingOutcome.Junk()));
	}

	@Override
	public void registerBounds(DisplayHelper displayHelper) {
	}

	@Override
	public void registerOthers(RecipeHelper recipeHelper) {
		recipeHelper.registerWorkingStations(FishingDisplay.ID, EntryStack.create(Items.FISHING_ROD));
		recipeHelper.removeAutoCraftButton(FishingDisplay.ID);

		recipeHelper.registerWorkingStations(LavaFishingDisplay.ID, EntryStack.create(NetheritePlusItems.NETHERITE_FISHING_ROD.get().asItem()));
		recipeHelper.removeAutoCraftButton(LavaFishingDisplay.ID);
	}

}
