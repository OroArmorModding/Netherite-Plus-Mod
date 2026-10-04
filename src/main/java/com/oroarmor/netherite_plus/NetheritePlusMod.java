/*
 * MIT License
 *
 * Copyright (c) 2021-2026 OroArmor (Eli Orona)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.oroarmor.netherite_plus;

import java.nio.file.Paths;

import com.oroarmor.netherite_plus.advancements.triggers.NetheritePlusCriteriaTriggers;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.network.protocol.configuration.ClientboundLavaVisionUpdatePacket;
import com.oroarmor.netherite_plus.network.protocol.game.ServerboundSetNetheriteBeacon;
import com.oroarmor.netherite_plus.stats.NetheritePlusStats;
import com.oroarmor.netherite_plus.world.entity.NetheritePlusEntityAttachments;
import com.oroarmor.netherite_plus.world.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.world.inventory.NetheriteBeaconMenu;
import com.oroarmor.netherite_plus.world.inventory.NetheritePlusMenuType;
import com.oroarmor.netherite_plus.world.item.NetheritePlusItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerConfigurationConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.resources.Identifier;

public class NetheritePlusMod implements ModInitializer {
    public static final String MOD_ID = "netherite_plus";
    public static final NetheritePlusConfig CONFIG = NetheritePlusConfig.createToml(
            Paths.get("config"),
            MOD_ID,   // The family id, this should usually just be your mod ID
            "config",           // The id for this particular config, since your mod might have multiple
            NetheritePlusConfig.class      // The config class you created earlier
    );

    public static final Logger LOGGER = LogManager.getLogger("Netherite Plus");

    public static Identifier id(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }

    public void onInitialize() {
        NetheritePlusItems.init();
        NetheritePlusMenuType.init();
        NetheritePlusStatusEffects.init();
        NetheritePlusCriteriaTriggers.init();
        NetheritePlusStats.init();
        NetheritePlusEntityAttachments.init();

        PayloadTypeRegistry.serverboundPlay().register(
                ServerboundSetNetheriteBeacon.TYPE,
                ServerboundSetNetheriteBeacon.STREAM_CODEC
        );

        PayloadTypeRegistry.clientboundConfiguration().register(
                ClientboundLavaVisionUpdatePacket.TYPE,
                ClientboundLavaVisionUpdatePacket.STREAM_CODEC
        );

        ServerConfigurationConnectionEvents.CONFIGURE.register((listener, server) -> {
            listener.send(new ClientboundCustomPayloadPacket(new ClientboundLavaVisionUpdatePacket(CONFIG.graphics.lavaVisionDistance)));
        });

        ServerPlayNetworking.registerGlobalReceiver(ServerboundSetNetheriteBeacon.TYPE, (payload, context) -> {
            context.server().execute(() -> {
                if (context.player().containerMenu instanceof NetheriteBeaconMenu menu) {
                    if (!context.player().containerMenu.stillValid(context.player())) {
                        LOGGER.debug("Player {} interacted with invalid menu {}", context.player(), context.player().containerMenu);
                        return;
                    }

                    if (!menu.setEffects(payload.primary(), payload.secondary(), payload.tertiary())) {
                        context.responseSender().disconnect(Component.translatable("multiplayer.disconnect.generic"));
                        LOGGER.warn("Player {} tried to set invalid beacon effects", context.player());
                    }
                }
            });
        });
    }
}
