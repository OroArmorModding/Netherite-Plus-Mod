package com.oroarmor.netherite_plus.fabric;

import java.io.File;
import java.util.function.BiFunction;

import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.compatibility.NetheritePlusTrinketsCompatibilty;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import com.oroarmor.netherite_plus.network.UpdateNetheriteBeaconC2SPacket;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.mixin.object.builder.CriteriaAccessor;
import net.fabricmc.loader.api.FabricLoader;
import static com.oroarmor.netherite_plus.config.NetheritePlusConfig.CONFIG_FILE_NAME;

public class NetheritePlusModPlatformImpl {
    public static void sendBeaconUpdatePacket(FriendlyByteBuf buf){
        ClientPlayNetworking.send(UpdateNetheriteBeaconC2SPacket.ID, buf);
    }

    public static <T extends CriterionTrigger<?>> T registerCriteria(T object) {
        return CriteriaAccessor.callRegister(object);
    }

    public static Item getElytraItem(Item.Properties elytraSettings) {
        return !FabricLoader.getInstance().isModLoaded("trinkets") ? new NetheriteElytraItem(elytraSettings) : NetheritePlusTrinketsCompatibilty.getTrinketsElytra(elytraSettings);
    }

    public static File getConfigDir() {
        return new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME);
    }

    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<? extends H> menuType, NetheritePlusModPlatform.Factory<H, S> screenSupplier) {
        ScreenRegistry.register(menuType, (H h, Inventory i, Component t) -> screenSupplier.create(h, i, t));
    }

    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandler(ResourceLocation identifier, BiFunction<Integer, Inventory, T> menuTypeSupplier){
        return ScreenHandlerRegistry.registerSimple(identifier, (integer, inventory) -> menuTypeSupplier.apply(integer, inventory));
    }
}
