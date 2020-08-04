package com.oroarmor.util.config;

import com.google.gson.JsonElement;

public final class ConfigItem<T> {
	public enum Type {
		BOOLEAN, INTEGER, DOUBLE;

		public static Type getTypeFrom(Object defaultValue) {
			if (defaultValue instanceof Boolean) {
				return BOOLEAN;
			}
			if (defaultValue instanceof Integer) {
				return INTEGER;
			}
			if (defaultValue instanceof Double) {
				return DOUBLE;
			}

			return null;
		}

	}

	protected final String name;
	protected final String details;

	protected T value;
	protected final T defaultValue;

	protected final Type type;

	public ConfigItem(String name, T defaultValue, String details) {
		this.name = name;
		this.details = details;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
		this.type = Type.getTypeFrom(defaultValue);
	}

	public T getValue() {
		return value;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public String getName() {
		return name;
	}

	public String getDetails() {
		return details;
	}

	@SuppressWarnings("unchecked")
	public void fromJson(JsonElement element) {
		T newValue = null;

		switch (this.type) {
			case BOOLEAN:
				newValue = (T) (Object) element.getAsBoolean();
				break;

			case INTEGER:
				newValue = (T) (Object) element.getAsInt();
				break;

			case DOUBLE:
				newValue = (T) (Object) element.getAsDouble();
				break;

			default:
				return;
		}

		if (newValue != null) {
			setValue(newValue);
		}

	}

	public void setValue(T value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return name + ":" + value;
	}

}
