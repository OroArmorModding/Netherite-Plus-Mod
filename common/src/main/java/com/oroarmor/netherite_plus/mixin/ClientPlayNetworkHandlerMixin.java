/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.registry.Registry;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
    public void onEntitySpawnMixin(EntitySpawnS2CPacket packet, CallbackInfo info) {
        NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this, ((ClientPlayNetworkHandlerAccessor) this).getClient());
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        EntityType<?> entityType = packet.getEntityTypeId();
        Entity entity15 = null;

        ClientWorld level = ((ClientPlayNetworkHandlerAccessor) this).getWorld();

        Entity entity16;
        if (entityType == EntityType.TRIDENT) {
            entity15 = new TridentEntity(level, d, e, f);
            entity16 = level.getEntityById(packet.getEntityData());

            ((TridentEntity) entity15).tridentStack = new ItemStack(Registry.ITEM.get(NetheritePlusClientMod.TRIDENT_QUEUE.remove()));

            if (entity16 != null) {
                ((PersistentProjectileEntity) entity15).setOwner(entity16);
            }
        }

        if (entity15 != null) {
            int i = packet.getId();
            entity15.updateTrackedPosition(d, e, f);
            entity15.refreshPositionAfterTeleport(d, e, f);
            entity15.pitch = packet.getPitch() * 360 / 256.0F;
            entity15.yaw = packet.getYaw() * 360 / 256.0F;
            entity15.setEntityId(i);
            entity15.setUuid(packet.getUuid());
            level.addEntity(i, entity15);
            info.cancel();
        }
    }

}
