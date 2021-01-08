package com.oroarmor.netherite_plus;

import java.io.IOException;

import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import com.oroarmor.netherite_plus.client.ForgeNetheritePlusModClient;
import com.oroarmor.netherite_plus.client.NetheritePlusClientMod;
import com.oroarmor.netherite_plus.network.LavaVisionUpdatePacket;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import me.shedaniel.architectury.platform.forge.EventBuses;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

@Mod(NetheritePlusMod.MOD_ID)
public class ForgeNetheritePlusMod {

    private static final String PROTOCOL_VERSION = "1.0.0";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            NetheritePlusMod.id("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public ForgeNetheritePlusMod() {
        EventBuses.registerModEventBus(NetheritePlusMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        NetheritePlusMod.initialize();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().register(new ForgeNetheritePlusModClient()));
        FMLJavaModLoadingContext.get().getModEventBus().register(this);

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
            ctx.get().getSender().server.execute(() -> {
                if (ctx.get().getSender().containerMenu instanceof NetheriteBeaconScreenHandler) {
                    ((NetheriteBeaconScreenHandler) ctx.get().getSender().containerMenu).setEffects(packet.getPrimary(), packet.getSecondary(), packet.getTertiaryEffectId());
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
    public void registerItemsWithMultiItemLib(FMLLoadCompleteEvent event) {
        UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD.getOrNull());
        UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD.getOrNull());
        UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA.getOrNull());
        UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW.getOrNull());
        UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW.getOrNull());
        UniqueItemRegistry.TRIDENT.addItemToRegistry(NETHERITE_TRIDENT.getOrNull());
    }
}
