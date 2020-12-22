package com.oroarmor.netherite_plus.network;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.io.IOException;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundSetBeaconPacket;
import net.minecraft.resources.ResourceLocation;

public class UpdateNetheriteBeaconC2SPacket extends ServerboundSetBeaconPacket {

	public static final ResourceLocation ID = id("netherite_beacon_update_packet");

	private int tertiaryEffectId;

	@Environment(EnvType.CLIENT)
	public UpdateNetheriteBeaconC2SPacket(int primaryEffectId, int secondaryEffectId, int tertiaryEffectId) {
		super(primaryEffectId, secondaryEffectId);
		this.tertiaryEffectId = tertiaryEffectId;
	}

	public UpdateNetheriteBeaconC2SPacket() {
	}

	@Override
	public void read(FriendlyByteBuf buf) throws IOException {
		super.read(buf);
		tertiaryEffectId = buf.readVarInt();
	}

	@Override
	public void write(FriendlyByteBuf buf) throws IOException {
		super.write(buf);
		buf.writeVarInt(tertiaryEffectId);
	}

	public int getTertiaryEffectId() {
		return tertiaryEffectId;
	}
}
