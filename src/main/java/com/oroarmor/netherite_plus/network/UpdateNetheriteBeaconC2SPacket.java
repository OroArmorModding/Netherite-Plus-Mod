package com.oroarmor.netherite_plus.network;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import java.io.IOException;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.UpdateBeaconC2SPacket;
import net.minecraft.util.Identifier;

public class UpdateNetheriteBeaconC2SPacket extends UpdateBeaconC2SPacket {

	public static final Identifier ID = id("netherite_beacon_update_packet");

	private int tertiaryEffectId;

	@Environment(EnvType.CLIENT)
	public UpdateNetheriteBeaconC2SPacket(int primaryEffectId, int secondaryEffectId, int tertiaryEffectId) {
		super(primaryEffectId, secondaryEffectId);
		this.tertiaryEffectId = tertiaryEffectId;
	}

	public UpdateNetheriteBeaconC2SPacket() {
	}

	@Override
	public void read(PacketByteBuf buf) throws IOException {
		super.read(buf);
		tertiaryEffectId = buf.readVarInt();
	}

	@Override
	public void write(PacketByteBuf buf) throws IOException {
		super.write(buf);
		buf.writeVarInt(tertiaryEffectId);
	}

	public int getTertiaryEffectId() {
		return tertiaryEffectId;
	}
}
