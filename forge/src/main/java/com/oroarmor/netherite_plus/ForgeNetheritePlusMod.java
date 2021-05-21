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

import java.io.IOException;

import com.oroarmor.config.screen.ForgeConfigScreen;
import com.oroarmor.netherite_plus.client.ForgeNetheritePlusModClient;
import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import me.shedaniel.architectury.event.EventHandler;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import net.minecraft.item.ShearsItem;
import net.minecraft.server.network.ServerPlayerEntity;

@Mod(NetheritePlusMod.MOD_ID)
public class ForgeNetheritePlusMod {

	private static final String PROTOCOL_VERSION = "1.0.0";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(NetheritePlusMod.id("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public ForgeNetheritePlusMod() {
		EventBuses.registerModEventBus(NetheritePlusMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		NetheritePlusMod.initialize();
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().register(new ForgeNetheritePlusModClient()));
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> new ForgeConfigScreen(NetheritePlusMod.CONFIG));

		INSTANCE.registerMessage(0, UpdateNetheriteBeaconC2SPacket.class, (unb, fbb2) -> {
			try {
				unb.write(fbb2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, (fbb) -> {
			UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket();
			try {
				packet.read(fbb);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return packet;
		}, (packet, ctx) -> {
			ServerPlayerEntity sender = ctx.get().getSender();
			sender.server.execute(() -> {
				if (sender.currentScreenHandler instanceof NetheriteBeaconScreenHandler) {
					((NetheriteBeaconScreenHandler) sender.currentScreenHandler).setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId(), packet.getTertiaryEffectId());
				}
			});
		});
		INSTANCE.registerMessage(1, LavaVisionUpdatePacket.class, (unb, fbb2) -> {
			try {
				unb.write(fbb2);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, (fbb) -> {
			LavaVisionUpdatePacket packet = new LavaVisionUpdatePacket(0);
			try {
				packet.read(fbb);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return packet;
		}, (packet, ctx) -> {
			ctx.get().getSender().server.execute(() -> {
				NetheritePlusClientMod.LAVA_VISION_DISTANCE = packet.getDistance();
			});
		});
	}

	@SubscribeEvent
	public void registerItemsWithMultiItemLib(FMLCommonSetupEvent event) {
		NetheritePlusMod.registerItemsWithMultiItemLib();
	}
}
