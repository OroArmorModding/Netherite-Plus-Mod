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

package com.oroarmor.netherite_plus.client;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

import com.oroarmor.netherite_plus.NetheritePlusMod;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Items;

public class NetheritePlusModelProvider {
    private static void registerBowModels() {
        ItemProperties.register(NETHERITE_BOW, new ResourceLocation("pull"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.getUseItem() != itemStack ? 0.0F : (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
        });

        ItemProperties.register(NETHERITE_BOW, new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    private static void registerCrossbowModels() {
        ItemProperties.register(NETHERITE_CROSSBOW, new ResourceLocation("pull"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(itemStack);
        });
        ItemProperties.register(NETHERITE_CROSSBOW, new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });
        ItemProperties.register(NETHERITE_CROSSBOW, new ResourceLocation("charged"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
        });

        ItemProperties.register(NETHERITE_CROSSBOW, new ResourceLocation("firework"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return CrossbowItem.isCharged(itemStack) && CrossbowItem.containsChargedProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
        });
    }

    private static void registerElytraModels() {
        ItemProperties.register(NETHERITE_ELYTRA, new ResourceLocation("broken"), (itemStack, clientWorld, livingEntity, i) -> {
            return ElytraItem.isFlyEnabled(itemStack) ? 0.0F : 1.0F;
        });
    }

    private static void registerFishingRodModels() {
        ItemProperties.register(NETHERITE_FISHING_ROD, new ResourceLocation("cast"), (itemStack, clientWorld, livingEntity, i) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            boolean bl = livingEntity.getMainHandItem() == itemStack;
            boolean bl2 = livingEntity.getOffhandItem() == itemStack;
            if (livingEntity.getMainHandItem().getItem() instanceof FishingRodItem) {
                bl2 = false;
            }

            return (bl || bl2) && livingEntity instanceof Player && ((Player) livingEntity).fishing != null ? 1.0F : 0.0F;
        });
    }

    public static void registerItemsWithModelProvider() {
        if (NetheritePlusMod.CONFIG.enabled.elytra.value()) {
            registerElytraModels();
        }

        if (NetheritePlusMod.CONFIG.enabled.shields.value()) {
//			registerShieldModels();
        }

        if (NetheritePlusMod.CONFIG.enabled.fishing_rod.value()) {
            registerFishingRodModels();
        }

        if (NetheritePlusMod.CONFIG.enabled.bows_and_crossbows.value()) {
            registerBowModels();
            registerCrossbowModels();
        }

        if (NetheritePlusMod.CONFIG.enabled.trident.value()) {
            registerTridentModels();
        }
    }

    private static void registerShieldModels() {
        ItemProperties.register(NETHERITE_SHIELD, new ResourceLocation("blocking"), (itemStack, clientWorld, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    private static void registerTridentModels() {
        ItemProperties.register(NETHERITE_TRIDENT, new ResourceLocation("throwing"), (itemStack, clientWorld, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
        });
    }
}
