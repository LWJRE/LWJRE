package gamma.engine.resources;

import gamma.engine.rendering.Model;
import gamma.engine.rendering.Shader;

import java.util.HashMap;
import java.util.Map;

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
	private static final HashMap<String, ResourceLoader<?>> LOADERS = new HashMap<>();

	static {
		Resources.addLoader(YamlParser::parseResource, ".yaml");
		Resources.addLoader(Model.ASSIMP_LOADER, ".obj");
		Resources.addLoader(Model.ASSIMP_LOADER, ".dae");
		Resources.addLoader(Shader.SHADER_LOADER, ".glsl");
	}

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
				System.err.println("There is no loader associated with " + extension + " files");
				return null;
			}
			System.err.println("Cannot get type of file " + path);
			return null;
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

	public static String pathOf(Object resource) {
		return RESOURCES.entrySet().stream()
				.filter(entry -> entry.getValue().equals(resource))
				.findFirst()
				.map(Map.Entry::getKey)
				.orElseGet(() -> {
					System.err.println("Cannot get the path of " + resource);
					return "";
				});
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

	public static boolean hasLoader(String file) {
		int index = file.lastIndexOf('.');
		return index != -1 && LOADERS.containsKey(file.substring(index));
	}
}
