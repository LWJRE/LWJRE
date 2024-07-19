package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.scene.Node;
import io.github.ardentengine.core.util.ReflectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Resource used for loading scenes from files.
 * Use {@link SceneResource#instantiate()} to instantiate a scene.
 * <p>
 *     Each instance of a {@code SceneResource} corresponds to a {@link Node} and contains a map of its modified properties.
 *     Each {@code SceneResource} contains a list of other {@code SceneResource}s that represent the children of the scene's root node.
 *     Scenes may also be inherited from other scenes.
 * </p>
 */
@SuppressWarnings("FieldMayBeFinal")
public class SceneResource {

    /**
     * Utility method to get a scene resource using {@link ResourceManager#getOrLoad(String)}.
     * <p>
     *     Loads the scene resource at the given path or returns the same instance if it was already loaded.
     * </p>
     * <p>
     *     Returns null and logs an error if the resource at the given path is not of class {@code SceneResource}.
     * </p>
     * @param resourcePath Path at which to load the scene resource. Must point to a {@code .yaml} scene resource file in the classpath.
     * @return The requested scene resource.
     */
    public static SceneResource getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof SceneResource) {
            return (SceneResource) resource;
        }
        Logger.error("Resource " + resourcePath + " is not a scene resource");
        return null;
    }

    /** The type of node used at the root of the scene. */
    private Class<? extends Node> type = null;
    /** Scene to use as base if this scene inherits from another scene. */
    private SceneResource base = null;
    /** Maps the node's properties to their values. */
    private Map<String, Object> properties = null;
    /** Children of this node. */
    private List<SceneResource> children = null;

    /**
     * Instantiates this scene and returns the root of the instantiated scene.
     *
     * @return The root of the instantiated scene.
     * @throws io.github.ardentengine.core.util.ReflectionException If a reflection error occurs while instantiating the scene.
     * @throws ClassCastException If the base class type for the scene's root is not a subclass of {@code Node}.
     */
    public Node instantiate() {
        if(this.type == null && this.base == null) {
            this.type = Node.class;
        }
        var node = this.base == null ? ReflectionUtils.newInstance(this.type) : this.base.instantiate();
        if(this.properties != null) {
            this.properties.forEach((name, value) -> ReflectionUtils.setField(node, name, value));
        }
        if(this.children != null) {
            for(var child : this.children) {
                node.addChild(child.instantiate());
            }
        }
        return node;
    }
}