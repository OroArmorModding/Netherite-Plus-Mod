package com.oroarmor.util.config;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ConfigItemGroup extends ConfigItem<ConfigItemGroup> {

	private final List<ConfigItem<?>> configs;

	private ConfigItemGroup() {
		super(null, null, null);
		configs = null;
	}

	public ConfigItemGroup(List<ConfigItem<?>> configs, String name) {
		super(name, new ConfigItemGroup(), "");
		this.configs = configs;
	}

	@Override
	public void fromJson(JsonElement jsonConfigs) {
		JsonObject object = jsonConfigs.getAsJsonObject();
		for (Entry<String, JsonElement> entry : object.entrySet()) {
			configs.stream().filter(c -> c.getName().equals(entry.getKey())).forEach(c -> c.fromJson(entry.getValue()));
		}
	}

	public List<ConfigItem<?>> getConfigs() {
		return configs;
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
		case STRING:
			object.addProperty(c.getName(), (String) c.getValue());
			break;
		case GROUP:
			object.add(c.getName(), ((ConfigItemGroup) c).toJson());
		default:
			break;
		}
	}

	public JsonObject toJson() {
		JsonObject object = new JsonObject();

		configs.stream().forEachOrdered(c -> parseConfig(c, object));

		return object;
	}

	@Override
	public String toString() {
		String string = getName() + ": [";
		string += configs.stream().map(Object::toString).collect(Collectors.joining(", "));
		return string + "]";
	}

}
