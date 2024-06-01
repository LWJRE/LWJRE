package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.rendering.ShaderData;
import io.github.hexagonnico.core.rendering.Texture;
import io.github.scalamath.vecmatlib.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;

public class OpenGLShader extends ShaderData {

    private final int program;

    private final HashMap<Integer, Integer> shaders = new HashMap<>();

    private final HashMap<String, Integer> uniformLocations = new HashMap<>();
    private final HashMap<Integer, Runnable> uniformCache = new HashMap<>();

    private final HashMap<String, Integer> textureUnits = new HashMap<>();
    private int textures = 0;

    public OpenGLShader() {
        this.program = GL20.glCreateProgram();
    }

    private void compileShader(int type, String code) {
        var shader = this.shaders.containsKey(type) ? this.shaders.get(type) : GL20.glCreateShader(type);
        GL20.glShaderSource(shader, code);
        GL20.glCompileShader(shader);
        if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.out.println(GL20.glGetShaderInfoLog(shader));
            GL20.glDeleteShader(shader);
            throw new RuntimeException("Could not compile shader"); // TODO: Better error handling
        }
        this.shaders.put(type, shader);
        GL20.glAttachShader(this.program, shader);
    }

    @Override
    public void compile(String vertexShader, String fragmentShader) {
        this.compileShader(GL20.GL_VERTEX_SHADER, vertexShader);
        this.compileShader(GL20.GL_FRAGMENT_SHADER, fragmentShader);
        GL20.glLinkProgram(this.program);
        GL20.glValidateProgram(this.program);
    }

    private int getUniformLocation(String variable) {
        return this.uniformLocations.computeIfAbsent(variable, key -> GL20.glGetUniformLocation(this.program, key));
    }

    @Override
    public void set(String variable, float value) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform1f(location, value));
    }

    @Override
    public void set(String variable, float x, float y) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform2f(location, x, y));
    }

    @Override
    public void set(String variable, float x, float y, float z) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform3f(location, x, y, z));
    }

    @Override
    public void set(String variable, float x, float y, float z, float w) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform4f(location, x, y, z, w));
    }

    @Override
    public void set(String variable, int value) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform1i(location, value));
    }

    @Override
    public void set(String variable, int x, int y) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform2i(location, x, y));
    }

    @Override
    public void set(String variable, int x, int y, int z) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform3i(location, x, y, z));
    }

    @Override
    public void set(String variable, int x, int y, int z, int w) {
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniform4i(location, x, y, z, w));
    }

    @Override
    public void set(String variable, Mat2f matrix) {
        var buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(matrix.m00()); buffer.put(matrix.m01());
        buffer.put(matrix.m10()); buffer.put(matrix.m11());
        buffer.flip();
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniformMatrix2fv(location, true, buffer));
    }

    @Override
    public void set(String variable, Mat2x3f matrix) {
        var buffer = BufferUtils.createFloatBuffer(9);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
        buffer.put(0.0f); buffer.put(0.0f); buffer.put(1.0f);
        buffer.flip();
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniformMatrix3fv(location, true, buffer));
    }

    @Override
    public void set(String variable, Mat3f matrix) {
        var buffer = BufferUtils.createFloatBuffer(9);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22());
        buffer.flip();
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniformMatrix3fv(location, true, buffer));
    }

    @Override
    public void set(String variable, Mat3x4f matrix) {
        var buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
        buffer.put(0.0f); buffer.put(0.0f); buffer.put(0.0f); buffer.put(1.0f);
        buffer.flip();
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniformMatrix4fv(location, true, buffer));
    }

    @Override
    public void set(String variable, Mat4f matrix) {
        var buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
        buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
        buffer.flip();
        var location = this.getUniformLocation(variable);
        this.uniformCache.put(location, () -> GL20.glUniformMatrix4fv(location, true, buffer));
    }

    private int getTextureUnit(String variable) {
        return this.textureUnits.computeIfAbsent(variable, key -> this.textures++);
    }

    @Override
    public void set(String variable, Texture texture) {
        var textureData = OpenGLApi.getTextureData(texture);
        if(textureData != null) {
            var location = this.getUniformLocation(variable);
            var textureUnit = this.getTextureUnit(variable);
            this.uniformCache.put(location, () -> {
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
                textureData.bind();
                GL20.glUniform1i(location, textureUnit);
            });
        }
    }

    public void start() {
        GL20.glUseProgram(this.program);
        for(var uniform : this.uniformCache.values()) {
            uniform.run();
        }
        this.uniformCache.clear();
    }

    public void delete() {
        for(var shader : this.shaders.values()) {
            GL20.glDetachShader(this.program, shader);
            GL20.glDeleteShader(shader);
        }
        GL20.glDeleteProgram(this.program);
    }
}
