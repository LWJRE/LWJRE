package io.github.lwjre.engine.resources;

/**
 * Interface used to implement a loader that can load a certain type of resource.
 * Loaders are loaded with a {@link java.util.ServiceLoader} when the {@link Resources} class is initialized.
 * Loaders must be added to {@code META-INF/servers/io.github.lwjre.engine.resources.ResourceLoader}.
 *
 * @author Nico
 */
public interface ResourceLoader {

	/**
	 * Loads a resource at the given path.
	 *
	 * @param path Path of the resource to load
	 * @return The loaded resource
	 */
	Object load(String path);

	/**
	 * Gets an array containing the extensions of the files that this loader can load, starting with a dot.
	 *
	 * @return An array containing the extensions of the files that this loader can load, starting with a dot
	 */
	String[] getExtensions();
}
