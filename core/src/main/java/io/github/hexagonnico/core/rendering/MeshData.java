package io.github.hexagonnico.core.rendering;

/**
 * Interface used to abstract the representation of a mesh across different rendering APIs.
 * <p>
 *     This interface is used internally by the engine.
 *     Users should use the {@link Mesh} class instead.
 * </p>
 * <p>
 *     All the methods in this interface have an empty default implementation to allow the {@link RenderingApi} interface to use a default implementation when {@link RenderingApi#createMesh()} is called.
 * </p>
 */
public interface MeshData {

    /**
     * Sets the vertices of the mesh to the given array of 3D vertices.
     * <p>
     *     Calling this method over {@link MeshData#setVertices2D(float[])} also determines which draw mode should be used by this mesh.
     *     After updating the vertices of the mesh, one should also update the indices using {@link MeshData#setIndices(int[])}.
     * </p>
     *
     * @param vertices Array of 3D vertices.
     */
    default void setVertices3D(float[] vertices) {

    }

    /**
     * Sets the vertices of the mesh to the given array of 2D vertices.
     * <p>
     *     Calling this method over {@link MeshData#setVertices3D(float[])} also determines which draw mode should be used by this mesh.
     *     After updating the vertices of the mesh, one should also update the indices using {@link MeshData#setIndices(int[])}.
     * </p>
     *
     * @param vertices Array of 2D vertices.
     */
    default void setVertices2D(float[] vertices) {

    }

    /**
     * Sets the indices of the mesh to the given array.
     *
     * @param indices Array of indices.
     */
    default void setIndices(int[] indices) {

    }

    /**
     * Sets the UVs of this mesh to the given array of 2D coordinates.
     *
     * @param uvs Array of 2D coordinates.
     */
    default void setUVs(float[] uvs) {

    }

    /**
     * Sets the normals of this mesh to the given array of 3D vectors.
     *
     * @param normals Array of 3D vectors.
     */
    default void setNormals(float[] normals) {

    }

    /**
     * Draws this mesh.
     * <p>
     *     Drawing may not happen immediately.
     *     The rendering system may batch together meshes to speed up the rendering.
     * </p>
     */
    default void draw() {

    }
}
