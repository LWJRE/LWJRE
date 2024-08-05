package io.github.ardentengine.core.rendering;

import io.github.scalamath.vecmatlib.Vec3f;

import java.util.Objects;

/**
 * An axis-aligned box primitive mesh.
 * The origin of the mesh is the center of the box.
 */
public class BoxMesh extends Mesh {

    /** Size of the box mesh. */
    private Vec3f size;

    /**
     * Constructs a box mesh with the given size.
     *
     * @param size Size of the box.
     */
    public BoxMesh(Vec3f size) {
        this.size = size;
    }

    /**
     * Constructs a box mesh with the given size.
     *
     * @param x Width of the box.
     * @param y Height of the box.
     * @param z Depth of the box.
     */
    public BoxMesh(float x, float y, float z) {
        this(new Vec3f(x, y, z));
    }

    /**
     * Constructs a box mesh of size (1, 1, 1).
     */
    public BoxMesh() {
        this(Vec3f.One());
    }

    /**
     * Setter method.
     * Updates the size of this mesh.
     *
     * @param size The size of the mesh.
     */
    public void setSize(Vec3f size) {
        size = Objects.requireNonNullElse(size, Vec3f.Zero());
        if(!this.size.equals(size)) {
            this.size = size;
            RenderingServer.update(this);
        }
    }

    /**
     * Setter method.
     * Updates the size of this mesh.
     *
     * @param x Size of the mesh on the x axis.
     * @param y Size of the mesh on the y axis.
     * @param z Size of the mesh on the z axis.
     */
    public void setSize(float x, float y, float z) {
        this.setSize(new Vec3f(x, y, z));
    }

    /**
     * Getter method.
     *
     * @return The size of the box mesh.
     */
    public Vec3f getSize() {
        return this.size;
    }

    @Override
    public float[] getVertices() {
        return new float[] {
            -0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
            0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
            0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
            -0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
            -0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
            0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
            0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
            -0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
            -0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
            -0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
            -0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
            -0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
            0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
            0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
            0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
            0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
            -0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
            0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
            0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
            -0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
            0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
            -0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
            -0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
            0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z()
        };
    }

    @Override
    public int[] getIndices() {
        return new int[] {
            0, 3, 2,
            2, 1, 0,
            4, 5, 6,
            6, 7 ,4,
            11, 8, 9,
            9, 10, 11,
            12, 13, 14,
            14, 15, 12,
            16, 17, 18,
            18, 19, 16,
            20, 21, 22,
            22, 23, 20
        };
    }

    @Override
    public float[] getUVs() {
        return new float[] {
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0,
            0, 0,
            0, 1,
            1, 1,
            1, 0
        };
    }

    @Override
    public float[] getNormals() {
        return new float[] {
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
        };
    }
}