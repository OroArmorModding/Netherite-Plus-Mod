package com.oroarmor.netherite_plus.config;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.oroarmor.util.config.Config;
import com.oroarmor.util.config.ConfigItem;
import com.oroarmor.util.config.ConfigItemGroup;

import net.fabricmc.loader.api.FabricLoader;

public final class NetheritePlusConfig extends Config {
	public static class ANVIL extends ConfigItemGroup {
		public static final ConfigItem<Double> XP_REDUCTION = new ConfigItem<Double>("xp_reduction", 0.5,
				"XP Reduction in anvil");

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(XP_REDUCTION);

		public ANVIL() {
			super(OPTIONS, "anvil");
		}

	}

	public static class DAMAGE extends ConfigItemGroup {
		public static final ConfigItem<Double> BOW_DAMAGE_ADDITION = new ConfigItem<Double>("bow_damage_addition", 0d,
				"Damage added to the bow after the multiplier");

		public static final ConfigItem<Double> BOW_DAMAGE_MULTIPLIER = new ConfigItem<Double>("bow_damage_multiplier",
				1d, "Multiplier of the normal bow damage");
		public static final ConfigItem<Double> CROSSBOW_DAMAGE_ADDITION = new ConfigItem<Double>(
				"crossbow_damage_addition", 0d, "Damage added to the crossbow after the multiplier");
		public static final ConfigItem<Double> CROSSBOW_DAMAGE_MULTIPLIER = new ConfigItem<Double>(
				"crossbow_damage_multiplier", 1d, "Multiplier of the normal crossbow damage");
		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(BOW_DAMAGE_ADDITION, BOW_DAMAGE_MULTIPLIER,
				CROSSBOW_DAMAGE_ADDITION, CROSSBOW_DAMAGE_MULTIPLIER);

		public DAMAGE() {
			super(OPTIONS, "damage");
		}

	}

	public static class DURABILITIES extends ConfigItemGroup {
		public static final ConfigItem<Integer> BOW_DURABILITY = new ConfigItem<Integer>("bow", 768, "Bow Durability");

		public static final ConfigItem<Integer> CROSSBOW_DURABILITY = new ConfigItem<Integer>("crossbow", 652,
				"Crossbow Durability");

		public static final ConfigItem<Integer> ELYTRA_DURABILITY = new ConfigItem<Integer>("elytra", 864,
				"Elytra Durability");

		public static final ConfigItem<Integer> FISHING_ROD_DURABILITY = new ConfigItem<Integer>("fishing_rod", 128,
				"Fishing Rod Durability");

		public static final ConfigItem<Integer> SHIELD_DURABILITY = new ConfigItem<Integer>("shield", 672,
				"Shield Durability");

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(ELYTRA_DURABILITY, FISHING_ROD_DURABILITY,
				SHIELD_DURABILITY, BOW_DURABILITY, CROSSBOW_DURABILITY);

		public DURABILITIES() {
			super(OPTIONS, "durabilities");
		}

	}

	public static class ENABLED extends ConfigItemGroup {
		public static final ConfigItem<Boolean> ENABLED_CONFIG_PRINT = new ConfigItem<Boolean>("config_debug_print",
				false, "Prints the Config on game start");

		public static final ConfigItem<Boolean> ENABLED_ANVIL = new ConfigItem<Boolean>("anvils", true,
				"Enable or disable anvils");

		public static final ConfigItem<Boolean> ENABLED_BOWS_AND_CROSSBOWS = new ConfigItem<Boolean>(
				"bows_and_crossbows", true, "Enable or disable bows and crossbows");

		public static final ConfigItem<Boolean> ENABLED_ELYTRA = new ConfigItem<Boolean>("elytra", true,
				"Enable or disable elytra");

		public static final ConfigItem<Boolean> ENABLED_FAKE_NETHERITE_BLOCKS = new ConfigItem<Boolean>(
				"fake_netherite_blocks", true, "Enable or disable fake netherite blocks");

		public static final ConfigItem<Boolean> ENABLED_FISHING_ROD = new ConfigItem<Boolean>("fishing_rod", true,
				"Enable or disable fishing rod");

		public static final ConfigItem<Boolean> ENABLED_HORSE_ARMOR = new ConfigItem<Boolean>("horse_armor", true,
				"Enable or disable horse armor");

		public static final ConfigItem<Boolean> ENABLED_SHIELDS = new ConfigItem<Boolean>("shields", true,
				"Enable or disable shields");

		public static final ConfigItem<Boolean> ENABLED_SHULKER_BOXES = new ConfigItem<Boolean>("shulker_boxes", true,
				"Enable or disable shulker boxes");

		public static final List<ConfigItem<?>> OPTIONS = ImmutableList.of(ENABLED_SHULKER_BOXES, ENABLED_ELYTRA,
				ENABLED_SHIELDS, ENABLED_BOWS_AND_CROSSBOWS, ENABLED_FISHING_ROD, ENABLED_HORSE_ARMOR,
				ENABLED_FAKE_NETHERITE_BLOCKS, ENABLED_ANVIL, ENABLED_CONFIG_PRINT);

		public ENABLED() {
			super(OPTIONS, "enabled");
		}
	}

	private static final String CONFIG_FILE_NAME = "netherite_plus.json";

	private static final List<ConfigItemGroup> CONFIGS = ImmutableList.of(new ENABLED(), new DURABILITIES(),
			new DAMAGE(), new ANVIL());

	public NetheritePlusConfig() {
		super(CONFIGS, new File(FabricLoader.getInstance().getConfigDir().toFile(), CONFIG_FILE_NAME));
	}

}
