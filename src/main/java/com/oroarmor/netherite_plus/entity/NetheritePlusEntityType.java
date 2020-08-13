package com.oroarmor.netherite_plus.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
public class NetheritePlusEntityType {
    public static EntityType<NetheriteTridentEntity> NETHERITE_TRIDENT = register(new Identifier("netherite_plus", "netherite_trident").toString(), EntityType.Builder.create(NetheriteTridentEntity::new, SpawnGroup.MISC)
            .setDimensions(0.5F, 0.5F).maxTrackingRange(4).trackingTickInterval(20).makeFireImmune());
    private static <T extends Entity> EntityType<NetheriteTridentEntity> register(String id, EntityType.Builder<T> type) {
        return (EntityType)Registry.register(Registry.ENTITY_TYPE, (String)id, type.build(id));
    }
}
