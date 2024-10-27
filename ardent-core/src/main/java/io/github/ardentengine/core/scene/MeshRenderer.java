package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Mesh;
import io.github.ardentengine.core.rendering.RenderingServer;

/**
 * Node used to render a single mesh.
 * Useful for rendering primitive shapes.
 */
public class MeshRenderer extends VisualInstance3D {

    /**
     * The mesh rendered by this mesh renderer.
     * No mesh will be rendered if it is set to null.
     */
    private Mesh mesh;

    @Override
    void update(float deltaTime) {
        if(this.mesh != null && this.visible()) {
            // TODO: Don't render objects that are outside of the camera's frustum
            RenderingServer.getInstance().draw(this.mesh, this.materialOverride(), this.globalTransform());
        }
        super.update(deltaTime);
    }

    /**
     * Getter method for {@link MeshRenderer#mesh}.
     *
     * @return The mesh rendered by this mesh renderer. Can be null.
     */
    public final Mesh mesh() {
        return this.mesh;
    }

    /**
     * Setter method for {@link MeshRenderer#mesh}.
     *
     * @param mesh The mesh rendered by this mesh renderer. Can be set to null to not render any mesh.
     */
    public final void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}