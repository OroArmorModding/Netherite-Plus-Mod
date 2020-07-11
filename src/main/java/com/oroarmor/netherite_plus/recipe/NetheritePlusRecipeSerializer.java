package com.oroarmor.netherite_plus.recipe;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NetheritePlusRecipeSerializer {
	public static final SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe> NETHERITE_SHULKER_BOX = register(
			"crafting_special_netheriteshulkerboxcoloring",
			new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new));

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("netherite_plus", id), serializer);
	}

	public static void init() {

	}

}
