package io.github.hexagonnico.core.rendering;

/**
 * Abstract mesh class.
 * Different types of meshes may extend this class to provide different ways of updating the mesh.
 */
public abstract class Mesh {

    /**
     * Internally used mesh data.
     * Created using {@link RenderingServer#createMesh()}.
     */
    protected final MeshData meshData;

    /**
     * Constructs a mesh with no vertices.
     */
    public Mesh() {
        this.meshData = RenderingServer.createMesh();
    }

    /**
     * Draws this mesh.
     * <p>
     *     Drawing may not happen immediately.
     *     The rendering system may batch together meshes to speed up the rendering.
     * </p>
     */
    public void draw() {
        this.meshData.draw();
    }
}
