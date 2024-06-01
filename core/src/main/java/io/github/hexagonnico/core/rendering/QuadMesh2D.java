package io.github.hexagonnico.core.rendering;

/**
 * Quad mesh used to render 2D objects.
 */
public class QuadMesh2D extends Mesh {

    /**
     * Constructs a quad mesh of size (1, 1).
     */
    public QuadMesh2D() {
        this.meshData.setVertices2D(new float[] {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f});
        this.meshData.setIndices(new int[] {0, 1, 3, 3, 1, 2});
        this.meshData.setUVs(new float[] {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f});
        // TODO: Make this use triangle strips
    }
}
