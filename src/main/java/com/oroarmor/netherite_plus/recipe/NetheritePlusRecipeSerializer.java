package com.oroarmor.netherite_plus.recipe;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;

public final class NetheritePlusRecipeSerializer {
	public static SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe> NETHERITE_SHULKER_BOX;
	public static SpecialRecipeSerializer<NetheriteShieldDecorationRecipe> NETHERITE_SHIELD;

	public static void init() {
		NETHERITE_SHULKER_BOX = NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue() ? register("crafting_special_netheriteshulkerboxcoloring", new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new)) : null;

		NETHERITE_SHIELD = NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue() ? register("crafting_special_netheriteshielddecoration", new SpecialRecipeSerializer<>(NetheriteShieldDecorationRecipe::new)) : null;
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, id(id), serializer);
	}

}
