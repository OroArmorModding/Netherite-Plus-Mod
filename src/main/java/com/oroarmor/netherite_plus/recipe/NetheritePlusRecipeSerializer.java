package com.oroarmor.netherite_plus.recipe;

import com.oroarmor.netherite_plus.NetheritePlusConfigManager;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class NetheritePlusRecipeSerializer {
	public static SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe> NETHERITE_SHULKER_BOX;
	public static SpecialRecipeSerializer<NetheriteShieldDecorationRecipe> NETHERITE_SHIELD;

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, new Identifier("netherite_plus", id), serializer);
	}

	public static void init() {
		NETHERITE_SHULKER_BOX = NetheritePlusConfigManager.ENABLED.ENABLED_SHULKER_BOXES.getBooleanValue()
				? register("crafting_special_netheriteshulkerboxcoloring",
						new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new))
				: null;

		NETHERITE_SHIELD = NetheritePlusConfigManager.ENABLED.ENABLED_SHIELDS.getBooleanValue()
				? register("crafting_special_netheriteshielddecoration",
						new SpecialRecipeSerializer<>(NetheriteShieldDecorationRecipe::new))
				: null;
	}

}
