package io.github.ardentengine.core.rendering;

/**
 * Quad mesh used to render 2D objects.
 */
public class QuadMesh2D extends Mesh {

    private static QuadMesh2D instance;

    public static QuadMesh2D getInstance() {
        if(instance == null) {
            instance = new QuadMesh2D();
        }
        return instance;
    }

    // TODO: Size?

    @Override
    public float[] getVertices() {
        return new float[] {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f};
    }

    @Override
    public boolean is2D() {
        return true;
    }

    @Override
    public int[] getIndices() {
        return new int[] {0, 1, 3, 3, 1, 2};
    }

    @Override
    public float[] getUVs() {
        return new float[] {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};
    }

    @Override
    public float[] getNormals() {
        return null;
    }
}