package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Shader;
import io.github.scalamath.vecmatlib.Mat3x4f;
import io.github.scalamath.vecmatlib.Mat4f;

/**
 * Base class for all visual 3D nodes.
 * Contains a {@link VisualInstance3D#render(Mat3x4f)} method that can be extended to render objects.
 */
public class VisualInstance3D extends Node3D {

    /**
     * Shader used by this node.
     * <p>
     *     Most of the work related to rendering is performed by shaders.
     *     The {@link VisualInstance3D#render(Mat3x4f)} method is mostly responsible for loading data into the shader.
     * </p>
     * <p>
     *     If this property is set to null, this object won't be rendered.
     *     Classes extending {@link VisualInstance3D} may set the shader to a default built-in shader before rendering if it is null.
     * </p>
     */
    public Shader shader;
    /**
     * True if this visual instance is visible.
     * The visual instance won't be rendered if {@code visible} is set to false.
     */
    public boolean visible = true;

    /**
     * Renders this visual instance.
     * <p>
     *     The render method is called from the main loop before this node is processed.
     *     Unlike {@link Node#onUpdate(float)}, the render method is called first in parent nodes.
     * </p>
     *
     * @param transform This node's global transform.
     */
    protected void render(Mat3x4f transform) {
        if(this.visible && this.shader != null) {
            this.shader.set("transformation_matrix", transform);
            if(this.getSceneTree().getCamera3D() != null) {
                this.shader.set("view_matrix", this.getSceneTree().getCamera3D().viewMatrix());
                this.shader.set("projection_matrix", this.getSceneTree().getCamera3D().projectionMatrix());
            } else {
                this.shader.set("view_matrix", Mat4f.Identity());
                this.shader.set("projection_matrix", Mat4f.Identity());
            }
        }
    }

    // TODO: Don't render things if they're outside of the camera's frustum
}