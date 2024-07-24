package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.display.DisplayServer;
import io.github.ardentengine.core.rendering.QuadMesh2D;
import io.github.ardentengine.core.rendering.Shader;
import io.github.scalamath.vecmatlib.Mat2x3f;
import io.github.scalamath.vecmatlib.Mat3f;
import io.github.scalamath.vecmatlib.Mat4f;

/**
 * Base class for all visual 2D nodes.
 * Contains a {@link VisualInstance2D#render(Mat2x3f, int)} method that can be extended to render objects.
 */
public class VisualInstance2D extends Node2D {

    /**
     * Shader used by this node.
     * <p>
     *     Most of the work related to rendering is performed by shaders.
     *     The {@link VisualInstance2D#render(Mat2x3f, int)} method is mostly responsible for loading data into the shader.
     * </p>
     * <p>
     *     If this property is set to null, this object won't be rendered.
     *     Classes extending {@link VisualInstance2D} may set the shader to a default built-in shader before rendering if it is null.
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
     * @param zIndex This node's effective z index.
     */
    protected void render(Mat2x3f transform, int zIndex) {
        if(this.visible && this.shader != null) {
            this.shader.set("transformation_matrix", transform);
            if(this.getSceneTree().getCamera2D() != null) {
                this.shader.set("view_matrix", this.getSceneTree().getCamera2D().viewMatrix());
            } else {
                this.shader.set("view_matrix", Mat3f.Identity());
            }
            var windowSize = DisplayServer.getWindowSize(); // TODO: Avoid computing this every frame
            this.shader.set("projection_matrix", Mat4f.scaling(2.0f / windowSize.x(), 2.0f / windowSize.y(), -1.0f / 4096.0f));
            this.shader.set("z_index", zIndex);
            this.shader.draw(QuadMesh2D.getInstance());
        }
    }

    // TODO: Don't render things if they're outside of the camera's bounding rect
}