package com.oroarmor.netherite_plus.recipe;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.init.Initable;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;

public final class NetheritePlusRecipeSerializer implements Initable {
	public static SimpleRecipeSerializer<NetheriteShulkerBoxColoringRecipe> NETHERITE_SHULKER_BOX;
	public static SimpleRecipeSerializer<NetheriteShieldDecorationRecipe> NETHERITE_SHIELD;

	static {
		NETHERITE_SHULKER_BOX = NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue() ? register("crafting_special_netheriteshulkerboxcoloring", new SimpleRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new)) : null;
		NETHERITE_SHIELD = NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue() ? register("crafting_special_netheriteshielddecoration", new SimpleRecipeSerializer<>(NetheriteShieldDecorationRecipe::new)) : null;
	}

	public static void init() {
	}

	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
		return Registry.register(Registry.RECIPE_SERIALIZER, id(id), serializer);
	}

}
