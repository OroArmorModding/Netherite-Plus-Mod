package com.oroarmor.netherite_plus;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.oroarmor.util.config.Config;
import com.oroarmor.util.config.ConfigItem;
import com.oroarmor.util.config.ConfigItemGroup;

import net.fabricmc.loader.api.FabricLoader;

public final class NetheritePlusConfigManager {
	private static final String CONFIG_FILE_NAME = "netherite_plus.json";

	public static class ENABLED {
		public static final ConfigItem<Boolean> ENABLED_SHULKER_BOXES = new ConfigItem<Boolean>("shulker_boxes", true,
				"Enable or disable shulker boxes", ConfigItem.Type.BOOLEAN);

		public static final ConfigItem<Boolean> ENABLED_ELYTRA = new ConfigItem<Boolean>("elytra", true,
				"Enable or disable elytra", ConfigItem.Type.BOOLEAN);

		public static final ConfigItem<Boolean> ENABLED_SHIELDS = new ConfigItem<Boolean>("shields", true,
				"Enable or disable shields", ConfigItem.Type.BOOLEAN);

		public static final ConfigItem<Boolean> ENABLED_BOWS_AND_CROSSBOWS = new ConfigItem<Boolean>(
				"bows_and_crossbows", true, "Enable or disable bows and crossbows", ConfigItem.Type.BOOLEAN);

		public static final ConfigItem<Boolean> ENABLED_FISHING_ROD = new ConfigItem<Boolean>("fishing_rod", true,
				"Enable or disable fishing rod", ConfigItem.Type.BOOLEAN);

		public static final ConfigItem<Boolean> ENABLED_HORSE_ARMOR = new ConfigItem<Boolean>("horse_armor", true,
				"Enable or disable horse armor", ConfigItem.Type.BOOLEAN);

		public static final ConfigItem<Boolean> ENABLED_FAKE_NETHERITE_BLOCKS = new ConfigItem<Boolean>(
				"fake_netherite_blocks", true, "Enable or disable fake netherite blocks", ConfigItem.Type.BOOLEAN);

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(ENABLED_SHULKER_BOXES, ENABLED_ELYTRA,
				ENABLED_SHIELDS, ENABLED_BOWS_AND_CROSSBOWS, ENABLED_FISHING_ROD, ENABLED_HORSE_ARMOR,
				ENABLED_FAKE_NETHERITE_BLOCKS);

		public static final ConfigItemGroup ENABLED_CONFIGS = new ConfigItemGroup(OPTIONS, "enabled");

	}

	public static class DURABILITIES {
		public static final ConfigItem<Integer> ELYTRA_DURABILITY = new ConfigItem<Integer>("elytra", 864,
				"Elytra Durability", ConfigItem.Type.INTEGER);

		public static final ConfigItem<Integer> FISHING_ROD_DURABILITY = new ConfigItem<Integer>("fishing_rod", 128,
				"Fishing Rod Durability", ConfigItem.Type.INTEGER);

		public static final ConfigItem<Integer> SHIELD_DURABILITY = new ConfigItem<Integer>("shield", 672,
				"Shield Durability", ConfigItem.Type.INTEGER);

		public static final ConfigItem<Integer> BOW_DURABILITY = new ConfigItem<Integer>("bow", 768, "Bow Durability",
				ConfigItem.Type.INTEGER);

		public static final ConfigItem<Integer> CROSSBOW_DURABILITY = new ConfigItem<Integer>("crossbow", 652,
				"Crossbow Durability", ConfigItem.Type.INTEGER);

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(ELYTRA_DURABILITY, FISHING_ROD_DURABILITY,
				SHIELD_DURABILITY, BOW_DURABILITY, CROSSBOW_DURABILITY);

		public static final ConfigItemGroup DURABILITIES_CONFIGS = new ConfigItemGroup(OPTIONS, "durabilities");
	}

	public static class DAMAGE {
		public static final ConfigItem<Double> BOW_DAMAGE_ADDITION = new ConfigItem<Double>("bow_damage_addition", 0d,
				"Damage added to the bow after the multiplier", ConfigItem.Type.DOUBLE);
		public static final ConfigItem<Double> CROSSBOW_DAMAGE_ADDITION = new ConfigItem<Double>(
				"crossbow_damage_addition", 0d, "Damage added to the crossbow after the multiplier",
				ConfigItem.Type.DOUBLE);
		public static final ConfigItem<Double> BOW_DAMAGE_MULTIPLIER = new ConfigItem<Double>("bow_damage_multiplier",
				1d, "Multiplier of the normal bow damage", ConfigItem.Type.DOUBLE);
		public static final ConfigItem<Double> CROSSBOW_DAMAGE_MULTIPLIER = new ConfigItem<Double>(
				"crossbow_damage_multiplier", 1d, "Multiplier of the normal crossbow damage", ConfigItem.Type.DOUBLE);

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(BOW_DAMAGE_ADDITION, BOW_DAMAGE_MULTIPLIER,
				CROSSBOW_DAMAGE_ADDITION, CROSSBOW_DAMAGE_MULTIPLIER);

		public static final ConfigItemGroup DAMAGE_CONFIGS = new ConfigItemGroup(OPTIONS, "damage");
	}

	private static final List<ConfigItemGroup> CONFIGS = ImmutableList.of(ENABLED.ENABLED_CONFIGS,
			DURABILITIES.DURABILITIES_CONFIGS, DAMAGE.DAMAGE_CONFIGS);

	public static final Config config = new Config(CONFIGS,
			new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME));

}
