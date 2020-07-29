package com.oroarmor.netherite_plus.item;

import com.oroarmor.util.item.UniqueItemRegistry;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NetheriteElytraItem_Trinkets extends ArmorItem implements Trinket {

	public NetheriteElytraItem_Trinkets(Settings settings) {
		super(NetheriteElytraArmorMaterials.NETHERITE_ELYTRA_MATERIAL, EquipmentSlot.CHEST, settings);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
	}

	public static boolean isStackUsableAsElytra(ItemStack itemStack) {
		return UniqueItemRegistry.ELYTRA.isItemInRegistry(itemStack.getItem()) && ElytraItem.isUsable(itemStack);
	}

	@Override
	public EquipmentSlot getSlotType() {
		return EquipmentSlot.CHEST;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getStackInHand(hand);
		TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
		if (comp.equip(stack)) {
			stack.setCount(0);
			return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, stack);
		} else {
			return new TypedActionResult<ItemStack>(ActionResult.FAIL, stack);
		}
	}

	@Override
	public boolean canWearInSlot(String group, String slot) {
		return slot.equals(Slots.CAPE) || slot.equals(SlotGroups.CHEST);
	}

}
