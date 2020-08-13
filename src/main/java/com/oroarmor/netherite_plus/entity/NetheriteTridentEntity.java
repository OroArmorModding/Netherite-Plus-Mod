package com.oroarmor.netherite_plus.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class NetheriteTridentEntity extends TridentEntity {
    public NetheriteTridentEntity(EntityType<? extends NetheriteTridentEntity> entityType, World world) {
        super(entityType, world);
    }

    public NetheriteTridentEntity(World world, PlayerEntity playerEntity, ItemStack stack) {
        super(world, playerEntity, stack);
    }

}
