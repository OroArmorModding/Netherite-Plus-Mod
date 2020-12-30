package com.oroarmor.netherite_plus.forge;

import java.io.File;
import java.util.function.BiFunction;

import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import me.shedaniel.architectury.ExpectPlatform;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.client.gui.IHasContainer;

public class NetheritePlusModPlatformImpl {
    public static void sendBeaconUpdatePacket(PacketBuffer buf) {
        throw new AssertionError();
    }

    public static <T extends ICriterionTrigger<?>> T registerCriteria(T object) {
        throw new AssertionError();
    }

    public static Item getElytraItem(Item.Properties elytraSettings) {
        return new NetheriteElytraItem(elytraSettings);
    }

    public static File getConfigDir() {
        return new File(FMLPaths.CONFIGDIR.get().toFile(), NetheritePlusConfig.CONFIG_FILE_NAME);
    }

    public static BiFunction<Item, String, Item> getItemRegisterer() {
        return (item, string) -> {
            return item;
        };
    }

    public static BiFunction<Block, String, Block> getBlockRegisterer() {
        return (block, string) -> {
            return block;
        };
    }

    public static BiFunction<TileEntityType, String, TileEntityType> getBlockEntityTypeRegisterer() {
        return (type, string) -> {
            return type;
        };
    }

    @ExpectPlatform
    public static <H extends Container, S extends Screen & IHasContainer<H>> void registerScreen(ContainerType<?> menuType, NetheritePlusModPlatform.Factory<H, S> screenSupplier) {

    }

    @ExpectPlatform
    public static <T extends Container> ContainerType<T> registerScreenHandler(ResourceLocation identifier, BiFunction<Integer, IInventory, T> menuTypeSupplier) {
        return IForgeContainerType.create((windowId, inv, data) -> menuTypeSupplier.apply(windowId, inv));
    }

}
