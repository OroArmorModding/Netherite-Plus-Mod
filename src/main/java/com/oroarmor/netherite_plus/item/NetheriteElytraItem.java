package com.oroarmor.netherite_plus.item;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class NetheriteElytraItem extends ArmorItem {

	public static boolean isStackUsableAsElytra(ItemStack itemStack) {
		return UniqueItemRegistry.ELYTRA.isItemInRegistry(itemStack.getItem()) && ElytraItem.isUsable(itemStack);
	}

	public NetheriteElytraItem(Settings settings) {
		super(NetheriteElytraArmorMaterials.NETHERITE_ELYTRA_MATERIAL, EquipmentSlot.CHEST, settings);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
	}

	@Override
	public EquipmentSlot getSlotType() {
		return EquipmentSlot.CHEST;
	}

}
