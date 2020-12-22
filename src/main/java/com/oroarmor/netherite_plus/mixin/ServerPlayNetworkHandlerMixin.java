package com.oroarmor.netherite_plus.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.screen.NetheriteAnvilScreenHandler;

import net.minecraft.SharedConstants;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundRenameItemPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerPlayNetworkHandlerMixin {

	@Shadow
	private ServerPlayer player;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void init(MinecraftServer server, Connection connection, ServerPlayer player, CallbackInfo info) {
		NetheritePlusMod.CONNECTED_CLIENTS.add(player);
		NetheritePlusConfig.createLavaVisionUpdatePacket(player);
	}

	@Inject(method = "disconnect", at = @At("RETURN"))
	public void disconnect(Component reason, CallbackInfo info) {
		NetheritePlusMod.CONNECTED_CLIENTS.remove(player);
	}

	@Inject(method = "handleRenameItem", at = @At("RETURN"))
	public void onRenameItem(ServerboundRenameItemPacket packet, CallbackInfo info) {
		if (player.containerMenu instanceof NetheriteAnvilScreenHandler) {
			NetheriteAnvilScreenHandler anvilScreenHandler = (NetheriteAnvilScreenHandler) player.containerMenu;
			String string = SharedConstants.filterText(packet.getName());
			if (string.length() <= 35) {
				anvilScreenHandler.setNewItemName(string);
			}
		}
	}
}
