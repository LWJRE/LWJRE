package io.github.ardentengine.core.resources;

/**
 * Interface used to implement a resource loader.
 * <p>
 *     Resource loaders are loaded as services from the {@link ResourceManager} class.
 * </p>
 */
public interface ResourceLoader {

    /**
     * Loads the resource.
     * Implementations of this interface should provide logic for resource loading in this method.
     * <p>
     *     This method is called from the {@link ResourceManager#getOrLoad(String)} method when a resource needs to be loaded.
     *     The given path is the path at which the resource is expected to be.
     * </p>
     * <p>
     *     Should return null and log an error if an error occurs while loading the resource.
     * </p>
     *
     * @param resourcePath Path at which the resource is expected to be.
     * @return The loaded resource.
     */
    Object load(String resourcePath);

    /**
     * Returns an array of strings representing supported extensions that should be recognized by this resource loader.
     * Each entry must begin with a dot.
     * <p>
     *     When a resource is loaded from the {@link ResourceManager}, it will first look for a suitable resource loader based on the file's extension.
     * </p>
     *
     * @return An array of strings representing supported extensions that should be recognized by this resource loader.
     */
    String[] supportedExtensions();
}