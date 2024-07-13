package io.github.ardentengine.core.resources;

import java.io.IOException;
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
                            System.err.println("Could not find resource " + resourcePath);
                        }
                    } catch(IOException e) {
                        System.err.println("Error loading resource " + resourcePath);
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("There is no resource loader for resources of type '" + extension + "'");
                }
            } else {
                System.err.println("Invalid path " + resourcePath);
            }
        }
        return resource;
    }
}
