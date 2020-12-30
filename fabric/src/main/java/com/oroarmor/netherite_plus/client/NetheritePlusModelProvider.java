package com.oroarmor.netherite_plus.client;

import com.oroarmor.netherite_plus.config.NetheritePlusConfig;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Items;

import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.*;

public class NetheritePlusModelProvider {
	private static void registerBowModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_BOW.get(), new ResourceLocation("pull"), (itemStack, clientWorld, livingEntity) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.getUseItem() != itemStack ? 0.0F : (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
		});

		FabricModelPredicateProviderRegistry.register(NETHERITE_BOW.get(), new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity) -> {
			if (livingEntity == null) {
				return 0.0F;
			}
			return livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
		});
	}

	private static void registerCrossbowModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW.get(), new ResourceLocation("pull"), (itemStack4, clientWorld3, livingEntity2) -> {
			if (livingEntity2 == null) {
				return 0.0F;
			}
			return CrossbowItem.isCharged(itemStack4) ? 0.0F : (float) (itemStack4.getUseDuration() - livingEntity2.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(itemStack4);
		});
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW.get(), new ResourceLocation("pulling"), (itemStack3, clientWorld1, livingEntity1) -> {
			if (livingEntity1 == null) {
				return 0.0F;
			}
			return livingEntity1.isUsingItem() && livingEntity1.getUseItem() == itemStack3 && !CrossbowItem.isCharged(itemStack3) ? 1.0F : 0.0F;
		});
		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW.get(), new ResourceLocation("charged"), (itemStack2, clientWorld4, livingEntity3) -> {
			if (livingEntity3 == null) {
				return 0.0F;
			}
			return CrossbowItem.isCharged(itemStack2) ? 1.0F : 0.0F;
		});

		FabricModelPredicateProviderRegistry.register(NETHERITE_CROSSBOW.get(), new ResourceLocation("firework"), (itemStack1, clientWorld2, livingEntity4) -> {
			if (livingEntity4 == null) {
				return 0.0F;
			}
			return CrossbowItem.isCharged(itemStack1) && CrossbowItem.containsChargedProjectile(itemStack1, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F;
		});
	}

	private static void registerElytraModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_ELYTRA.get(), new ResourceLocation("broken"), (itemStack, clientWorld, livingEntity) -> {
			return ElytraItem.isFlyEnabled(itemStack) ? 0.0F : 1.0F;
		});
	}

	private static void registerFishingRodModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_FISHING_ROD.get(), new ResourceLocation("cast"), (itemStack, clientWorld, livingEntity) -> {
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

	private static void registerShieldModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_SHIELD.get(), new ResourceLocation("blocking"), (itemStack, clientWorld, livingEntity) -> {
			return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
		});
	}

	private static void registerTridentModels() {
		FabricModelPredicateProviderRegistry.register(NETHERITE_TRIDENT.get(), new ResourceLocation("throwing"), (itemStack, clientWorld, livingEntity) -> {
			return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F;
		});
	}
}
