package gamma.engine.resources;

import gamma.engine.utils.YamlParser;

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
