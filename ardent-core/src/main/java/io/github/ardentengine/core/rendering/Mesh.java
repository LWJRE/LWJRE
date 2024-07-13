package io.github.ardentengine.core.rendering;

/**
 * Base mesh class.
 * Other classes may extend this one to provide different ways of updating the mesh.
 */
public class Mesh {

    /** Mesh data used internally to abstract the representation of a mesh across different rendering APIs. */
    protected final MeshData meshData = RenderingServer.createMesh();

    /**
     * Draws this mesh by calling {@link MeshData#draw()}.
     * Mesh classes may extend this method to update the mesh before it is drawn in case it has been modified.
     */
    public void draw() {
        this.meshData.draw();
    }
}
