package com.oroarmor.netherite_plus.mixin;

import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import com.oroarmor.netherite_plus.entity.NetheriteTridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Inject(method = "handleAddEntity", at = @At("HEAD"), cancellable = true)
    public void onEntitySpawnMixin(ClientboundAddEntityPacket packet, CallbackInfo info) {
        PacketUtils.ensureRunningOnSameThread(packet, (ClientPacketListener) (Object) this, ((ClientPlayNetworkHandlerAccessor) this).getMinecraft());
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        EntityType<?> entityType = packet.getType();
        Entity entity15 = null;

        ClientLevel level = ((ClientPlayNetworkHandlerAccessor) this).getLevel();

        Entity entity16;
        if (entityType == EntityType.TRIDENT) {
            entity15 = new ThrownTrident(level, d, e, f);
            entity16 = level.getEntity(packet.getData());

            ((ThrownTrident) entity15).tridentItem = new ItemStack(Registry.ITEM.byId(NetheritePlusClientMod.TRIDENT_QUEUE.remove()));

            if (entity16 != null) {
                ((AbstractArrow) entity15).setOwner(entity16);
            }
        }

        if (entity15 != null) {
            int i = packet.getId();
            entity15.setPacketCoordinates(d, e, f);
            entity15.moveTo(d, e, f);
            entity15.xRot = packet.getxRot() * 360 / 256.0F;
            entity15.yRot = packet.getyRot() * 360 / 256.0F;
            entity15.setId(i);
            entity15.setUUID(packet.getUUID());
            level.putNonPlayerEntity(i, entity15);
            info.cancel();
        }
    }

}
