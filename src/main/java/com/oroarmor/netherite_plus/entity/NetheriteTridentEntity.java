/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.netherite_plus.entity;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
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

public class NetheriteTridentEntity extends ThrownTrident {
    public NetheriteTridentEntity(EntityType<? extends ThrownTrident> entityType, Level world) {
        super(entityType, world);
        tridentItem = new ItemStack(NetheritePlusItems.NETHERITE_TRIDENT);
    }

    public NetheriteTridentEntity(Level world, LivingEntity owner, ItemStack stack) {
        super(world, owner, stack);
        tridentItem = stack;
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        float f = 8.0F;
        if (entity instanceof LivingEntity livingEntity) {
            f += EnchantmentHelper.getDamageBonus(tridentItem, livingEntity.getMobType());
        }

        f = (float) (f * NetheritePlusMod.CONFIG.damage.trident_damage_multiplier.value() + NetheritePlusMod.CONFIG.damage.trident_damage_addition.value());

        Entity entity2 = getOwner();
        DamageSource damageSource = this.damageSources().trident(this, entity2 == null ? this : entity2);
        dealtDamage = true;
        SoundEvent soundEvent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damageSource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingEntity2) {
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingEntity2, entity2);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity2, livingEntity2);
                }

                doPostHurtEffects(livingEntity2);
            }
        }

        this.setDeltaMovement(getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
        float g = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && EnchantmentHelper.hasChanneling(tridentItem)) {
            BlockPos blockPos = entity.blockPosition();
            if (this.level().canSeeSky(blockPos)) {
                LightningBolt lightningEntity = EntityType.LIGHTNING_BOLT.create(this.level());
                lightningEntity.moveTo(Vec3.atBottomCenterOf(blockPos));
                lightningEntity.setCause(entity2 instanceof ServerPlayer ? (ServerPlayer) entity2 : null);
                this.level().addFreshEntity(lightningEntity);
                soundEvent = SoundEvents.TRIDENT_THUNDER;
                g = 5.0F;
            }
        }

        playSound(soundEvent, g, 1.0F);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return super.getAddEntityPacket();
    }
}
