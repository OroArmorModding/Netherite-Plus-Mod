/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.recipe;

import com.oroarmor.netherite_plus.NetheritePlusMod;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public final class NetheritePlusRecipeSerializer {
    public static SpecialRecipeSerializer<NetheriteShulkerBoxColoringRecipe> NETHERITE_SHULKER_BOX;
    public static SpecialRecipeSerializer<NetheriteShieldDecorationRecipe> NETHERITE_SHIELD;

    static {
        NETHERITE_SHULKER_BOX = NetheritePlusMod.CONFIG.enabled.shulker_boxes.value() ? register("crafting_special_netheriteshulkerboxcoloring", new SpecialRecipeSerializer<>(NetheriteShulkerBoxColoringRecipe::new)) : null;
        NETHERITE_SHIELD = NetheritePlusMod.CONFIG.enabled.shields.value() ? register("crafting_special_netheriteshielddecoration", new SpecialRecipeSerializer<>(NetheriteShieldDecorationRecipe::new)) : null;
    }

    public static void init() {
    }

    public static <S extends RecipeSerializer<T>, T extends Recipe<?>> S register(String id, S serializer) {
        return Registry.register(Registries.RECIPE_SERIALIZER, id(id), serializer);
    }

}
