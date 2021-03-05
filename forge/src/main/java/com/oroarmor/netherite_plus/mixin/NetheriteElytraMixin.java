package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import net.minecraftforge.common.extensions.IForgeItem;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Mixin(NetheriteElytraItem.class)
public class NetheriteElytraMixin extends Item implements IForgeItem {
    public NetheriteElytraMixin(Settings properties) {
        super(properties);
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
