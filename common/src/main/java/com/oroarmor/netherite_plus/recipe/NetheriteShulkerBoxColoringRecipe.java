package com.oroarmor.netherite_plus.recipe;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class NetheriteShulkerBoxColoringRecipe extends CustomRecipe {
	public NetheriteShulkerBoxColoringRecipe(ResourceLocation identifier) {
		super(identifier);
	}

	@Override
	public ItemStack assemble(CraftingContainer craftingInventory) {
		ItemStack itemStack = ItemStack.EMPTY;
		DyeItem dyeItem = (DyeItem) Items.WHITE_DYE;

		for (int i = 0; i < craftingInventory.getContainerSize(); ++i) {
			ItemStack itemStack2 = craftingInventory.getItem(i);
			if (!itemStack2.isEmpty()) {
				Item item = itemStack2.getItem();
				if (Block.byItem(item) instanceof NetheriteShulkerBoxBlock) {
					itemStack = itemStack2;
				} else if (item instanceof DyeItem) {
					dyeItem = (DyeItem) item;
				}
			}
		}

		ItemStack itemStack3 = NetheriteShulkerBoxBlock.getItemStack(dyeItem.getDyeColor());
		if (itemStack.hasTag()) {
			itemStack3.setTag(itemStack.getTag().copy());
		}

		return itemStack3;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return RecipeSerializer.SHULKER_BOX_COLORING;
	}

	@Override
	public boolean matches(CraftingContainer craftingInventory, Level world) {
		int i = 0;
		int j = 0;

		for (int k = 0; k < craftingInventory.getContainerSize(); ++k) {
			ItemStack itemStack = craftingInventory.getItem(k);
			if (!itemStack.isEmpty()) {
				if (Block.byItem(itemStack.getItem()) instanceof NetheriteShulkerBoxBlock) {
					++i;
				} else {
					if (!(itemStack.getItem() instanceof DyeItem)) {
						return false;
					}

					++j;
				}

				if (j > 1 || i > 1) {
					return false;
				}
			}
		}

		return i == 1 && j == 1;
	}
}
