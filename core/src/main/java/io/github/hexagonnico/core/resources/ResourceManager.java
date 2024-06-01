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

    public static Object getOrLoad(String path) {
        if(RESOURCES.containsKey(path)) {
            return RESOURCES.get(path);
        } else {
            var index = path.lastIndexOf('.');
            if(index != -1) {
                var extension = path.substring(index);
                if(RESOURCE_LOADERS.containsKey(extension)) {
                    var resource = RESOURCE_LOADERS.get(extension).load(path);
                    RESOURCES.put(path, resource);
                    return resource;
                }
                // TODO: Log an error if there is no suitable loader
            }
            // TODO: Log an error if the given path is invalid
            return null;
        }
    }
}
