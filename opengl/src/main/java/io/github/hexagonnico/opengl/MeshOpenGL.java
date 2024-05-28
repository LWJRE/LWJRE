package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.rendering.MeshData;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

public class MeshOpenGL implements MeshData {

    private final int vertexArray;
    private final HashMap<Integer, Integer> vertexBuffers = new HashMap<>();

    private int vertexCount = 0;

    private int indexBuffer = 0;
    private int indicesCount = 0;

    public MeshOpenGL() {
        this.vertexArray = GL30.glGenVertexArrays();
    }

    private void setAttribute(float[] array, int size, int index) {
        GL30.glBindVertexArray(this.vertexArray);
        int vbo = this.vertexBuffers.containsKey(index) ? this.vertexBuffers.get(index) : GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(array.length).put(array).flip();
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
        GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
        this.vertexBuffers.put(index, vbo);
    }

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
        if(this.indicesCount == 0) {
            this.indexBuffer = GL15.glGenBuffers();
        }
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length).put(indices).flip();
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_DYNAMIC_DRAW);
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
        for(int attribute : this.vertexBuffers.keySet()) {
            GL20.glEnableVertexAttribArray(attribute);
        }
        // TODO: Implement different draw modes
        if(this.indicesCount > 0) {
            GL11.glDrawElements(GL11.GL_TRIANGLES, this.indicesCount, GL11.GL_UNSIGNED_INT, 0);
        } else if(this.vertexCount > 0) {
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertexCount);
        }
    }

    public void delete() {
        for(int vbo : this.vertexBuffers.values()) {
            GL15.glDeleteBuffers(vbo);
        }
        GL15.glDeleteBuffers(this.indexBuffer);
        GL30.glDeleteVertexArrays(this.vertexArray);
    }
}
