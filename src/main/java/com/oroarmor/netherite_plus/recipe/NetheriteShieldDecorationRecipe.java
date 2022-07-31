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

package com.oroarmor.netherite_plus.recipe;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteShieldDecorationRecipe extends SpecialCraftingRecipe {
    public NetheriteShieldDecorationRecipe(Identifier identifier) {
        super(identifier);
    }

    @Override
    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack craftingStack = craftingInventory.getStack(i);
            if (!craftingStack.isEmpty()) {
                if (craftingStack.getItem() instanceof BannerItem) {
                    itemStack = craftingStack;
                } else if (craftingStack.getItem() == NetheritePlusItems.NETHERITE_SHIELD) {
                    itemStack2 = craftingStack.copy();
                }
            }
        }

        if (itemStack2.isEmpty()) {
            return itemStack2;
        }
        NbtCompound compoundTag = itemStack.getSubNbt("BlockEntityTag");
        NbtCompound compoundTag2 = compoundTag == null ? new NbtCompound() : compoundTag.copy();
        compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
        itemStack2.setSubNbt("BlockEntityTag", compoundTag2);
        return itemStack2;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.SHIELD_DECORATION;
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        ItemStack itemStack = ItemStack.EMPTY;
        ItemStack itemStack2 = ItemStack.EMPTY;

        for (int i = 0; i < craftingInventory.size(); ++i) {
            ItemStack itemStack3 = craftingInventory.getStack(i);
            if (!itemStack3.isEmpty()) {
                if (itemStack3.getItem() instanceof BannerItem) {
                    if (!itemStack2.isEmpty()) {
                        return false;
                    }

                    itemStack2 = itemStack3;
                } else {
                    if (itemStack3.getItem() != NetheritePlusItems.NETHERITE_SHIELD) {
                        return false;
                    }

                    if (!itemStack.isEmpty()) {
                        return false;
                    }

                    if (itemStack3.getSubNbt("BlockEntityTag") != null) {
                        return false;
                    }

                    itemStack = itemStack3;
                }
            }
        }

        return !itemStack.isEmpty() && !itemStack2.isEmpty();
    }
}
