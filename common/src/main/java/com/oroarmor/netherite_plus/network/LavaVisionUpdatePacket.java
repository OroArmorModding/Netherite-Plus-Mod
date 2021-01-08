package com.oroarmor.netherite_plus.network;

import java.io.IOException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class LavaVisionUpdatePacket implements Packet {
    public static ResourceLocation ID = id("lava_vision_update");

    private double lavaVisionValue;

    public LavaVisionUpdatePacket(double lavaVisionValue) {
        this.lavaVisionValue = lavaVisionValue;
    }

    @Override
    public void read(FriendlyByteBuf arg) throws IOException {
        lavaVisionValue = arg.readDouble();
    }

    @Override
    public void write(FriendlyByteBuf arg) throws IOException {
        arg.writeDouble(lavaVisionValue);
    }

    @Override
    public void handle(PacketListener arg) {
    }

    public double getDistance() {
        return lavaVisionValue;
    }
}
