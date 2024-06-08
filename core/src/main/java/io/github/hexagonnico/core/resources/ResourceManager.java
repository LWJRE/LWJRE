package io.github.hexagonnico.core.resources;

import java.util.HashMap;
import java.util.ServiceLoader;

public class ResourceManager {

    private static final HashMap<String, ResourceLoader> RESOURCE_LOADERS = new HashMap<>();
    private static final HashMap<String, Object> RESOURCES = new HashMap<>();

    // TODO: Don't put this in a static initializer
    static {
        for(var loader : ServiceLoader.load(ResourceLoader.class)) {
            for(var extension : loader.supportedExtensions()) {
                RESOURCE_LOADERS.put(extension, loader);
                // TODO: Log an error if a loader for a certain extension has been defined twice
            }
        }
    }

    public static Object getOrLoad(String resourcePath) {
        return RESOURCES.computeIfAbsent(resourcePath, path -> {
            var index = path.lastIndexOf('.');
            if(index != -1) {
                var extension = path.substring(index);
                if(RESOURCE_LOADERS.containsKey(extension)) {
                    return RESOURCE_LOADERS.get(extension).load(path);
                }
                System.err.println("There is no resource loader for resources of type " + extension);
                return null;
            }
            System.err.println("Invalid path " + path);
            return null;
        });
    }
}
