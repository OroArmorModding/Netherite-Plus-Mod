package com.oroarmor.netherite_plus.forge;

import java.io.File;
import java.util.function.BiFunction;

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.NetheritePlusModPlatform;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import me.shedaniel.architectury.ExpectPlatform;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

public class NetheritePlusModPlatformImpl {

    public static void sendBeaconUpdatePacket(FriendlyByteBuf buf) {
    }

    public static <T extends CriterionTrigger<?>> T registerCriteria(T object) {
        return null;
    }

    public static Item getElytraItem(Item.Properties elytraSettings) {
        return new NetheriteElytraItem(elytraSettings);
    }

    public static File getConfigDir() {
        return new File(FMLPaths.CONFIGDIR.get().toFile(), NetheritePlusConfig.CONFIG_FILE_NAME);
    }


    @ExpectPlatform
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<? extends H> menuType, NetheritePlusModPlatform.Factory<H, S> screenSupplier) {
        MenuScreens.register(menuType, (H t, Inventory i, Component c) -> screenSupplier.create(t, i, c));
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandler(ResourceLocation identifier, BiFunction<Integer, Inventory, T> menuTypeSupplier) {
        MenuType<T> menuType = IForgeContainerType.create((windowId, inv, data) -> menuTypeSupplier.apply(windowId, inv));
        NetheritePlusMod.REGISTRIES.get().get(Registry.MENU_REGISTRY).registerSupplied(identifier, () -> menuType);
        return menuType;
    }

}
