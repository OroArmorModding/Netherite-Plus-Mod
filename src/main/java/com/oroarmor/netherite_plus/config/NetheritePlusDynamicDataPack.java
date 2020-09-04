package com.oroarmor.netherite_plus.config;

import static com.oroarmor.netherite_plus.NetheritePlusMod.MOD_ID;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.FAKE_NETHERITE_BLOCK;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_ANVIL_ITEM;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_BEACON;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_BOW;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_CROSSBOW;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_ELYTRA;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_FISHING_ROD;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_HORSE_ARMOR;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_SHIELD;
import static com.oroarmor.netherite_plus.item.NetheritePlusItems.NETHERITE_TRIDENT;
import static java.util.Arrays.stream;
import static net.devtech.arrp.api.RuntimeResourcePack.id;
import static net.devtech.arrp.json.recipe.JIngredient.ingredient;
import static net.devtech.arrp.json.recipe.JKeys.keys;
import static net.devtech.arrp.json.recipe.JPattern.pattern;
import static net.devtech.arrp.json.recipe.JRecipe.shaped;
import static net.devtech.arrp.json.recipe.JRecipe.smithing;
import static net.devtech.arrp.json.recipe.JResult.item;
import static net.devtech.arrp.json.tags.JTag.tag;
import static net.minecraft.item.Items.BEACON;
import static net.minecraft.item.Items.BOW;
import static net.minecraft.item.Items.CROSSBOW;
import static net.minecraft.item.Items.DIAMOND_HORSE_ARMOR;
import static net.minecraft.item.Items.ELYTRA;
import static net.minecraft.item.Items.FISHING_ROD;
import static net.minecraft.item.Items.IRON_BLOCK;
import static net.minecraft.item.Items.NETHERITE_BLOCK;
import static net.minecraft.item.Items.NETHERITE_INGOT;
import static net.minecraft.item.Items.SHIELD;
import static net.minecraft.item.Items.TRIDENT;

import com.oroarmor.netherite_plus.block.NetheriteShulkerBoxBlock;

import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.util.DyeColor;

public class NetheritePlusDynamicDataPack {

	public static final RuntimeResourcePack DataPack = RuntimeResourcePack.create(MOD_ID + ":dynamic_datapack");

	public static void init() {

		if (NetheritePlusConfig.ENABLED.ENABLED_FAKE_NETHERITE_BLOCKS.getValue()) {
			DataPack.addTag(id("minecraft:blocks/beacon_base_blocks"), tag().add(id(MOD_ID, "fake_netherite_block")));

			DataPack.addRecipe(id(MOD_ID + ":fake_netherite_block"),
					shaped(pattern("###", "#I#", "###"),
							keys().key("#", ingredient().item(IRON_BLOCK)).key("I", ingredient().item(NETHERITE_INGOT)),
							item(FAKE_NETHERITE_BLOCK)));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_ANVIL.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_anvil"), shaped(pattern("###", " I ", "III"),
					keys().key("#", ingredient().item(NETHERITE_BLOCK)).key("I", ingredient().item(NETHERITE_INGOT)),
					item(NETHERITE_ANVIL_ITEM)));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_SHULKER_BOXES.getValue()) {
			stream(DyeColor.values()).forEach(NetheritePlusDynamicDataPack::createShulkerRecipe);

			DataPack.addRecipe(id(MOD_ID + ":netherite_shulker_box"),
					smithing(ingredient().item(ShulkerBoxBlock.get(null).asItem()), ingredient().item(NETHERITE_INGOT),
							item(NetheriteShulkerBoxBlock.get(null).asItem())));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_BOWS_AND_CROSSBOWS.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_bow"),
					smithing(ingredient().item(BOW), ingredient().item(NETHERITE_INGOT), item(NETHERITE_BOW)));

			DataPack.addRecipe(id(MOD_ID + ":netherite_crossbow"), smithing(ingredient().item(CROSSBOW),
					ingredient().item(NETHERITE_INGOT), item(NETHERITE_CROSSBOW)));
		}

		if (NetheritePlusConfig.ENABLED.ENABLED_ELYTRA.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_eytra"),
					smithing(ingredient().item(ELYTRA), ingredient().item(NETHERITE_INGOT), item(NETHERITE_ELYTRA)));

		}
		if (NetheritePlusConfig.ENABLED.ENABLED_HORSE_ARMOR.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_horse_armor"), smithing(ingredient().item(DIAMOND_HORSE_ARMOR),
					ingredient().item(NETHERITE_INGOT), item(NETHERITE_HORSE_ARMOR)));

		}
		if (NetheritePlusConfig.ENABLED.ENABLED_FISHING_ROD.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_fishing_rod"), smithing(ingredient().item(FISHING_ROD),
					ingredient().item(NETHERITE_INGOT), item(NETHERITE_FISHING_ROD)));

		}
		if (NetheritePlusConfig.ENABLED.ENABLED_SHIELDS.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_shield"),
					smithing(ingredient().item(SHIELD), ingredient().item(NETHERITE_INGOT), item(NETHERITE_SHIELD)));

		}

		if (NetheritePlusConfig.ENABLED.ENABLED_BEACON.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_beacon"),
					shaped(pattern("III", "IBI", "NNN"), keys().key("I", ingredient().item(NETHERITE_INGOT))
							.key("B", ingredient().item(BEACON)).key("N", ingredient().item(NETHERITE_BLOCK)),
							item(NETHERITE_BEACON)));

		}

		if (NetheritePlusConfig.ENABLED.ENABLED_TRIDENT.getValue()) {
			DataPack.addRecipe(id(MOD_ID + ":netherite_trident"),
					smithing(ingredient().item(TRIDENT), ingredient().item(NETHERITE_INGOT), item(NETHERITE_TRIDENT)));

		}

		RRPCallback.EVENT.register(a -> a.add(DataPack));
	}

	public static void createShulkerRecipe(DyeColor color) {
		DataPack.addRecipe(id(MOD_ID + ":netherite_" + color.getName() + "_shulker_box"),
				smithing(ingredient().item(ShulkerBoxBlock.get(color).asItem()), ingredient().item(NETHERITE_INGOT),
						item(NetheriteShulkerBoxBlock.get(color).asItem())));
	}

}
