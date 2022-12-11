package io.github.view.utils;

import java.util.Properties;

public class PropertiesFile extends Properties {

	public static PropertiesFile loadFromFile(String file) {
		return FileUtils.readProperties(file);
	}

	public PropertiesFile set(String key, Object value) {
		this.setProperty(key, String.valueOf(value));
		return this;
	}

	public int getInt(String key) {
		return this.get(key, 0);
	}

	public int get(String key, int defaultValue) {
		if(this.containsKey(key)) {
			try {
				return Integer.parseInt(this.getProperty(key));
			} catch(NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public String getString(String key) {
		return this.get(key, "");
	}

	public String get(String key, String defaultValue) {
		return this.getProperty(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return this.get(key, false);
	}

	public boolean get(String key, boolean defaultValue) {
		if(this.containsKey(key)) {
			return Boolean.parseBoolean(this.getProperty(key));
		}
		return defaultValue;
	}
}
