package gamma.engine.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Static class responsible for loading and storing resources.
 * Loaded resources are stored in a hash map for immediate access after they were loaded for the first time.
 *
 * @author Nico
 */
public final class Resources {

	/** Map of all the loaded resources */
	private static final HashMap<String, Object> RESOURCES = new HashMap<>();
	/** Stores all the resource loaders */
	private static final HashMap<String, ResourceLoader> LOADERS = new HashMap<>();

	static {
		ServiceLoader.load(ResourceLoader.class).forEach(loader -> {
			for(String extension : loader.getExtensions()) {
				LOADERS.put(extension, loader);
			}
		});
	}

	/**
	 * Loads the resource at the given path in the classpath or returns the same instance if it was already loaded.
	 * Reads file extension of the given path and gets a suitable {@link ResourceLoader}.
	 * Loaded resources are stored in a hash map for immediate access after they were loaded for the first time.
	 *
	 * @param path Path of the resource to load
	 * @return The loaded resource
	 */
	public static Object getOrLoad(String path) {
		if(RESOURCES.containsKey(path)) {
			return RESOURCES.get(path);
		} else {
			int index = path.lastIndexOf('.');
			if(index != -1) {
				String extension = path.substring(index);
				if(LOADERS.containsKey(extension)) {
					Object resource = LOADERS.get(extension).load(path);
					RESOURCES.put(path, resource);
					return resource;
				}
				throw new RuntimeException("There is no loader associated with " + extension + " files");
			}
			throw new RuntimeException("Cannot get type of file " + path);
		}
	}

	/**
	 * Changes the path at which a resource is stored.
	 * This method should be called when a resource is moved in the file system.
	 * If there was no loaded resource at the given path this method has no effect.
	 *
	 * @param oldPath Old path of the resource that has been moved
	 * @param newPath New path to which the resource has been moved
	 */
	public static void updatePath(String oldPath, String newPath) {
		Object resource = RESOURCES.remove(oldPath);
		if(resource != null) {
			RESOURCES.put(newPath, resource);
		}
	}

	/**
	 * Looks through all the loaded resource to find the path of the given resource. Used for serialization.
	 *
	 * @param resource The resource to look for
	 * @return The path of the given resource or an empty string if it could not be found
	 */
	public static String pathOf(Object resource) {
		return RESOURCES.entrySet().stream()
				.filter(entry -> entry.getValue().equals(resource))
				.findFirst()
				.map(Map.Entry::getKey)
				.orElse("");
	}

	public static boolean hasLoader(String file) {
		int index = file.lastIndexOf('.');
		return index != -1 && LOADERS.containsKey(file.substring(index));
	}
}
