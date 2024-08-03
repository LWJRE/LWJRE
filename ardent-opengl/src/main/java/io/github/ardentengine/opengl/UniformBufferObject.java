package io.github.ardentengine.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

import java.util.ArrayList;
import java.util.HashMap;

public class UniformBufferObject {

    // TODO: This needs a better solution

    private static final HashMap<String, Integer> BINDINGS = new HashMap<>();
    private static int uniformBlocksCount = 0;
    private static final ArrayList<UniformBufferObject> UNIFORM_BUFFER_OBJECTS = new ArrayList<>();

    private static int getBinding(String uniformBlock) {
        return BINDINGS.computeIfAbsent(uniformBlock, key -> uniformBlocksCount++);
    }

    public static void bind(int shader, String uniformBlock) {
        var index = GL31.glGetUniformBlockIndex(shader, uniformBlock);
        if(index >= 0) {
            GL31.glUniformBlockBinding(shader, index, getBinding(uniformBlock));
        }
    }

    private final int ubo;

    public UniformBufferObject(String uniformBlock, int bytes) {
        this.ubo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, this.ubo);
        GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, BufferUtils.createByteBuffer(bytes), GL15.GL_STATIC_DRAW);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
        GL31.glBindBufferRange(GL31.GL_UNIFORM_BUFFER, getBinding(uniformBlock), this.ubo, 0, bytes);
        UNIFORM_BUFFER_OBJECTS.add(this);
    }

    public void putData(float[] data, long offset) {
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, this.ubo);
        var buffer = BufferUtils.createFloatBuffer(data.length).put(data).flip();
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, offset, buffer);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    public void putData(float[] data) {
        this.putData(data, 0L);
    }

    public void putData(int[] data, long offset) {
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, this.ubo);
        var buffer = BufferUtils.createIntBuffer(data.length).put(data).flip();
        GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, offset, buffer);
        GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
    }

    private void delete() {
        GL15.glDeleteBuffers(this.ubo);
    }

    public static void deleteBuffers() {
        for(var ubo : UNIFORM_BUFFER_OBJECTS) {
            ubo.delete();
        }
    }
}