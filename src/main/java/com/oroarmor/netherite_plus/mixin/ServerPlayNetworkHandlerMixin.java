package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;

import net.minecraft.SharedConstants;
import net.minecraft.network.packet.c2s.play.RenameItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {

	@Shadow
	private ServerPlayerEntity player;

	@SuppressWarnings("unused")
	@Inject(method = "onRenameItem", at = @At("RETURN"))
	public void onRenameItem(RenameItemC2SPacket packet, CallbackInfo info) {
		if (player.currentScreenHandler instanceof NetheriteAnvilScreenHandler) {
			NetheriteAnvilScreenHandler anvilScreenHandler = (NetheriteAnvilScreenHandler) player.currentScreenHandler;
			String string = SharedConstants.stripInvalidChars(packet.getName());
			if (string.length() <= 35) {
				anvilScreenHandler.setNewItemName(string);
			}
		}
	}

	@SuppressWarnings("unused")
	@Inject(method = "onUpdateBeacon", at = @At("RETURN"))
	public void onUpdateBeacon(UpdateBeaconC2SPacket inPacket, CallbackInfo info) {
		if (inPacket instanceof UpdateNetheriteBeaconC2SPacket) {

			UpdateNetheriteBeaconC2SPacket packet = (UpdateNetheriteBeaconC2SPacket) inPacket;
			if (player.currentScreenHandler instanceof NetheriteBeaconScreenHandler) {
				((NetheriteBeaconScreenHandler) player.currentScreenHandler).setEffects(packet.getPrimaryEffectId(),
						packet.getSecondaryEffectId(), packet.getTertiaryEffectId());
			}
		}

	}
}
