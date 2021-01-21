package com.oroarmor.netherite_plus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.oroarmor.config.ConfigItemGroup;
import com.oroarmor.config.command.ConfigCommand;
import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;
import com.oroarmor.netherite_plus.screen.NetheriteBeaconScreenHandler;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import com.oroarmor.netherite_plus.stat.NetheritePlusStats;
import me.shedaniel.architectury.event.events.CommandRegistrationEvent;
import me.shedaniel.architectury.networking.NetworkManager;
import me.shedaniel.architectury.registry.Registries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.Lazy;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusMod {
    public static final NetheritePlusConfig CONFIG = new NetheritePlusConfig();

    public static final Logger LOGGER = LogManager.getLogger("Netherite Plus");

    public static final String MOD_ID = "netherite_plus";

    public static final List<PlayerEntity> CONNECTED_CLIENTS = new ArrayList<>();

    public static final Lazy<Registries> REGISTRIES = new Lazy<>(() -> Registries.get(MOD_ID));

    public static void initialize() {
        processConfig();
        NetheritePlusItems.init();
        NetheritePlusScreenHandlers.init();
        NetheritePlusRecipeSerializer.init();
        NetheritePlusStatusEffects.init();
        NetheritePlusCriteria.init();
        NetheritePlusStats.init();

        NetworkManager.registerReceiver(NetworkManager.Side.C2S, UpdateNetheriteBeaconC2SPacket.ID, (friendlyByteBuf, packetContext) -> {
            UpdateNetheriteBeaconC2SPacket packet = new UpdateNetheriteBeaconC2SPacket();
            try {
                packet.read(friendlyByteBuf);
            } catch (IOException e) {
                e.printStackTrace();
            }
            packetContext.queue(() -> {
                if (packetContext.getPlayer().currentScreenHandler instanceof NetheriteBeaconScreenHandler) {
                    ((NetheriteBeaconScreenHandler) packetContext.getPlayer().currentScreenHandler).setEffects(packet.getPrimaryEffectId(), packet.getSecondaryEffectId(), packet.getTertiaryEffectId());
                }
            });
        });

         CommandRegistrationEvent.EVENT.register((commandSourceCommandDispatcher, registrationEnvironment) -> new ConfigCommand(CONFIG).register(commandSourceCommandDispatcher, registrationEnvironment == CommandManager.RegistrationEnvironment.DEDICATED));
    }

    private static void processConfig() {
        CONFIG.readConfigFromFile();

        if (NetheritePlusConfig.ENABLED.ENABLED_CONFIG_PRINT.getValue()) {
            CONFIG.getConfigs().stream().map(ConfigItemGroup::getConfigs).forEach(l -> l.forEach(ci -> LOGGER.log(Level.INFO, ci.toString())));
        }
    }

    public static Identifier id(String id) {
        return new Identifier(MOD_ID, id);
    }

    public static void registerItemsWithMultiItemLib() {
        if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue())
            UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD.get());
        if (NetheritePlusConfig.ENABLED.ENABLED_FISHING_ROD.getValue())
            UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD.get());
        if (NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue())
            UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA.get());
        if (NetheritePlusConfig.ENABLED.ENABLED_BOWS_AND_CROSSBOWS.getValue()) {
            UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW.get());
            UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW.get());
        }
        if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue())
            UniqueItemRegistry.TRIDENT.addItemToRegistry(NETHERITE_TRIDENT.get());
    }
}
