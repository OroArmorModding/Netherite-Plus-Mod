package com.oroarmor.netherite_plus.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;

public class NetheriteFishingBobberEntity extends FishingBobberEntity {

    public NetheriteFishingBobberEntity(World world, PlayerEntity thrower, double x, double y, double z) {
        super(world, thrower, x, y, z);
    }
}