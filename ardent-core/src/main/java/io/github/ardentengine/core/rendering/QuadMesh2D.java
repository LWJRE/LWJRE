package io.github.ardentengine.core.rendering;

public class QuadMesh2D extends Mesh {

    // TODO: Are 2D meshes useful for anything other than rendering 2D stuff on a quad mesh?

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