/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.data.recipes;

import java.util.concurrent.CompletableFuture;

import com.oroarmor.netherite_plus.tags.NetheritePlusItemTags;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.recipes.TransmuteRecipeBuilder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShieldDecorationRecipe;
import net.minecraft.world.level.block.ColorCollection;

public class NetheritePlusRecipeProvider extends FabricRecipeProvider {
    public NetheritePlusRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
        return new RecipeProvider(recipes, advancements) {
            @Override
            public void buildRecipes() {
                this.shaped(RecipeCategory.DECORATIONS, NetheritePlusItems.FAKE_NETHERITE_BLOCK, 8)
                        .pattern("###")
                        .pattern("#I#")
                        .pattern("###")
                        .define('#', Items.IRON_BLOCK)
                        .define('I', Items.NETHERITE_INGOT)
                        .unlockedBy("has_netherite_ingot", this.has(Items.NETHERITE_INGOT))
                        .save(this.output);

                this.shaped(RecipeCategory.DECORATIONS, NetheritePlusItems.NETHERITE_ANVIL)
                        .pattern("###")
                        .pattern(" I ")
                        .pattern("III")
                        .define('#', Items.NETHERITE_BLOCK)
                        .define('I', Items.NETHERITE_INGOT)
                        .unlockedBy("has_netherite_ingot", this.has(Items.NETHERITE_INGOT))
                        .save(this.output);

                this.shaped(RecipeCategory.DECORATIONS, NetheritePlusItems.NETHERITE_BEACON)
                        .pattern("III")
                        .pattern("IBI")
                        .pattern("###")
                        .define('#', Items.NETHERITE_BLOCK)
                        .define('I', Items.NETHERITE_INGOT)
                        .define('B', Items.BEACON)
                        .unlockedBy("has_netherite_ingot", this.has(Items.NETHERITE_INGOT))
                        .save(this.output);

                this.netheriteSmithing(Items.SHULKER_BOX, RecipeCategory.DECORATIONS, NetheritePlusItems.NETHERITE_SHULKER_BOX);
                ColorCollection.zipApply(Items.DYED_SHULKER_BOX, NetheritePlusItems.DYED_NETHERITE_SHULKER_BOX,
                        (shulker, netheriteShulker) -> this.netheriteSmithing(shulker, RecipeCategory.DECORATIONS, netheriteShulker));

                this.netheriteSmithing(Items.BOW, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_BOW);
                this.netheriteSmithing(Items.CROSSBOW, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_CROSSBOW);
                this.netheriteSmithing(Items.ELYTRA, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_ELYTRA);
                this.netheriteSmithing(Items.FISHING_ROD, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_FISHING_ROD);
                this.netheriteSmithing(Items.SHEARS, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_SHEARS);
                this.netheriteSmithing(Items.SHIELD, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_SHIELD);
                this.netheriteSmithing(Items.TRIDENT, RecipeCategory.TOOLS, NetheritePlusItems.NETHERITE_TRIDENT);

                ColorCollection.zipApply(Items.DYE, NetheritePlusItems.DYED_NETHERITE_SHULKER_BOX, (dye, box) ->
                        TransmuteRecipeBuilder.transmute(RecipeCategory.DECORATIONS, this.tag(NetheritePlusItemTags.NETHERITE_SHULKER_BOXES), Ingredient.of(dye), box)
                                .group("netherite_shulker_box_dye")
                                .unlockedBy("has_shulker_box", this.has(NetheritePlusItemTags.NETHERITE_SHULKER_BOXES))
                                .save(this.output)
                );

                SpecialRecipeBuilder.special(() ->
                                new ShieldDecorationRecipe(
                                        this.tag(ItemTags.BANNERS),
                                        Ingredient.of(NetheritePlusItems.NETHERITE_SHIELD),
                                        new ItemStackTemplate(NetheritePlusItems.NETHERITE_SHIELD)
                                )
                        )
                        .save(this.output, "netherite_shield_decoration");
            }
        };
    }
}
