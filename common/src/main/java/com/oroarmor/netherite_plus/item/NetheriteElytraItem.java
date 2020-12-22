package com.oroarmor.netherite_plus.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class NetheriteElytraItem extends ArmorItem {

	public NetheriteElytraItem(Properties settings) {
		super(NetheriteElytraArmorMaterials.NETHERITE_ELYTRA_MATERIAL, EquipmentSlot.CHEST, settings);
	}

	@Override
	public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
	}

	@Override
	public EquipmentSlot getSlot() {
		return EquipmentSlot.CHEST;
	}

}
