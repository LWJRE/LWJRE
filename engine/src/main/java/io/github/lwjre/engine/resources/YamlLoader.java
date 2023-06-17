package io.github.lwjre.engine.resources;

import io.github.lwjre.engine.utils.YamlParser;

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
