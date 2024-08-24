package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.RenderingSystem;
import io.github.ardentengine.core.rendering.Mesh;

import java.util.HashMap;
import java.util.Map;

/**
 * Node that renders a mesh.
 * <p>
 *     Uses a shader of type {@code mesh_renderer}.
 * </p>
 */
public class MeshRenderer extends VisualInstance3D {

    /** The mesh rendered by this node. */
    public Mesh mesh;

    @Override
    public void onUpdate(float delta) {
        if(this.mesh != null && this.visible) {
            // TODO: Don't render objects that are outside of the camera's frustum
            RenderingSystem.getInstance().render(this.mesh, this);
        }
    }

    // TODO: The mesh_shader will probably be the same for all 3D objects. The same cannot be said for 2D though.

    @Override
    public final String shaderType() {
        return "mesh_shader";
    }

    @Override
    public final Map<String, Object> getShaderParameters() {
        return new HashMap<>();
    }
}