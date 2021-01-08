package com.oroarmor.netherite_plus;

import java.io.File;
import java.util.function.BiFunction;

import me.shedaniel.architectury.ExpectPlatform;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

public class NetheritePlusModPlatform {
    @ExpectPlatform
    public static <T extends CriterionTrigger<?>> T registerCriteria(T object) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Item getElytraItem(Item.Properties elytraSettings) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static File getConfigDir() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Item.Properties setISTER(Item.Properties properties) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> void registerScreen(MenuType<?> menuType, Factory<H, S> screenSupplier) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends AbstractContainerMenu> MenuType<T> registerScreenHandler(ResourceLocation identifier, BiFunction<Integer, Inventory, T> menuTypeSupplier) {
        throw new AssertionError();
    }

    @FunctionalInterface
    public interface Factory<H extends AbstractContainerMenu, S extends Screen & MenuAccess<H>> {
        S create(H var1, Inventory var2, Component var3);
    }
}
