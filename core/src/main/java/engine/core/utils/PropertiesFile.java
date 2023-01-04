package engine.core.utils;

import java.util.Properties;

public class PropertiesFile extends Properties {

	public PropertiesFile() {
		super();
	}

	public PropertiesFile(String filePath) {
		FileUtils.readFile(filePath, this::load);
	}

	public final PropertiesFile set(String key, Object value) {
		this.setProperty(key, String.valueOf(value));
		return this;
	}

	public final int getInt(String key) {
		return this.get(key, 0);
	}

	public final int get(String key, int defaultValue) {
		if(this.containsKey(key)) {
			try {
				return Integer.parseInt(this.getProperty(key));
			} catch(NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public final String getString(String key) {
		return this.get(key, "");
	}

	public final String get(String key, String defaultValue) {
		return this.getProperty(key, defaultValue);
	}

	public final boolean getBoolean(String key) {
		return this.get(key, false);
	}

	public final boolean get(String key, boolean defaultValue) {
		if(this.containsKey(key)) {
			return Boolean.parseBoolean(this.getProperty(key));
		}
		return defaultValue;
	}
}
