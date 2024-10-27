package io.github.ardentengine.core.rendering;

/**
 * A mesh constructed by specifying attributes as arrays.
 */
public class ArrayMesh extends Mesh {

    // FIXME: This implementation does not work because if the user modifies one of the arrays returned by the getter methods the mesh won't be updated by the rendering API

    /** Vertices array. */
    private float[] vertices;
    /** Indices array. */
    private int[] indices;
    /** UVs array. */
    private float[] uvs;
    /** Normals array. */
    private float[] normals;

    /**
     * Sets the vertices of this mesh.
     * <p>
     *     Vertices are essential for rendering the mesh.
     *     They define the coordinates of the points that make up this mesh.
     * </p>
     *
     * @param vertices Array of vertices.
     */
    public void setVertices(float[] vertices) {
        this.vertices = vertices;
        RenderingServer.getInstance().update(this);
    }

    @Override
    public float[] vertices() {
        return this.vertices;
    }

    /**
     * Sets the indices of this mesh.
     * <p>
     *     Indices point to a vertex in the vertex array and define a list of triangles that make up the mesh.
     *     The size of the returned array must always be a multiple of 3.
     * </p>
     *
     * @param indices Array of indices.
     */
    public void setIndices(int[] indices) {
        this.indices = indices;
        RenderingServer.getInstance().update(this);
    }

    @Override
    public int[] indices() {
        return this.indices;
    }

    /**
     * Sets the UVs of this mesh.
     * <p>
     *     UVs are often used to map a texture to the mesh.
     *     Since UVs are 2D coordinates, the size of the given array must be a multiple of 2.
     *     The number of UVs must be equal to the number of vertices.
     * </p>
     *
     * @param uvs Array of UVs.
     */
    public void setUVs(float[] uvs) {
        this.uvs = uvs;
        RenderingServer.getInstance().update(this);
    }

    @Override
    public float[] uvs() {
        return this.uvs;
    }

    /**
     * Sets the normals of this mesh.
     * <p>
     *     Normals are used for lighting calculations.
     *     Since normals are 3D coordinates, the size of the given array must be a multiple of 3.
     *     The number of normals must be equal to the number of vertices.
     * </p>
     *
     * @param normals Array of normals.
     */
    public void setNormals(float[] normals) {
        this.normals = normals;
        RenderingServer.getInstance().update(this);
    }

    @Override
    public float[] normals() {
        return this.normals;
    }
}