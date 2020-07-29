package com.oroarmor.netherite_plus;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.util.JsonUtils;
import net.fabricmc.loader.api.FabricLoader;

public final class NetheritePlusConfigManager {
	private static final String CONFIG_FILE_NAME = "netherite_plus.json";

	public static class ENABLED {
		public static final ConfigBoolean ENABLED_CONFIG_CONSOLE_LOG = new ConfigBoolean("config_console_log", true,
				"Enabled console logs for config");

		public static final ConfigBoolean ENABLED_SHULKER_BOXES = new ConfigBoolean("shulker_boxes", true,
				"Enable or disable shulker boxes");

		public static final ConfigBoolean ENABLED_ELYTRA = new ConfigBoolean("elytra", true,
				"Enable or disable elytra");

		public static final ConfigBoolean ENABLED_SHIELDS = new ConfigBoolean("shields", true,
				"Enable or disable shields");

		public static final ConfigBoolean ENABLED_BOWS_AND_CROSSBOWS = new ConfigBoolean("bows_and_crossbows", true,
				"Enable or disable bows and crossbows");

		public static final ConfigBoolean ENABLED_FISHING_ROD = new ConfigBoolean("fishing_rod", true,
				"Enable or disable fishing rod");

		public static final ConfigBoolean ENABLED_HORSE_ARMOR = new ConfigBoolean("horse_armor", true,
				"Enable or disable horse armor");

		public static final ConfigBoolean ENABLED_FAKE_NETHERITE_BLOCKS = new ConfigBoolean("fake_netherite_blocks",
				true, "Enable or disable fake netherite blocks");

		public static final List<ConfigBoolean> OPTIONS = ImmutableList.of(ENABLED_CONFIG_CONSOLE_LOG,
				ENABLED_SHULKER_BOXES, ENABLED_ELYTRA, ENABLED_SHIELDS, ENABLED_BOWS_AND_CROSSBOWS, ENABLED_FISHING_ROD,
				ENABLED_HORSE_ARMOR, ENABLED_FAKE_NETHERITE_BLOCKS);

	}

	public static class DURABILITIES {
		public static final ConfigInteger ELYTRA_DURABILITY = new ConfigInteger("elytra", 864, "Elytra Durability");

		public static final ConfigInteger FISHING_ROD_DURABILITY = new ConfigInteger("fishing_rod", 128,
				"Fishing Rod Durability");

		public static final ConfigInteger SHIELD_DURABILITY = new ConfigInteger("shield", 672, "Shield Durability");

		public static final ConfigInteger BOW_DURABILITY = new ConfigInteger("bow", 768, "Bow Durability");

		public static final ConfigInteger CROSSBOW_DURABILITY = new ConfigInteger("crossbow", 652,
				"Crossbow Durability");

		public static final List<ConfigInteger> OPTIONS = ImmutableList.of(ELYTRA_DURABILITY, FISHING_ROD_DURABILITY,
				SHIELD_DURABILITY, BOW_DURABILITY, CROSSBOW_DURABILITY);
	}

	public static class DAMAGE {
		public static final ConfigDouble BOW_DAMAGE_ADDITION = new ConfigDouble("bow_damage_addition", 0,
				"Damage added to the bow after the multiplier");
		public static final ConfigDouble CROSSBOW_DAMAGE_ADDITION = new ConfigDouble("crossbow_damage_addition", 0,
				"Damage added to the crossbow after the multiplier");
		public static final ConfigDouble BOW_DAMAGE_MULTIPLIER = new ConfigDouble("bow_damage_multiplier", 1,
				"Multiplier of the normal bow damage");
		public static final ConfigDouble CROSSBOW_DAMAGE_MULTIPLIER = new ConfigDouble("corssbow_damage_multiplier", 1,
				"Multiplier of the normal crossbow damage");

		public static final List<ConfigDouble> OPTIONS = ImmutableList.of(BOW_DAMAGE_ADDITION, BOW_DAMAGE_MULTIPLIER,
				CROSSBOW_DAMAGE_ADDITION, CROSSBOW_DAMAGE_MULTIPLIER);
	}

	public static void load() {
		File configFile = new File(getConfigDirectory(), CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "enabled", NetheritePlusConfigManager.ENABLED.OPTIONS);

				ConfigUtils.readConfigBase(root, "durabilities", NetheritePlusConfigManager.DURABILITIES.OPTIONS);

				ConfigUtils.readConfigBase(root, "damage", NetheritePlusConfigManager.DAMAGE.OPTIONS);
			}
		} else {
			save();
		}

		if (NetheritePlusConfigManager.ENABLED.ENABLED_CONFIG_CONSOLE_LOG.getBooleanValue()) {
			NetheritePlusConfigManager.ENABLED.OPTIONS
					.forEach(c -> System.out.println("enabled:netherite_" + c.getName() + ":" + c.getBooleanValue()));

			NetheritePlusConfigManager.DURABILITIES.OPTIONS.forEach(
					c -> System.out.println("durability:netherite_" + c.getName() + ":" + c.getIntegerValue()));

			NetheritePlusConfigManager.DAMAGE.OPTIONS.forEach(
					c -> System.out.println("damage:netherite_" + c.getName() + ":" + c.getDefaultDoubleValue()));
		}
	}

	public static void save() {
		File dir = getConfigDirectory();

		if (dir.exists() && dir.isDirectory() || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "enabled", NetheritePlusConfigManager.ENABLED.OPTIONS);
			ConfigUtils.writeConfigBase(root, "durabilities", NetheritePlusConfigManager.DURABILITIES.OPTIONS);
			ConfigUtils.writeConfigBase(root, "damage", NetheritePlusConfigManager.DAMAGE.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
		}
	}

	private static File getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir().toFile();
	}

}
