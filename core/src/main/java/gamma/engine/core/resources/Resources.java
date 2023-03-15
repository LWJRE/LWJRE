package gamma.engine.core.resources;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;

public class Resources {

	private static final HashMap<String, Resource> RESOURCES = new HashMap<>();
	private static final HashMap<Set<String>, ResourceLoader<?>> LOADERS = new HashMap<>();

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

	// TODO: Check if resources actually need to store their path 'cause it would be better if they didn't

	public static void store(Resource resource) {
		String path = resource.path();
		if(RESOURCES.containsKey(path))
			throw new RuntimeException("Resource " + RESOURCES.get(path) + " already exists at path " + path);
		RESOURCES.put(path, resource);
	}

	public static void addLoader(ResourceLoader<?> loader, String... extensions) {
		LOADERS.put(Set.of(extensions), loader);
	}

	public static Resource remove(String path) {
		return RESOURCES.remove(path);
	}
}
