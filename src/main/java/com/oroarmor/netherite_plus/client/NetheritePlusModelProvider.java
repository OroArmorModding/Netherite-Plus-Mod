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

import com.oroarmor.netherite_plus.NetheritePlusMod;
import com.oroarmor.netherite_plus.config.NetheritePlusConfig;

import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusModelProvider {
	private static void registerBowModels() {
		ModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.getActiveItem() != itemStack ? 0.0F : (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
		});

		ModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
		});
	}

	private static void registerCrossbowModels() {
		ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pull"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return CrossbowItem.isCharged(itemStack) ? 0.0F : (float) (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / (float) CrossbowItem.getPullTime(itemStack);
		});
		ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pulling"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack && !CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
		});
		ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("charged"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return CrossbowItem.isCharged(itemStack) ? 1.0F : 0.0F;
		});

		ModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("firework"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return CrossbowItem.isCharged(itemStack) && CrossbowItem.hasProjectile(itemStack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
		});
	}

	private static void registerElytraModels() {
		ModelPredicateProviderRegistry.register(NETHERITE_ELYTRA, new Identifier("broken"), (itemStack, clientWorld, livingEntity, i) -> {
			return ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F;
		});
	}

	private static void registerFishingRodModels() {
		ModelPredicateProviderRegistry.register(NETHERITE_FISHING_ROD, new Identifier("cast"), (itemStack, clientWorld, livingEntity, i) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			boolean bl = livingEntity.getMainHandStack() == itemStack;
			boolean bl2 = livingEntity.getOffHandStack() == itemStack;
			if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) {
				bl2 = false;
			}

			return (bl || bl2) && livingEntity instanceof PlayerEntity && ((PlayerEntity) livingEntity).fishHook != null ? 1.0F : 0.0F;
		});
	}

	public static void registerItemsWithModelProvider() {
		if (NetheritePlusMod.CONFIG.enabled.elytra) {
			registerElytraModels();
		}

		if (NetheritePlusMod.CONFIG.enabled.shields) {
			registerShieldModels();
		}

		if (NetheritePlusMod.CONFIG.enabled.fishing_rod) {
			registerFishingRodModels();
		}

		if (NetheritePlusMod.CONFIG.enabled.bows_and_crossbows) {
			registerBowModels();
			registerCrossbowModels();
		}

		if (NetheritePlusMod.CONFIG.enabled.trident) {
			registerTridentModels();
		}
	}

	private static void registerShieldModels() {
		ModelPredicateProviderRegistry.register(NETHERITE_SHIELD, new Identifier("blocking"), (itemStack, clientWorld, livingEntity, i) -> {
			return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
		});
	}

	private static void registerTridentModels() {
		ModelPredicateProviderRegistry.register(NETHERITE_TRIDENT, new Identifier("throwing"), (itemStack, clientWorld, livingEntity, i) -> {
			return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
		});
	}
}
