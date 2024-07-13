package io.github.ardentengine.core.resources;

import io.github.ardentengine.core.scene.Node;
import io.github.ardentengine.core.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SceneResource {

    public static SceneResource getOrLoad(String path) {
        var resource = ResourceManager.getOrLoad(path);
        if(resource instanceof SceneResource sceneResource) {
            return sceneResource;
        }
        System.err.println("Resource " + path + " is not a scene resource");
        return null;
    }

    private String type = null;
    private Map<String, Object> properties = null;
    // TODO: Subscenes
    private List<SceneResource> children = null;

    public Node instantiate() {
        if(this.type == null) {
            this.type = Node.class.getName();
        }
        // TODO: Might throw class cast exception
        var node = (Node) ReflectionUtils.newInstance(this.type);
        if(this.properties != null) {
            for(var propertyName : this.properties.keySet()) {
                ReflectionUtils.setField(node, propertyName, this.properties.get(propertyName));
            }
        }
        if(this.children != null) {
            for(var child : this.children) {
                node.addChild(child.instantiate());
            }
        }
        return node;
    }

    public void save(Node node) {
        this.type = node.getClass().getName();
        this.properties = new LinkedHashMap<>();
        ReflectionUtils.getAllFields(node, this.properties);
        if(node.getChildCount() > 0) {
            this.children = new ArrayList<>();
            for(var child : node.getChildren()) {
                var scene = new SceneResource();
                scene.save(child);
                this.children.add(scene);
            }
        }
    }
}
