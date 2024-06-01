package io.github.hexagonnico.core.rendering;

/**
 * Class used internally by meshes to abstract the representation of a mesh across different rendering APIs.
 * <p>
 *     All the methods in this class are dummy methods.
 *     The rendering API module should extend this class to provide its implementation.
 * </p>
 *
 * @see Mesh
 */
public class MeshData {

    /**
     * Sets the vertices of the mesh to the given array of 3D vertices.
     * <p>
     *     Calling this method over {@link MeshData#setVertices2D(float[])} also determines which draw mode should be used by this mesh.
     *     After updating the vertices of the mesh, one should optionally update the indices using {@link MeshData#setIndices(int[])}.
     * </p>
     *
     * @param vertices Array of 3D vertices.
     */
    public void setVertices3D(float[] vertices) {

    }

    /**
     * Sets the vertices of the mesh to the given array of 2D vertices.
     * <p>
     *     Calling this method over {@link MeshData#setVertices3D(float[])} also determines which draw mode should be used by this mesh.
     *     After updating the vertices of the mesh, one should optionally update the indices using {@link MeshData#setIndices(int[])}.
     * </p>
     *
     * @param vertices Array of 2D vertices.
     */
    public void setVertices2D(float[] vertices) {

    }

    /**
     * Sets the indices of the mesh to the given array.
     *
     * @param indices Array of indices.
     */
    public void setIndices(int[] indices) {

    }

    /**
     * Sets the UVs of this mesh to the given array of 2D coordinates.
     *
     * @param uvs Array of 2D coordinates.
     */
    public void setUVs(float[] uvs) {

    }

    /**
     * Sets the normals of this mesh to the given array of 3D vectors.
     *
     * @param normals Array of 3D vectors.
     */
    public void setNormals(float[] normals) {

    }
}
