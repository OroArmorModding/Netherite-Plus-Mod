package com.oroarmor.netherite_plus.recipe;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.util.init.Initable;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.REGISTRIES;
import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public final class NetheritePlusRecipeSerializer implements Initable {
	public static RegistrySupplier<SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe>> NETHERITE_SHULKER_BOX;
	public static RegistrySupplier<SpecialRecipeSerializer<NetheriteShieldDecorationRecipe>> NETHERITE_SHIELD;

	static {
		NETHERITE_SHULKER_BOX = NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue() ? register("crafting_special_netheriteshulkerboxcoloring", new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new)) : null;
		NETHERITE_SHIELD = NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue() ? register("crafting_special_netheriteshielddecoration", new SpecialRecipeSerializer<>(NetheriteShieldDecorationRecipe::new)) : null;
	}

	public static void init() {
	}

	@SuppressWarnings("unchecked")
	public static <S extends RecipeSerializer<T>, T extends Recipe<?>> RegistrySupplier<S> register(String id, S serializer) {
		return (RegistrySupplier<S>) REGISTRIES.get().get(Registry.RECIPE_SERIALIZER_KEY).register(id(id), () -> serializer);
	}

}
