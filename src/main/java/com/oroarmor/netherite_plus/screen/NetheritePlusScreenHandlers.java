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

import com.oroarmor.netherite_plus.client.gui.screen.NetheriteAnvilScreen;
import com.oroarmor.netherite_plus.client.gui.screen.NetheriteBeaconScreen;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.feature_flags.FeatureFlagBitSet;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.NetheritePlusMod.id;

public class NetheritePlusScreenHandlers {

    public static ScreenHandlerType<NetheriteAnvilScreenHandler> NETHERITE_ANVIL;
    public static ScreenHandlerType<NetheriteBeaconScreenHandler> NETHERITE_BEACON;

    static {
        NETHERITE_ANVIL = register(id("netherite_anvil"), NetheriteAnvilScreenHandler::new);
        NETHERITE_BEACON = register(id("netherite_beacon"), NetheriteBeaconScreenHandler::new);
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> register(Identifier id, ScreenHandlerType.Factory<T> factory) {
        return Registry.register(Registries.SCREEN_HANDLER_TYPE, id, new ScreenHandlerType<>(factory, FeatureFlagBitSet.empty()));
    }

    public static void init() {
    }

    public static void initializeClient() {
        HandledScreens.register(NetheritePlusScreenHandlers.NETHERITE_ANVIL, NetheriteAnvilScreen::new);
        HandledScreens.register(NetheritePlusScreenHandlers.NETHERITE_BEACON, NetheriteBeaconScreen::new);
    }
}
