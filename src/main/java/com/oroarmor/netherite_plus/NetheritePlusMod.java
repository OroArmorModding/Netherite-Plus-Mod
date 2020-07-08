package com.oroarmor.netherite_plus;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.Maps;
import com.oroarmor.netherite_plus.item.NetheriteElytraItem;
import com.oroarmor.netherite_plus.item.NetheriteFishingRodItem;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class NetheritePlusMod implements ModInitializer {
	public static final Item NETHERITE_ELYTRA = new NetheriteElytraItem(
			new Item.Settings().maxDamage(864).group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON).fireproof());
	public static final Item NETHERITE_FISHING_ROD = new NetheriteFishingRodItem(
			(new Item.Settings()).maxDamage(128).group(ItemGroup.TOOLS).fireproof());

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("netherite_plus", "netherite_elytra"), NETHERITE_ELYTRA);
		Registry.register(Registry.ITEM, new Identifier("netherite_plus", "netherite_fishing_rod"),
				NETHERITE_FISHING_ROD);
		registerItemsAsDamagable();
	}

	private void registerItemsAsDamagable() {
		try {
			Field f = ModelPredicateProviderRegistry.class.getDeclaredField("ITEM_SPECIFIC");
			f.setAccessible(true);
			registerElytraAsDamageable(f);
			registerFishingRodAsDamageable(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void registerElytraAsDamageable(Field f) throws IllegalAccessException {
		((Map<Item, Map<Identifier, ModelPredicateProvider>>) f.get(new Object()))
				.computeIfAbsent(NETHERITE_ELYTRA, (itemx) -> {
					return Maps.newHashMap();
				}).put(new Identifier("broken"), (itemStack, clientWorld, livingEntity) -> {
					return ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F;
				});
	}

	@SuppressWarnings("unchecked")
	private void registerFishingRodAsDamageable(Field f) throws IllegalAccessException {
		((Map<Item, Map<Identifier, ModelPredicateProvider>>) f.get(new Object()))
				.computeIfAbsent(NETHERITE_FISHING_ROD, (itemx) -> {
					return Maps.newHashMap();
				}).put(new Identifier("cast"), (itemStack, clientWorld, livingEntity) -> {
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
}
