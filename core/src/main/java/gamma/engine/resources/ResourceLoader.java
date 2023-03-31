package gamma.engine.resources;

/**
 * Interface used to implement a loader that can load a certain type of resource.
 *
 * @see Resources#addLoader(ResourceLoader, String)
 *
 * @param <R> The resource loaded by this loader
 */
public interface ResourceLoader<R extends Resource> {

	/**
	 * Loads a resource at the given path.
	 *
	 * @param path Path of the resource to load
	 * @return The loaded resource
	 */
	R load(String path);
}
