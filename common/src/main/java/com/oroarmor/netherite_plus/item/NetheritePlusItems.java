package com.oroarmor.netherite_plus.item;

import java.util.function.Supplier;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.block.NetheritePlusBlocks;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import me.shedaniel.architectury.registry.RegistrySupplier;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public final class NetheritePlusItems {
    public static final Item.Settings NETHERITE_SHULKER_BOX_ITEM_SETTINGS = NetheritePlusModPlatform.setISTER(new Item.Settings().maxCount(1).group(ItemGroup.DECORATIONS).fireproof());

    public static RegistrySupplier<Item> NETHERITE_ELYTRA;

    public static RegistrySupplier<Item> NETHERITE_FISHING_ROD;

    public static RegistrySupplier<Item> NETHERITE_SHIELD;

    public static RegistrySupplier<Item> NETHERITE_BOW;

    public static RegistrySupplier<Item> NETHERITE_CROSSBOW;

    public static RegistrySupplier<Item> NETHERITE_TRIDENT;

    public static RegistrySupplier<Item> NETHERITE_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_WHITE_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_ORANGE_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_MAGENTA_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_LIGHT_BLUE_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_YELLOW_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_LIME_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_PINK_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_GRAY_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_LIGHT_GRAY_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_CYAN_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_PURPLE_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_BLUE_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_BROWN_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_GREEN_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_RED_SHULKER_BOX;
    public static RegistrySupplier<Item> NETHERITE_BLACK_SHULKER_BOX;

    public static RegistrySupplier<Item> NETHERITE_BEACON;

    public static RegistrySupplier<Item> NETHERITE_HORSE_ARMOR;

    public static RegistrySupplier<Item> FAKE_NETHERITE_BLOCK;

    public static RegistrySupplier<Item> NETHERITE_ANVIL_ITEM;

    private static RegistrySupplier<Item> register(RegistrySupplier<Block> block, Supplier<BlockItem> item) {
        return register(block.getId(), item::get);
    }

    private static RegistrySupplier<Item> register(Identifier id, Supplier<Item> itemSupplier) {
        return NetheritePlusMod.REGISTRIES.get().get(Registry.ITEM_KEY).registerSupplied(id, () -> {
            Item item = itemSupplier.get();
            if (item instanceof BlockItem) {
                ((BlockItem) item).appendBlocks(Item.BLOCK_ITEMS, item);
            }

            return item;
        });
    }

    private static void registerBowAndCrossbow() {
        NETHERITE_BOW = register(id("netherite_bow"), () -> new NetheriteBowItem(new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.BOW_DURABILITY.getValue()).group(ItemGroup.COMBAT).fireproof()));
        NETHERITE_CROSSBOW = register(id("netherite_crossbow"), () -> new NetheriteCrossbowItem(new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.CROSSBOW_DURABILITY.getValue()).group(ItemGroup.COMBAT).fireproof()));
    }

    private static void registerElytra() {
        Item.Settings elytraSettings = new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.ELYTRA_DURABILITY.getValue()).group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON).fireproof();

        NETHERITE_ELYTRA = register(id("netherite_elytra"), () -> NetheritePlusModPlatform.getElytraItem(elytraSettings));
    }

    private static void registerFishingRod() {
        NETHERITE_FISHING_ROD = register(id("netherite_fishing_rod"), () -> new NetheriteFishingRodItem(new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.FISHING_ROD_DURABILITY.getValue()).group(ItemGroup.TOOLS).fireproof()));
    }

    private static void registerHorseArmor() {
        NETHERITE_HORSE_ARMOR = register(id("netherite_horse_armor"), () -> new NetheriteHorseArmorItem(15, new Item.Settings().maxCount(1).group(ItemGroup.MISC).fireproof()));
    }

    public static void init() {
        if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
            registerShulkerBoxes();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue()) {
            registerElytra();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue()) {
            registerShield();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_BOWS_AND_CROSSBOWS.getValue()) {
            registerBowAndCrossbow();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_FISHING_ROD.getValue()) {
            registerFishingRod();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_HORSE_ARMOR.getValue()) {
            registerHorseArmor();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue()) {
            registerTrident();
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_FAKE_NETHERITE_BLOCKS.getValue()) {
            FAKE_NETHERITE_BLOCK = register(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK, () -> new BlockItem(NetheritePlusBlocks.FAKE_NETHERITE_BLOCK.get(), new Item.Settings().group(ItemGroup.BUILDING_BLOCKS).fireproof()));
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_ANVIL.getValue()) {
            NETHERITE_ANVIL_ITEM = register(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_ANVIL_BLOCK.get(), new Item.Settings().group(ItemGroup.DECORATIONS).fireproof()));
        }

        if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
            NETHERITE_BEACON = register(NetheritePlusBlocks.NETHERITE_BEACON, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_BEACON.get(), new Item.Settings().maxCount(64).group(ItemGroup.MISC).fireproof()));
        }
    }

    private static void registerShield() {
        NETHERITE_SHIELD = register(id("netherite_shield"), () -> new NetheriteShieldItem(NetheritePlusModPlatform.setISTER(new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.SHIELD_DURABILITY.getValue()).group(ItemGroup.COMBAT).fireproof())));
    }

    private static void registerShulkerBoxes() {
        NETHERITE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_WHITE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_WHITE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_ORANGE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_ORANGE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_MAGENTA_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_MAGENTA_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIGHT_BLUE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_BLUE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_YELLOW_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_YELLOW_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIME_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_LIME_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_PINK_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_PINK_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_GRAY_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_GRAY_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_LIGHT_GRAY_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_LIGHT_GRAY_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_CYAN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_CYAN_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_PURPLE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_PURPLE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BLUE_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_BLUE_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BROWN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_BROWN_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_GREEN_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_GREEN_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_RED_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_RED_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
        NETHERITE_BLACK_SHULKER_BOX = register(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX, () -> new BlockItem(NetheritePlusBlocks.NETHERITE_BLACK_SHULKER_BOX.get(), NETHERITE_SHULKER_BOX_ITEM_SETTINGS));
    }

    private static void registerTrident() {
        Item.Settings properties = NetheritePlusModPlatform.setISTER(new Item.Settings().maxDamage(NetheritePlusConfig.DURABILITIES.TRIDENT_DURABILITY.getValue()).group(ItemGroup.COMBAT).fireproof());
        NETHERITE_TRIDENT = register(id("netherite_trident"), () -> new NetheriteTridentItem(properties));
    }

}
