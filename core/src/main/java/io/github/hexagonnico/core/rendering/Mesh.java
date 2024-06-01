package io.github.hexagonnico.core.rendering;

/**
 * Base mesh class.
 * Other classes may extend this one to provide different ways of updating the mesh.
 * <p>
 *     Meshes are rendered using the {@link RenderingServer#render(Mesh, Shader)} method.
 * </p>
 */
public class Mesh {

    /**
     * Mesh data used internally to abstract the representation of a mesh across different rendering APIs.
     */
    protected final MeshData meshData = RenderingServer.createMesh(this);

    /**
     * Method called before the mesh is rendered in {@link RenderingServer#render(Mesh, Shader)}.
     * Can be used to update the mesh before it is rendered.
     */
    public void onRender() {

    }
}
