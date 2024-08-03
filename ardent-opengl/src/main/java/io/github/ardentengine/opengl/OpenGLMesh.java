package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.Mesh;
import io.github.ardentengine.core.rendering.MeshData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;

/**
 * OpenGL implementation of a mesh.
 */
public class OpenGLMesh extends MeshData {

    // TODO: Find a better solution that does not require constant binding and unbinding

    /** Keeps track of created meshes for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Mesh, OpenGLMesh> MESHES = new HashMap<>();

    /**
     * Returns the mesh data corresponding to the given mesh or returns a new one if it does not exist.
     *
     * @param mesh Mesh object.
     * @return The corresponding mesh data.
     */
    public static OpenGLMesh getOrCreate(Mesh mesh) {
        return MESHES.computeIfAbsent(mesh, key -> new OpenGLMesh());
    }

    /** Vertex array object. */
    private final int vertexArray;
    /** Map of vertex buffer objects and attribute lists. */
    private final HashMap<Integer, Integer> vertexBuffers = new HashMap<>();

    /** Number of vertices in this mesh. */
    private int vertexCount = 0;

    /** Vertex buffer object linked to indices. */
    private int indexBuffer = 0;
    /** Number of indices in this mesh. */
    private int indicesCount = 0;

    /**
     * Creates an OpenGL mesh.
     *
     * @see GL30#glGenVertexArrays()
     */
    private OpenGLMesh() {
        this.vertexArray = GL30.glGenVertexArrays();
    }

    /**
     * Adds an attribute to this mesh.
     *
     * @param array Attribute data.
     * @param size 3 for 3D coordinates, 2 for 2D coordinates.
     * @param index Index of the attribute list.
     */
    private void setAttribute(float[] array, int size, int index) {
        GL30.glBindVertexArray(this.vertexArray);
        var vbo = this.vertexBuffers.containsKey(index) ? this.vertexBuffers.get(index) : GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        var buffer = BufferUtils.createFloatBuffer(array.length).put(array).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        GL30.glBindVertexArray(0);
        this.vertexBuffers.put(index, vbo);
    }

    /**
     * Adds the given vertices as an attribute and stores the number of vertices.
     *
     * @param vertices Array of vertices.
     * @param size 3 for 3D coordinates, 2 for 2D coordinates.
     */
    private void setVertices(float[] vertices, int size) {
        this.setAttribute(vertices, size, 0);
        this.vertexCount = vertices.length / size;
    }

    @Override
    public void setVertices3D(float[] vertices) {
        this.setVertices(vertices, 3);
    }

    @Override
    public void setVertices2D(float[] vertices) {
        this.setVertices(vertices, 2);
    }

    @Override
    public void setIndices(int[] indices) {
        GL30.glBindVertexArray(this.vertexArray);
        if(this.indicesCount == 0) {
            this.indexBuffer = GL15.glGenBuffers();
        }
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
        var buffer = BufferUtils.createIntBuffer(indices.length).put(indices).flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
        GL30.glBindVertexArray(0);
        this.indicesCount = indices.length;
    }

    @Override
    public void setUVs(float[] uvs) {
        this.setAttribute(uvs, 2, 1);
    }

    @Override
    public void setNormals(float[] normals) {
        this.setAttribute(normals, 3, 2);
    }

    @Override
    public void draw() {
        GL30.glBindVertexArray(this.vertexArray);
        for(var attribute : this.vertexBuffers.keySet()) {
            GL20.glEnableVertexAttribArray(attribute);
        }
        // TODO: Use triangle strips for 2D vertices
        if(this.indicesCount > 0) {
            GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);
        } else if(this.vertexCount > 0) {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertexCount);
        }
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
     * Called when the {@link RenderingSystem} is terminated.
     */
    public static void deleteMeshes() {
        for(var mesh : MESHES.values()) {
            mesh.delete();
        }
    }
}