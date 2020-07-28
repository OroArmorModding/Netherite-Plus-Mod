package com.oroarmor.netherite_plus;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.util.JsonUtils;
import net.fabricmc.loader.api.FabricLoader;

public class NetheritePlusConfigManager {
	public static class NetheritePlusConfiguration {
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

		public static final List<IConfigBase> OPTIONS = ImmutableList.of(ENABLED_SHULKER_BOXES, ENABLED_ELYTRA,
				ENABLED_SHIELDS, ENABLED_BOWS_AND_CROSSBOWS, ENABLED_FISHING_ROD);

	}

	private static final String CONFIG_FILE_NAME = "netherite_plus.json";

	public static void load() {
		File configFile = new File(getConfigDirectory(), CONFIG_FILE_NAME);

		if (configFile.exists() && configFile.isFile() && configFile.canRead()) {
			JsonElement element = JsonUtils.parseJsonFile(configFile);

			if (element != null && element.isJsonObject()) {
				JsonObject root = element.getAsJsonObject();

				ConfigUtils.readConfigBase(root, "config",
						NetheritePlusConfigManager.NetheritePlusConfiguration.OPTIONS);
			}
		} else {
			save();
		}

	}

	public static void save() {
		File dir = getConfigDirectory();

		if ((dir.exists() && dir.isDirectory()) || dir.mkdirs()) {
			JsonObject root = new JsonObject();

			ConfigUtils.writeConfigBase(root, "config", NetheritePlusConfigManager.NetheritePlusConfiguration.OPTIONS);

			JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
		}
	}

	private static File getConfigDirectory() {
		return FabricLoader.getInstance().getConfigDir().toFile();
	}

}
