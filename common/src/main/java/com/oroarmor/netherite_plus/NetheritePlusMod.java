package com.oroarmor.netherite_plus;

import java.util.ArrayList;
import java.util.List;

import com.oroarmor.config.ConfigItemGroup;
import com.oroarmor.multi_item_lib.UniqueItemRegistry;
import com.oroarmor.netherite_plus.advancement.criterion.NetheritePlusCriteria;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.entity.effect.NetheritePlusStatusEffects;
import com.oroarmor.netherite_plus.item.NetheritePlusItems;
import com.oroarmor.netherite_plus.loot.NetheritePlusLootManager;
import com.oroarmor.netherite_plus.recipe.NetheritePlusRecipeSerializer;
import com.oroarmor.netherite_plus.screen.NetheritePlusScreenHandlers;
import com.oroarmor.netherite_plus.stat.NetheritePlusStats;
import me.shedaniel.architectury.registry.Registries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.player.Player;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusMod {
    public static final NetheritePlusConfig CONFIG = new NetheritePlusConfig();

    public static final Logger LOGGER = LogManager.getLogger("Netherite Plus");

    public static final String MOD_ID = "netherite_plus";

    public static final List<Player> CONNECTED_CLIENTS = new ArrayList<>();

    public static final LazyLoadedValue<Registries> REGISTRIES = new LazyLoadedValue<>(() -> Registries.get(MOD_ID));

    public static void initialize() {
        processConfig();
        NetheritePlusItems.init();
        NetheritePlusScreenHandlers.init();
        NetheritePlusLootManager.init();
        NetheritePlusRecipeSerializer.init();
        NetheritePlusStatusEffects.init();
        NetheritePlusCriteria.init();
        NetheritePlusStats.init();
    }

    private static void processConfig() {
        CONFIG.readConfigFromFile();

        if (NetheritePlusConfig.ENABLED.ENABLED_CONFIG_PRINT.getValue()) {
            CONFIG.getConfigs().stream().map(ConfigItemGroup::getConfigs).forEach(l -> l.forEach(ci -> LOGGER.log(Level.INFO, ci.toString())));
        }
    }

    public static ResourceLocation id(String id) {
        return new ResourceLocation(MOD_ID, id);
    }

    public static void registerItemsWithMultiItemLib() {
        UniqueItemRegistry.SHIELD.addItemToRegistry(NETHERITE_SHIELD.get());
        UniqueItemRegistry.FISHING_ROD.addItemToRegistry(NETHERITE_FISHING_ROD.get());
        UniqueItemRegistry.ELYTRA.addItemToRegistry(NETHERITE_ELYTRA.get());
        UniqueItemRegistry.BOW.addItemToRegistry(NETHERITE_BOW.get());
        UniqueItemRegistry.CROSSBOW.addItemToRegistry(NETHERITE_CROSSBOW.get());
        UniqueItemRegistry.TRIDENT.addItemToRegistry(NETHERITE_TRIDENT.get());
    }
}
