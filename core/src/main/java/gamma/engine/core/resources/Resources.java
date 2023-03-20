package gamma.engine.core.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class Resources {

	private static final HashMap<String, Resource> RESOURCES = new HashMap<>();
	private static final HashMap<Set<String>, ResourceLoader<?>> LOADERS = new HashMap<>();

	static {
		addLoader(Shader.SHADER_LOADER, ".glsl");
		addLoader(Model.OBJ_LOADER, ".obj");
	}

	public static Resource getOrLoad(String path) {
		if(RESOURCES.containsKey(path)) {
			return RESOURCES.get(path);
		} else {
			String extension = path.substring(path.lastIndexOf('.'));
			return LOADERS.keySet().stream()
					.filter(set -> set.contains(extension))
					.findFirst()
					.map(set -> {
						Resource resource = LOADERS.get(set).load(path);
						RESOURCES.put(path, resource);
						return resource;
					}).orElseThrow(() -> new NoSuchElementException("There is no loader associated with " + extension + " files"));
		}
	}

	public static void updatePath(String oldPath, String newPath) {
		Resource resource = RESOURCES.remove(oldPath);
		if(resource != null) {
			RESOURCES.put(newPath, resource);
		}
	}

	public static String pathOf(Resource resource) {
		return RESOURCES.entrySet().stream()
				.filter(entry -> entry.getValue().equals(resource))
				.findFirst()
				.map(Map.Entry::getKey)
				.orElseThrow(() -> new NoSuchElementException("Cannot get the path of " + resource));
	}

	public static void addLoader(ResourceLoader<?> loader, String... extensions) {
		LOADERS.put(Set.of(extensions), loader);
	}
}
