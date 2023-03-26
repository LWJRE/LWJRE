package gamma.engine.core.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Static class responsible for loading and storing resources.
 * Loaded resources are stored in a hash map for immediate access after they were loaded for the first time.
 *
 * @author Nico
 */
public final class Resources {

	/** Map of all the loaded resources */
	private static final HashMap<String, Resource> RESOURCES = new HashMap<>();
	/** Stores all the resource loaders */
	private static final HashMap<String, ResourceLoader<?>> LOADERS = new HashMap<>();

	static {
		addLoader(Shader.SHADER_LOADER, ".glsl");
		addLoader(Model.ASSIMP_LOADER, ".obj");
		addLoader(Model.ASSIMP_LOADER, ".dae");
	}

	/**
	 * Gets the resource at the given path if it was already loaded.
	 * If it was not already loaded, finds a suitable {@link ResourceLoader} and loads it.
	 *
	 * @see Resources#addLoader(ResourceLoader, String)
	 *
	 * @param path Path of the resource to load
	 * @return The requested resource
	 * @throws RuntimeException if there is no loader associated with the file's extension
	 */
	public static Resource getOrLoad(String path) {
		if(RESOURCES.containsKey(path)) {
			return RESOURCES.get(path);
		} else {
			String extension = path.substring(path.lastIndexOf('.'));
			if(LOADERS.containsKey(extension)) {
				Resource resource = LOADERS.get(extension).load(path);
				RESOURCES.put(path, resource);
				return resource;
			}
			throw new RuntimeException("There is no loader associated with " + extension + " files");
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
		Resource resource = RESOURCES.remove(oldPath);
		if(resource != null) {
			RESOURCES.put(newPath, resource);
		}
	}

	/**
	 * Looks for the path of the given resource.
	 * Used to serialize resources when saving scene files.
	 * This method has a {@code O(n)} complexity, where {@code n} is the number of loaded resources.
	 *
	 * @param resource The resource to look for
	 * @return The path at which the resource is stored
	 * @throws NoSuchElementException if the given resource was not loaded from any path
	 */
	public static String pathOf(Resource resource) {
		return RESOURCES.entrySet().stream()
				.filter(entry -> entry.getValue().equals(resource))
				.findFirst()
				.map(Map.Entry::getKey)
				.orElseThrow(() -> new NoSuchElementException("Cannot get the path of " + resource));
	}

	/**
	 * Adds the given {@link ResourceLoader} for the given file extension.
	 *
	 * @param loader The loader to add
	 * @param extension Extension of files that this loader can load, starting with a dot
	 */
	public static void addLoader(ResourceLoader<?> loader, String extension) {
		LOADERS.put(extension, loader);
	}
}
