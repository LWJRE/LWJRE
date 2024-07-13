package io.github.ardentengine.core.resources;

import java.io.IOException;
import java.io.InputStream;

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
     *
     * @param inputStream Input stream used to load the resource. Must not be null.
     * @return The loaded resource.
     * @throws IOException If an error occurs while loading the resource.
     */
    Object load(InputStream inputStream) throws IOException;

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
