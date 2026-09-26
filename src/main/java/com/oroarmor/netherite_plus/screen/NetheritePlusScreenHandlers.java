/*
 * MIT License
 *
 * Copyright (c) 2021-2023 OroArmor (Eli Orona)
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

package com.oroarmor.netherite_plus.screen;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteBeaconScreen;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class NetheritePlusScreenHandlers {

    public static MenuType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;
    public static MenuType<NetheriteBeaconScreenHandler> NETHERITE_BEACON;

    static {
        NETHERITE_ANVIL = register(id("netherite_anvil"), NetheriteAnvilScreenHandler::new);
        NETHERITE_BEACON = register(id("netherite_beacon"), NetheriteBeaconScreenHandler::new);
    }

    public static <T extends AbstractContainerMenu> MenuType<T> register(ResourceLocation id, MenuType.MenuSupplier<T> factory) {
        return Registry.register(BuiltInRegistries.MENU, id, new MenuType<>(factory, FeatureFlagSet.of()));
    }

    public static void init() {
    }

    public static void initializeClient() {
        MenuScreens.register(NetheritePlusScreenHandlers.NETHERITE_ANVIL, NetheriteAnvilScreen::new);
        MenuScreens.register(NetheritePlusScreenHandlers.NETHERITE_BEACON, NetheriteBeaconScreen::new);
    }
}
