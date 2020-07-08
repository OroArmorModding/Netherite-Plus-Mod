package com.oroarmor.netherite_plus.item;

import com.oroarmor.netherite_plus.NetheritePlusMod;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class NetheriteElytraItem extends ArmorItem {

	public NetheriteElytraItem(Settings settings) {
		super(NetheriteElytraArmorMaterials.NETHERITE_ELYTRA_MATERIAL, EquipmentSlot.CHEST, settings);
	}

	@Override
	public boolean canRepair(ItemStack stack, ItemStack ingredient) {
		return ingredient.getItem() == Items.PHANTOM_MEMBRANE;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
		ItemStack itemStack2 = user.getEquippedStack(equipmentSlot);
		if (itemStack2.isEmpty()) {
			user.equipStack(equipmentSlot, itemStack.copy());
			itemStack.setCount(0);
			return TypedActionResult.method_29237(itemStack, world.isClient());
		}
		return TypedActionResult.fail(itemStack);
	}

	public static boolean isElytra(ItemStack itemStack) {
		return itemStack.getItem() == Items.ELYTRA || itemStack.getItem() == NetheritePlusMod.NETHERITE_ELYTRA;
	}

	public static boolean isStackUsableAsElytra(ItemStack itemStack) {
		return NetheriteElytraItem.isElytra(itemStack) && ElytraItem.isUsable(itemStack);
	}

}
