package com.oroarmor.netherite_plus.mixin;

import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.entity.NetheriteTridentEntity;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;

@Mixin(ClientPacketListener.class)
public class ClientPlayNetworkHandlerMixin {

	@Shadow
	Minecraft minecraft;

	@Shadow
	ClientLevel level;

	@Inject(method = "handleAddEntity", at = @At("HEAD"), cancellable = true)
	public void onEntitySpawnMixin(ClientboundAddEntityPacket packet, CallbackInfo info) {
		PacketUtils.ensureRunningOnSameThread(packet, (ClientPacketListener) (Object) this, minecraft);
		double d = packet.getX();
		double e = packet.getY();
		double f = packet.getZ();
		EntityType<?> entityType = packet.getType();
		Entity entity15 = null;

		Entity entity16;
		if (entityType == EntityType.TRIDENT) {
			entity15 = new ThrownTrident(level, d, e, f);
			entity16 = level.getEntity(packet.getData());

			if (entity16 instanceof LocalPlayer) {

				boolean hasNetheriteTrident = false;
				Iterator<ItemStack> items = entity16.getHandSlots().iterator();
				while (items.hasNext()) {
					hasNetheriteTrident |= items.next().getItem() == NetheritePlusItems.NETHERITE_TRIDENT;
				}
				if (hasNetheriteTrident) {
					entity15 = new NetheriteTridentEntity(level, d, e, f);
				}
			}

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
