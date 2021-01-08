package com.oroarmor.netherite_plus.entity;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class NetheriteTridentEntity extends ThrownTrident {
    public NetheriteTridentEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
        super(entityType, world);
        tridentItem = new ItemStack(NetheritePlusItems.NETHERITE_TRIDENT.get());
    }

    @Environment(EnvType.CLIENT)
    public NetheriteTridentEntity(Level world, double x, double y, double z) {
        super(world, x, y, z);
        tridentItem = new ItemStack(NetheritePlusItems.NETHERITE_TRIDENT.get());
    }

    public NetheriteTridentEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
        tridentItem = stack;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            f += EnchantmentHelper.getDamageBonus(tridentItem, livingEntity.getMobType());
        }

        f = (float) (f * NetheritePlusConfig.DAMAGE.TRIDENT_DAMAGE_MULTIPLIER.getValue() + NetheritePlusConfig.DAMAGE.TRIDENT_DAMAGE_ADDITION.getValue());

        Entity entity2 = getOwner();
        DamageSource damageSource = DamageSource.trident(this, entity2 == null ? this : entity2);
        dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity2 = (LivingEntity) entity;
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity2, entity2);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity2, livingEntity2);
                }

                doPostHurtEffects(livingEntity2);
            }
        }

        this.setDeltaMovement(getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float g = 1.0F;
        if (level instanceof ServerLevel && level.isThundering() && EnchantmentHelper.hasChanneling(tridentItem)) {
            BlockPos blockPos = entity.blockPosition();
            if (level.canSeeSky(blockPos)) {
                LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(level);
                lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
                lightningEntity.setCause(entity2 instanceof ServerPlayer ? (ServerPlayer) entity2 : null);
                level.addFreshEntity(lightningEntity);
                soundEvent = SoundEvents.TRIDENT_THUNDER;
                g = 5.0F;
            }
        }

        playSound(soundEvent, g, 1.0F);
    }

}
