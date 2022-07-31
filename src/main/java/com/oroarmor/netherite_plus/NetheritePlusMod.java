/*
 * MIT License
 *
 * Copyright (c) 2021 OroArmor (Eli Orona)
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

import java.util.ArrayList;
import java.util.Collection;

import com.oroarmor.multiitemlib.api.UniqueItemRegistry;
import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import com.oroarmor.netherite_plus.stat.NetheritePlusStats;
import io.netty.buffer.ByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.loader.api.config.QuiltConfig;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.quiltmc.qsl.networking.api.PacketByteBufs;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusMod implements ModInitializer {
    public static final String MOD_ID = "netherite_plus";
    public static final NetheritePlusConfig CONFIG = QuiltConfig.create(
            MOD_ID,   // The family id, this should usually just be your mod ID
            "config",           // The id for this particular config, since your mod might have multiple
            NetheritePlusConfig.class      // The config class you created earlier
    );

    public static final Logger LOGGER = LogManager.getLogger("Netherite Plus");
    public static final Collection<ServerPlayerEntity> CONNECTED_CLIENTS = new ArrayList<>();

    public void onInitialize(ModContainer mod) {
        NetheritePlusItems.init();
        NetheritePlusScreenHandlers.init();
        NetheritePlusRecipeSerializer.init();
        NetheritePlusStatusEffects.init();
        NetheritePlusCriteria.init();
        NetheritePlusStats.init();

        ServerPlayNetworking.registerGlobalReceiver(UpdateNetheriteBeaconC2SPacket.ID, (server, player, handler, buf, responseSender) -> {
            UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket(buf);
            server.execute(() -> {
                if (player.currentScreenHandler instanceof NetheriteBeaconScreenHandler screenHandler) {
                    screenHandler.setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId(), packet.getTertiaryEffect());
                }
            });
        });

        CONFIG.registerCallback(config -> {
            PacketByteBuf buf = PacketByteBufs.create();
            buf.writeDouble(CONFIG.graphics.lava_vision_distance);
            ServerPlayNetworking.send(CONNECTED_CLIENTS, LavaVisionUpdatePacket.ID, buf);
        });
        NetheritePlusMod.registerItemsWithMultiItemLib();
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static void registerItemsWithMultiItemLib() {
        if (CONFIG.enabled.shields) {
            UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD);
        }
        if (CONFIG.enabled.fishing_rod) {
            UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD);
        }
        if (CONFIG.enabled.bows_and_crossbows) {
            UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW);
            UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW);
        }
        if (CONFIG.enabled.trident) {
            UniqueItemRegistry.TRIDENT.addItemToRegistry(NETHERITE_TRIDENT);
        }
        if (CONFIG.enabled.shears) {
            UniqueItemRegistry.SHEARS.addItemToRegistry(NETHERITE_SHEARS);
        }
    }
}
