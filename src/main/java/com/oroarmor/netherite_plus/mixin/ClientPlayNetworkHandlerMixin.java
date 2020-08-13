package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.entity.NetheriteTridentEntity;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.thread.ThreadExecutor;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow
	MinecraftClient client;

	@Shadow
	ClientWorld world;

	@Inject(method = "onEntitySpawn", at = @At("HEAD"), cancellable = true)
	public void onEntitySpawnMixin(EntitySpawnS2CPacket packet, CallbackInfo info) {
		NetworkThreadUtils.forceMainThread(packet, (ClientPlayNetworkHandler) (Object) this,
				(ThreadExecutor<?>) client);
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		EntityType<?> entityType = packet.getEntityTypeId();
		Entity entity15 = null;

		Entity entity16;
		if (entityType == EntityType.TRIDENT) {
			entity15 = new TridentEntity(world, d, e, f);
			entity16 = world.getEntityById(packet.getEntityData());

			if (entity16 instanceof ClientPlayerEntity) {
				if (((ClientPlayerEntity) entity16).getItemsHand().iterator().next()
						.getItem() == NetheritePlusItems.NETHERITE_TRIDENT) {
					entity15 = new NetheriteTridentEntity(world, d, e, f);
				}
			}

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
			world.addEntity(i, entity15);
			info.cancel();
		}
	}

}
