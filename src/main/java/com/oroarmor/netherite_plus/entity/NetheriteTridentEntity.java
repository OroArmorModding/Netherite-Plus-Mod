package com.oroarmor.netherite_plus.entity;

import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NetheriteTridentEntity extends TridentEntity {

	public NetheriteTridentEntity(World world, LivingEntity owner, ItemStack stack) {
		super(world, owner, stack);
		this.tridentStack = stack;
	}

	@Environment(EnvType.CLIENT)
	public NetheriteTridentEntity(World world, double x, double y, double z) {
		super(world, x, y, z);
		this.tridentStack = new ItemStack(NetheritePlusItems.NETHERITE_TRIDENT);
	}

	public NetheriteTridentEntity(EntityType<? extends TridentEntity> entityType, World world) {
		super(entityType, world);
		this.tridentStack = new ItemStack(NetheritePlusItems.NETHERITE_TRIDENT);
	}

}
