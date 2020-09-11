package com.oroarmor.util.config;

import java.util.function.Consumer;

import org.jetbrains.annotations.Nullable;

import com.google.gson.JsonElement;

public class ConfigItem<T> {
	public enum Type {
		BOOLEAN, INTEGER, DOUBLE, STRING, GROUP;

		public static Type getTypeFrom(Object value) {
			if (value instanceof Boolean) {
				return BOOLEAN;
			}
			if (value instanceof Integer) {
				return INTEGER;
			}
			if (value instanceof Double) {
				return DOUBLE;
			}
			if (value instanceof String) {
				return STRING;
			}
			if (value instanceof ConfigItemGroup) {
				return GROUP;
			}

			return null;
		}

	}

	protected final String name;
	protected final String details;

	protected T value;
	protected final T defaultValue;

	protected final Type type;

	@Nullable
	protected final Consumer<ConfigItem<T>> onChange;

	public ConfigItem(String name, T defaultValue, String details) {
		this(name, defaultValue, details, null);
	}

	public ConfigItem(String name, T defaultValue, String details, Consumer<ConfigItem<T>> onChange) {
		this.name = name;
		this.details = details;
		this.defaultValue = defaultValue;
		this.value = defaultValue;
		this.type = Type.getTypeFrom(defaultValue);
		this.onChange = onChange;
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

		case STRING:
			newValue = (T) element.getAsString();
			break;

		case GROUP:
			((ConfigItemGroup) defaultValue).fromJson(element.getAsJsonObject());
			newValue = defaultValue;

		default:
			return;
		}

		if (newValue != null) {
			setValue(newValue);
		}

	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public String getDetails() {
		return details;
	}

	public String getName() {
		return name;
	}

	public Type getType() {
		return type;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		if (this.onChange != null) {
			this.onChange.accept(this);
		}
	}

	@Override
	public String toString() {
		return name + ":" + value;
	}

}
