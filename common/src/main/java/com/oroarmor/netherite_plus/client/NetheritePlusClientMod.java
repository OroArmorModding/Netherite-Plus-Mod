package com.oroarmor.netherite_plus.client;

import java.util.LinkedList;
import java.util.Queue;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import me.shedaniel.architectury.networking.NetworkManager;

public class NetheritePlusClientMod {
    public static double LAVA_VISION_DISTANCE = NetheritePlusConfig.GRAPHICS.LAVA_VISION_DISTANCE.getValue();
    public static final Queue<Integer> TRIDENT_QUEUE = new LinkedList<>();

    public static void init() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, LavaVisionUpdatePacket.ID, (friendlyByteBuf, packetContext) -> {
            NetheritePlusClientMod.LAVA_VISION_DISTANCE = friendlyByteBuf.readDouble();
        });

        NetworkManager.registerReceiver(NetworkManager.Side.S2C, NetheritePlusMod.id("netherite_trident"), (friendlyByteBuf, packetContext) -> {
            System.out.println("received");
            TRIDENT_QUEUE.add(friendlyByteBuf.readInt());
        });
    }

}
