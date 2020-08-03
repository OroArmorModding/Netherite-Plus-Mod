package com.oroarmor.util.config;

import java.util.List;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigItemGroup {
	private final List<ConfigItem<?>> configs;
	private final String name;

	public ConfigItemGroup(List<ConfigItem<?>> configs, String name) {
		this.configs = configs;
		this.name = name;
	}

	public List<ConfigItem<?>> getConfigs() {
		return configs;
	}

	public String getName() {
		return name;
	}

	public void fromJson(JsonObject jsonConfigs) {
		for (Entry<String, JsonElement> e : jsonConfigs.entrySet()) {
			configs.stream().filter(c -> c.getName().equals(e.getKey())).forEach(c -> c.fromJson(e.getValue()));
		}
	}

	public JsonObject toJson() {
		JsonObject object = new JsonObject();

		configs.stream().forEachOrdered(c -> parseConfig(c, object));

		return object;
	}

	private void parseConfig(ConfigItem<?> c, JsonObject object) {
		switch (c.getType()) {
			case BOOLEAN:
				object.addProperty(c.getName(), (Boolean) c.getValue());
				break;
			case DOUBLE:
			case INTEGER:
				object.addProperty(c.getName(), (Number) c.getValue());
				break;
			default:
				break;
		}

	}

}
