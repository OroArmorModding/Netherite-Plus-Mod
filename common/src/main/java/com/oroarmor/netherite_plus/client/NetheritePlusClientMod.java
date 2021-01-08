package com.oroarmor.netherite_plus.client;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import me.shedaniel.architectury.networking.NetworkManager;

public class NetheritePlusClientMod {
    public static double LAVA_VISION_DISTANCE = NetheritePlusConfig.GRAPHICS.LAVA_VISION_DISTANCE.getValue();

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, LavaVisionUpdatePacket.ID, (friendlyByteBuf, packetContext) -> {
            NetheritePlusClientMod.LAVA_VISION_DISTANCE = friendlyByteBuf.readDouble();
        });
    }

}
