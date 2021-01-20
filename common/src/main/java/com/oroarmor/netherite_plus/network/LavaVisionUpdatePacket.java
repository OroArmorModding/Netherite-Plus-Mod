package com.oroarmor.netherite_plus.network;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class LavaVisionUpdatePacket implements Packet<ClientPlayPacketListener> {
    public static Identifier ID = id("lava_vision_update");

    private double lavaVisionValue;

    public LavaVisionUpdatePacket(double lavaVisionValue) {
        this.lavaVisionValue = lavaVisionValue;
    }

    @Override
    public void read(PacketByteBuf arg) throws IOException {
        lavaVisionValue = arg.readDouble();
    }

    @Override
    public void write(PacketByteBuf arg) throws IOException {
        arg.writeDouble(lavaVisionValue);
    }

    public double getDistance() {
        return lavaVisionValue;
    }

    @Override
    public void apply(ClientPlayPacketListener listener) {

    }
}
