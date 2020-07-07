package net.fabricmc.example;

import java.lang.reflect.Field;
import java.util.Map;

import com.google.common.collect.Maps;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.example.item.NetheriteElytraItem;
import net.minecraft.client.item.ModelPredicateProvider;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public class NetheriteElytraMod implements ModInitializer {
	public static final Item NETHERITE_ELYTRA = new NetheriteElytraItem(
			new Item.Settings().maxDamage(432).group(ItemGroup.TRANSPORTATION).rarity(Rarity.UNCOMMON).fireproof());

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM, new Identifier("oroarmor", "netherite_elytra"), NETHERITE_ELYTRA);
		registerNetheriteElytraAsDamagable();

		System.out.println("Hello Fabric world!");
	}

	@SuppressWarnings("unchecked")
	private void registerNetheriteElytraAsDamagable() {
		try {
			Field f = ModelPredicateProviderRegistry.class.getDeclaredField("ITEM_SPECIFIC");
			f.setAccessible(true);
			((Map<Item, Map<Identifier, ModelPredicateProvider>>) f.get(new Object()))
					.computeIfAbsent(NETHERITE_ELYTRA, (itemx) -> {
						return Maps.newHashMap();
					}).put(new Identifier("broken"), (itemStack, clientWorld, livingEntity) -> {
						return ElytraItem.isUsable(itemStack) ? 0.0F : 1.0F;
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isStackUsableAsElytra(ItemStack itemStack) {
		return isElytra(itemStack) && ElytraItem.isUsable(itemStack);
	}

	public static boolean isElytra(ItemStack itemStack) {
		return itemStack.getItem() == Items.ELYTRA || itemStack.getItem() == NetheriteElytraMod.NETHERITE_ELYTRA;
	}

}
