package gamma.engine.core.resources;

import gamma.engine.core.utils.YamlParser;

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
