package io.github.ardentengine.core.rendering;

/**
 * Base class for all types of mesh.
 * <p>
 *     Meshes are the base unit used for rendering.
 *     They contain vertices, texture coordinates, and normals.
 * </p>
 */
public abstract class Mesh {

    /**
     * The material used by this mesh.
     * Can be set to null to tell the rendering API to use the default material.
     */
    private Material material;

    /**
     * Returns an array representing the vertices of this mesh.
     * <p>
     *     Vertices are essential for rendering the mesh.
     *     They define the coordinates of the points that make up this mesh.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not have any vertices, even though such mesh cannot be rendered.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array representing the vertices of this mesh.
     */
    public abstract float[] vertices();

    /**
     * Returns true if this mesh is a 2D mesh.
     * In such case, the vertices returned by {@link Mesh#vertices()} must be 2D coordinates.
     * <p>
     *     The default implementation of this method returns false.
     * </p>
     *
     * @return True if this mesh is a 2D mesh, otherwise false.
     */
    public boolean is2D() {
        return false;
    }

    /**
     * Returns an array containing the indices of this mesh.
     * <p>
     *     Indices point to a vertex in the vertex array and define a list of triangles that make up the mesh.
     *     The size of the returned array must always be a multiple of 3.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not use indices.
     *     In that case, vertices are connected in the order they appear in the vertex array.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array containing the indices of this mesh.
     *
     * @see Mesh#vertices()
     */
    public abstract int[] indices();

    /**
     * Returns an array containing the UVs (or texture coordinates) of this mesh.
     * <p>
     *     UVs are often used to map a texture to the mesh.
     *     Since UVs are 2D coordinates, the size of the returned array must be a multiple of 2.
     *     The number of UVs must be equal to the number of vertices.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not use UVs.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array containing the UVs of this mesh
     */
    public abstract float[] uvs();

    /**
     * Returns an array containing the normal vectors to each vertex in this mesh.
     * <p>
     *     Normals are used for lighting calculations.
     *     Since normals are 3D coordinates, the size of the returned array must be a multiple of 3.
     *     The number of normals must be equal to the number of vertices.
     * </p>
     * <p>
     *     This method may return null or an empty array if this mesh does not have normals.
     * </p>
     * <p>
     *     The rendering api may call this method when a mesh update is requested using {@link RenderingServer#update(Mesh)}.
     *     Implementations of the {@code Mesh} class may use this method to create the vertices array and therefore it should only be called when the mesh needs to be created or updated to avoid overhead.
     * </p>
     *
     * @return An array containing the normals of this mesh.
     */
    public abstract float[] normals();

    // TODO: Add vertex colors

    // TODO: Add support for custom attributes

    /**
     * Setter method for {@link Mesh#material}.
     *
     * @param material The material used by this mesh. Can be set to null to tell the rendering API to use the default material.
     */
    public final void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Getter method for {@link Mesh#material}.
     *
     * @return The material used by this mesh. Can be null.
     */
    public final Material material() {
        return this.material;
    }
}