package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.Mesh;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

/**
 * OpenGL implementation of a mesh.
 */
public class MeshData {

    /** Keeps track of created meshes for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Mesh, MeshData> MESHES = new HashMap<>();

    /**
     * Returns the mesh data corresponding to the given mesh or creates a new one if it does not exist.
     * <p>
     *     This method also updates the mesh data if it has requested to be updated with {@link MeshData#requestUpdate(Mesh)}.
     * </p>
     *
     * @param mesh Mesh object.
     * @return The corresponding mesh data.
     */
    public static MeshData getOrCreate(Mesh mesh) {
        var meshData = MESHES.computeIfAbsent(mesh, MeshData::new);
        if(meshData.dirty) {
            meshData.updateMesh(mesh);
            meshData.dirty = false;
        }
        return meshData;
    }

    /**
     * Requests the given mesh to be updated.
     * <p>
     *     The mesh data corresponding to the given mesh will be updated the next time {@link MeshData#getOrCreate(Mesh)} is called.
     * </p>
     *
     * @param mesh Mesh object.
     */
    public static void requestUpdate(Mesh mesh) {
        var meshData = MESHES.get(mesh);
        if(meshData != null) {
            meshData.dirty = true;
        }
    }

    /** Vertex array object. */
    private final int vertexArray;
    /** Map of vertex buffer objects and attribute lists. */
    private final HashMap<Integer, Integer> vertexBuffers = new HashMap<>();

    /** Number of vertices in this mesh. */
    private int vertexCount = -1;

    /** Vertex buffer object linked to indices. */
    private int indexBuffer = -1;
    /** Number of indices in this mesh. */
    private int indicesCount = -1;

    /** Set to true from {@link MeshData#requestUpdate(Mesh)} when the mesh must be updated. */
    private boolean dirty = false;

    /**
     * Creates the mesh data for the given mesh.
     *
     * @param mesh Mesh object.
     */
    private MeshData(Mesh mesh) {
        this.vertexArray = GL30.glGenVertexArrays();
        this.updateMesh(mesh);
    }

    /**
     * Updates this mesh.
     *
     * @param mesh Mesh object.
     */
    private void updateMesh(Mesh mesh) {
        GL30.glBindVertexArray(this.vertexArray);
        this.setVertices(mesh.vertices(), mesh.is2D() ? 2 : 3);
        this.setIndices(mesh.indices());
        this.setUVs(mesh.uvs());
        this.setNormals(mesh.normals());
        GL30.glBindVertexArray(0);
    }

    /**
     * Adds an attribute to this mesh.
     *
     * @param array Attribute data.
     * @param size 3 for 3D coordinates, 2 for 2D coordinates.
     * @param index Index of the attribute list.
     */
    private void setAttribute(float[] array, int size, int index) {
        if(array != null && array.length > 0) {
            var vbo = this.vertexBuffers.containsKey(index) ? this.vertexBuffers.get(index) : GL15.glGenBuffers();
            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
            var buffer = BufferUtils.createFloatBuffer(array.length).put(array).flip();
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
            GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
            this.vertexBuffers.put(index, vbo);
        }
    }

    /**
     * Sets the vertices of this mesh and updates the vertex count.
     *
     * @param vertices Array of vertices.
     * @param size 3 for 3D vertices, 2 for 2D vertices.
     */
    private void setVertices(float[] vertices, int size) {
        this.setAttribute(vertices, size, 0);
        if(vertices != null) {
            this.vertexCount = vertices.length / size;
        } else {
            this.vertexCount = 0;
        }
    }

    /**
     * Sets the indices of this mesh and updates the indices count.
     *
     * @param indices Array of indices.
     */
    private void setIndices(int[] indices) {
        if(indices != null && indices.length > 0) {
            if(this.indexBuffer < 0) {
                this.indexBuffer = GL15.glGenBuffers();
            }
            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
            var buffer = BufferUtils.createIntBuffer(indices.length).put(indices).flip();
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
            this.indicesCount = indices.length;
        } else {
            this.indicesCount = 0;
        }
    }

    /**
     * Sets the UVs of this mesh.
     *
     * @param uvs Array of UVs.
     */
    private void setUVs(float[] uvs) {
        this.setAttribute(uvs, 2, 1);
    }

    /**
     * Sets the normals of this mesh.
     *
     * @param normals Array of normals.
     */
    private void setNormals(float[] normals) {
        this.setAttribute(normals, 3, 2);
    }

    // TODO: How can we ensure the mesh is bound before drawing?

    /**
     * Binds this mesh.
     * Meshes must be bound before they are drawn.
     */
    public void bind() {
        GL30.glBindVertexArray(this.vertexArray);
        for(var attribute : this.vertexBuffers.keySet()) {
            GL20.glEnableVertexAttribArray(attribute);
        }
    }

    /**
     * Draws this mesh.
     * Meshes must be bound before they are drawn.
     */
    public void draw() {
        // TODO: Use triangle strips for 2D vertices
        if(this.indicesCount > 0) {
            GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);
        } else if(this.vertexCount > 0) {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertexCount);
        }
    }

    /**
     * Unbinds this mesh.
     */
    public void unbind() {
        for(var attribute : this.vertexBuffers.keySet()) {
            GL20.glDisableVertexAttribArray(attribute);
        }
        GL30.glBindVertexArray(0);
    }

    /**
     * Deletes this mesh.
     *
     * @see GL15#glDeleteBuffers(int)
     * @see GL30#glDeleteVertexArrays(int)
     */
    private void delete() {
        for(var vbo : this.vertexBuffers.values()) {
            GL15.glDeleteBuffers(vbo);
        }
        GL15.glDeleteBuffers(this.indexBuffer);
        GL30.glDeleteVertexArrays(this.vertexArray);
    }

    /**
     * Deletes all meshes that were created.
     * Called when the {@link OpenGLSystem} is terminated.
     */
    public static void deleteMeshes() {
        for(var mesh : MESHES.values()) {
            mesh.delete();
        }
    }
}