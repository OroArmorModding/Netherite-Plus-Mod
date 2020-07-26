package com.oroarmor.netherite_plus.recipe;

import com.oroarmor.netherite_plus.item.NetheritePlusModItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class NetheriteShieldDecorationRecipe extends SpecialCraftingRecipe {
	public NetheriteShieldDecorationRecipe(Identifier identifier) {
		super(identifier);
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
					if (itemStack3.getItem() != NetheritePlusModItems.NETHERITE_SHIELD) {
						return false;
					}

					if (!itemStack.isEmpty()) {
						return false;
					}

					if (itemStack3.getSubTag("BlockEntityTag") != null) {
						return false;
					}

					itemStack = itemStack3;
				}
			}
		}

		if (!itemStack.isEmpty() && !itemStack2.isEmpty()) {
			return true;
		}
		return false;

	}

	@Override
	public ItemStack craft(CraftingInventory craftingInventory) {
		ItemStack itemStack = ItemStack.EMPTY;
		ItemStack itemStack2 = ItemStack.EMPTY;

		for (int i = 0; i < craftingInventory.size(); ++i) {
			ItemStack itemStack3 = craftingInventory.getStack(i);
			if (!itemStack3.isEmpty()) {
				if (itemStack3.getItem() instanceof BannerItem) {
					itemStack = itemStack3;
				} else if (itemStack3.getItem() == NetheritePlusModItems.NETHERITE_SHIELD) {
					itemStack2 = itemStack3.copy();
				}
			}
		}

		if (itemStack2.isEmpty()) {
			return itemStack2;
		}
		CompoundTag compoundTag = itemStack.getSubTag("BlockEntityTag");
		CompoundTag compoundTag2 = compoundTag == null ? new CompoundTag() : compoundTag.copy();
		compoundTag2.putInt("Base", ((BannerItem) itemStack.getItem()).getColor().getId());
		itemStack2.putSubTag("BlockEntityTag", compoundTag2);
		return itemStack2;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeSerializer.SHIELD_DECORATION;
	}
}
