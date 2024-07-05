package io.github.hexagonnico.core.rendering;

import io.github.scalamath.vecmatlib.Vec3f;

/**
 * An axis-aligned box primitive mesh.
 * The origin of the mesh is the center of the box.
 */
public class BoxMesh extends Mesh {

    /** Size of the box mesh. */
    private Vec3f size;

    /**
     * Flag used to tell that the mesh should be updated.
     * Set to false when the size is updated.
     */
    private boolean dirty;

    /**
     * Constructs a box mesh with the given size.
     *
     * @param size Size of the box.
     */
    public BoxMesh(Vec3f size) {
        this.meshData.setVertices3D(new float[] {
            -0.5f * size.x(), 0.5f * size.y(), -0.5f * size.z(),
            -0.5f * size.x(), -0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), -0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), 0.5f * size.y(), -0.5f * size.z(),
            -0.5f * size.x(), 0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), -0.5f * size.y(), 0.5f * size.z(),
            0.5f * size.x(), -0.5f * size.y(), 0.5f * size.z(),
            0.5f * size.x(), 0.5f * size.y(), 0.5f * size.z(),
            0.5f * size.x(), 0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), -0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), -0.5f * size.y(), 0.5f * size.z(),
            0.5f * size.x(), 0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), 0.5f * size.y(), -0.5f * size.z(),
            -0.5f * size.x(), -0.5f * size.y(), -0.5f * size.z(),
            -0.5f * size.x(), -0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), 0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), 0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), 0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), 0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), 0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), -0.5f * size.y(), 0.5f * size.z(),
            -0.5f * size.x(), -0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), -0.5f * size.y(), -0.5f * size.z(),
            0.5f * size.x(), -0.5f * size.y(), 0.5f * size.z()
        });
        this.meshData.setIndices(new int[] {
            0, 1, 3,
            3, 1, 2,
            4, 5, 7,
            7, 5, 6,
            8, 9, 11,
            11, 9, 10,
            12, 13, 15,
            15, 13, 14,
            16, 17, 19,
            19, 17, 18,
            20, 21, 23,
            23, 21, 22
        });
        this.meshData.setUVs(new float[] {
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
        });
        // TODO: Normals
        this.size = size;
        this.dirty = false;
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
        this.size = size;
        this.dirty = true;
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
    public void draw() {
        if(this.dirty) {
            this.meshData.setVertices3D(new float[] {
                -0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
                -0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
                -0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
                0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
                0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
                0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
                0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
                -0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
                -0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), 0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), 0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z(),
                -0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), -0.5f * this.size.y(), -0.5f * this.size.z(),
                0.5f * this.size.x(), -0.5f * this.size.y(), 0.5f * this.size.z()
            });
            this.dirty = false;
        }
        super.draw();
    }
}
