package com.oroarmor.netherite_plus.item;

import net.minecraftforge.common.extensions.IForgeItem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class ForgeNetheriteElytra extends NetheriteElytraItem implements IForgeItem {
    public ForgeNetheriteElytra(Properties settings) {
        super(settings);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return stack.getDamageValue() < stack.getMaxDamage();
    }

    @Override
    public boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return true;
    }
}
