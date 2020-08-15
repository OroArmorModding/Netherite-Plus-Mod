package com.oroarmor.netherite_plus.client;

import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_BOW;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_CROSSBOW;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_ELYTRA;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_FISHING_ROD;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_SHIELD;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_TRIDENT;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class NetheritePlusModelProvider {
	public static void registerItemsWithModelProvider() {

		if (NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue()) {
			registerElytraModels();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue()) {
			registerShieldModels();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_FISHING_ROD.getValue()) {
			registerFishingRodModels();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_BOWS_AND_CROSSBOWS.getValue()) {
			registerBowModels();
			registerCrossbowModels();
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue()) {
			registerTridentModels();
		}
	}

	private static void registerTridentModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_TRIDENT, new Identifier("throwing"),
				(itemStack, clientWorld, livingEntity) -> {
					return livingEntity != null && livingEntity.isUsingItem()
							&& livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
				});
	}

	private static void registerElytraModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_ELYTRA, new Identifier("broken"),
				(itemStack, clientWorld, livingEntity) -> {
					return ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F;
				});
	}

	private static void registerShieldModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_SHIELD, new Identifier("blocking"),
				(itemStack, clientWorld, livingEntity) -> {
					return livingEntity != null && livingEntity.isUsingItem()
							&& livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
				});
	}

	private static void registerFishingRodModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_FISHING_ROD, new Identifier("cast"),
				(itemStack, clientWorld, livingEntity) -> {
					if (livingEntity == null) {
						return 0.0F;
					}
					boolean bl = livingEntity.getMainHandStack() == itemStack;
					boolean bl2 = livingEntity.getOffHandStack() == itemStack;
					if (livingEntity.getMainHandStack().getItem() instanceof FishingRodItem) {
						bl2 = false;
					}

					return (bl || bl2) && livingEntity instanceof PlayerEntity
							&& ((PlayerEntity) livingEntity).fishHook != null ? 1.0F : 0.0F;
				});
	}

	private static void registerBowModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pull"),
				(itemStack, clientWorld, livingEntity) -> {
					if (livingEntity == null) {
						return 0.0F;
					}
					return livingEntity.getActiveItem() != itemStack ? 0.0F
							: (itemStack.getMaxUseTime() - livingEntity.getItemUseTimeLeft()) / 20.0F;
				});

		FabricModelPredicateProviderRegistry.register(NETHERITE_BOW, new Identifier("pulling"),
				(itemStack, clientWorld, livingEntity) -> {
					if (livingEntity == null) {
						return 0.0F;
					}
					return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
				});
	}

	private static void registerCrossbowModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pull"),
				(itemStack4, clientWorld3, livingEntity2) -> {
					if (livingEntity2 == null) {
						return 0.0F;
					}
					return CrossbowItem.isCharged(itemStack4) ? 0.0F
							: (float) (itemStack4.getMaxUseTime() - livingEntity2.getItemUseTimeLeft())
									/ (float) CrossbowItem.getPullTime(itemStack4);
				});
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("pulling"),
				(itemStack3, clientWorld1, livingEntity1) -> {
					if (livingEntity1 == null) {
						return 0.0F;
					}
					return livingEntity1.isUsingItem() && livingEntity1.getActiveItem() == itemStack3
							&& !CrossbowItem.isCharged(itemStack3) ? 1.0F : 0.0F;
				});
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("charged"),
				(itemStack2, clientWorld4, livingEntity3) -> {
					if (livingEntity3 == null) {
						return 0.0F;
					}
					return CrossbowItem.isCharged(itemStack2) ? 1.0F : 0.0F;
				});

		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW, new Identifier("firework"),
				(itemStack1, clientWorld2, livingEntity4) -> {
					if (livingEntity4 == null) {
						return 0.0F;
					}
					return CrossbowItem.isCharged(itemStack1)
							&& CrossbowItem.hasProjectile(itemStack1, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
				});
	}
}
