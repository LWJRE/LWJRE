package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.ServiceLoader;

/**
 * Static class that works as a centralized resource manager.
 * Responsible for loading resources and ensuring there is always at most one instance of every resource in memory.
 */
public final class ResourceManager {

    /** Maps a file extension with the suitable resource loader. */
    private static final HashMap<String, ResourceLoader> RESOURCE_LOADERS = new HashMap<>();
    /** Stores loaded resources for future access. */
    private static final HashMap<String, Object> RESOURCES = new HashMap<>();

    static {
        for(var loader : ServiceLoader.load(ResourceLoader.class)) {
            for(var extension : loader.supportedExtensions()) {
                var previous = RESOURCE_LOADERS.putIfAbsent(extension, loader);
                if(previous != null && !previous.equals(loader)) {
                    Logger.error("Cannot associate " + loader + " with files of type " + extension + " because they are already associated with " + previous);
                }
            }
        }
    }

    /**
     * Loads the resource at the given path or returns the same instance if that resource was already loaded.
     * <p>
     *     The given path must point to a resource file in the classpath and must include the file extension.
     * </p>
     * <p>
     *     Returns null and logs an error if the specified resource could not be loaded, there is no loader associated with the this type of resource, or the given path is missing the file extension.
     * </p>
     * @param resourcePath Path to the resource file in the classpath.
     * @return The requested resource.
     */
    public static Object getOrLoad(String resourcePath) {
        var resource = RESOURCES.get(resourcePath);
        if(resource == null) {
            var index = resourcePath.lastIndexOf('.');
            if(index != -1) {
                var extension = resourcePath.substring(index);
                var loader = RESOURCE_LOADERS.get(extension);
                if(loader != null) {
                    try(var inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath)) {
                        if(inputStream != null) {
                            resource = loader.load(inputStream);
                            RESOURCES.put(resourcePath, resource);
                        } else {
                            Logger.error("Could not find resource " + resourcePath);
                        }
                    } catch(IOException e) {
                        Logger.error("Error loading resource " + resourcePath, e);
                    }
                } else {
                    Logger.error("There is no resource loader for resources of type '" + extension + "'");
                }
            } else {
                Logger.error("Invalid path " + resourcePath);
            }
        }
        return resource;
    }
}