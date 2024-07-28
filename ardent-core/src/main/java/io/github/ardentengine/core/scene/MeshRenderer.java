package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Mesh;
import io.github.ardentengine.core.rendering.Shader;
import io.github.scalamath.vecmatlib.Mat3x4f;

/**
 * Node that renders a mesh.
 * <p>
 *     Uses a shader of type {@code mesh_renderer}.
 * </p>
 */
public class MeshRenderer extends VisualInstance3D {

    /** The mesh rendered by this node. */
    public Mesh mesh;

    // TODO: Modulate color and texture.

    @Override
    protected void render(Mat3x4f transform) {
        if(this.shader == null) {
            this.shader = Shader.getOrLoad("io/github/ardentengine/core/shaders/mesh_renderer.glsl");
        }
        super.render(transform);
        if(this.visible && this.shader != null && this.mesh != null) {
            this.shader.set("modulate", 1.0f, 1.0f, 1.0f, 1.0f);
            this.shader.draw(this.mesh);
        }
    }
}