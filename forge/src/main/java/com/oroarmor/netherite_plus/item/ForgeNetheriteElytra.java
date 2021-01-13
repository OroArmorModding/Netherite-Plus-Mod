package com.oroarmor.netherite_plus.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeItem;

public class ForgeNetheriteElytra extends NetheriteElytraItem implements IForgeItem {
	public ForgeNetheriteElytra(Settings settings) {
		super(settings);
	}

	@Override
	public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
		return stack.getDamage() < stack.getMaxDamage();
	}

	@Override
	public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
		return true;
	}
}
