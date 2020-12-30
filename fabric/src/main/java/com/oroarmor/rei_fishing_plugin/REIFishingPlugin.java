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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class REIFishingPlugin implements REIPluginV0 {

	@Override
	public ResourceLocation getPluginIdentifier() {
		return new ResourceLocation("netherite_plus:fishing_plugin");
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
