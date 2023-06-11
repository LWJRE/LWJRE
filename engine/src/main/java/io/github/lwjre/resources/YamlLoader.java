package io.github.lwjre.resources;

import io.github.lwjre.utils.YamlParser;

public class YamlLoader implements ResourceLoader {

	@Override
	public Object load(String path) {
		return YamlParser.parseResource(path);
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".yaml", ".yml"};
	}
}
