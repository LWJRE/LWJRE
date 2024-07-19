package io.github.ardentengine.opengl;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.rendering.Mesh;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.rendering.ShaderData;
import io.github.ardentengine.core.rendering.Texture;
import io.github.scalamath.vecmatlib.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL41;

import java.util.HashMap;

/**
 * OpenGL implementation of a shader.
 */
public class OpenGLShader extends ShaderData {

    /** Keeps track of created shaders for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Shader, OpenGLShader> SHADERS = new HashMap<>();

    /**
     * Returns the shader data corresponding to the given shader or returns a new one if it does not exist.
     *
     * @param shader Shader object.
     * @return The corresponding shader data.
     */
    public static OpenGLShader getOrCreate(Shader shader) {
        return SHADERS.computeIfAbsent(shader, key -> new OpenGLShader());
    }

    /** Shader program object. */
    private final int program;

    /** Shader objects store in a map by their type. */
    private final HashMap<Integer, Integer> shaders = new HashMap<>();
    /** Location of uniform variables stored in a map by their name. */
    private final HashMap<String, Integer> uniformLocations = new HashMap<>();

    /** Texture units used by uniform variables stored in a map by their name. */
    private final HashMap<String, Integer> textureUnits = new HashMap<>();
    /** Number of textures used to increase texture units every time a texture is loaded. */
    private int textures = 0;

    /**
     * Creates an OpenGL shader.
     *
     * @see GL20#glCreateProgram()
     */
    private OpenGLShader() {
        this.program = GL20.glCreateProgram();
    }

    /**
     * Returns a currently existing shader or create a new one for the given type.
     *
     * @param type Shader type.
     * @return A shader object of the given type.
     */
    private int getShader(int type) {
        return this.shaders.computeIfAbsent(type, GL20::glCreateShader);
    }

    /**
     * Compiles a shader for the given type with the given source code.
     *
     * @param type Shader type.
     * @param code Shader code.
     */
    private void compile(int type, CharSequence code) {
        var shader = this.getShader(type);
        GL20.glShaderSource(shader, code);
        GL20.glCompileShader(shader);
        if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            Logger.error("Could not compile shader");
            Logger.info(GL20.glGetShaderInfoLog(shader));
            GL20.glDeleteShader(shader);
        } else {
            this.shaders.put(type, shader);
            GL20.glAttachShader(this.program, shader);
        }
    }

    @Override
    public void compile(CharSequence vertexCode, CharSequence fragmentCode) {
        this.compile(GL20.GL_VERTEX_SHADER, vertexCode);
        this.compile(GL20.GL_FRAGMENT_SHADER, fragmentCode);
        GL20.glLinkProgram(this.program);
        GL20.glValidateProgram(this.program);
    }

    /**
     * Gets the location of a uniform variable with the given name.
     * Uniform locations are cached for future access.
     *
     * @param variable Name of the uniform variable as it is declared in glsl.
     * @return The location of the uniform variable.
     */
    private int getUniformLocation(String variable) {
        return this.uniformLocations.computeIfAbsent(variable, name -> GL20.glGetUniformLocation(this.program, name));
    }

    @Override
    public void set(String variable, float value) {
        GL41.glProgramUniform1f(this.program, this.getUniformLocation(variable), value);
    }

    @Override
    public void set(String variable, float x, float y) {
        GL41.glProgramUniform2f(this.program, this.getUniformLocation(variable), x, y);
    }

    @Override
    public void set(String variable, float x, float y, float z) {
        GL41.glProgramUniform3f(this.program, this.getUniformLocation(variable), x, y, z);
    }

    @Override
    public void set(String variable, float x, float y, float z, float w) {
        GL41.glProgramUniform4f(this.program, this.getUniformLocation(variable), x, y, z, w);
    }

    @Override
    public void set(String variable, int value) {
        GL41.glProgramUniform1i(this.program, this.getUniformLocation(variable), value);
    }

    @Override
    public void set(String variable, boolean value) {
        this.set(variable, value ? 1 : 0);
    }

    @Override
    public void set(String variable, int x, int y) {
        GL41.glProgramUniform2i(this.program, this.getUniformLocation(variable), x, y);
    }

    @Override
    public void set(String variable, int x, int y, int z) {
        GL41.glProgramUniform3i(this.program, this.getUniformLocation(variable), x, y, z);
    }

    @Override
    public void set(String variable, int x, int y, int z, int w) {
        GL41.glProgramUniform4i(this.program, this.getUniformLocation(variable), x, y, z, w);
    }

    @Override
    public void set(String variable, Mat2f matrix) {
        var buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(matrix.m00()); buffer.put(matrix.m01());
        buffer.put(matrix.m10()); buffer.put(matrix.m11());
        GL41.glProgramUniformMatrix2fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    @Override
    public void set(String variable, Mat2x3f matrix) {
        var buffer = BufferUtils.createFloatBuffer(6);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
        GL41.glProgramUniformMatrix3x2fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    @Override
    public void set(String variable, Mat3f matrix) {
        var buffer = BufferUtils.createFloatBuffer(9);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22());
        GL41.glProgramUniformMatrix3fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    @Override
    public void set(String variable, Mat3x4f matrix) {
        var buffer = BufferUtils.createFloatBuffer(12);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
        GL41.glProgramUniformMatrix4x3fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    @Override
    public void set(String variable, Mat4f matrix) {
        var buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
        buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
        GL41.glProgramUniformMatrix4fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    /**
     * Gets a texture unit for the given variable.
     * Texture units are increased by 1 every time a texture is loaded into the shader.
     *
     * @param variable Name of the uniform variable.
     * @return The texture unit used by that variable.
     */
    private int getTextureUnit(String variable) {
        return this.textureUnits.computeIfAbsent(variable, key -> this.textures++);
    }

    @Override
    public void set(String variable, Texture texture) {
        var location = this.getUniformLocation(variable);
        if(texture != null) {
            var textureUnit = this.getTextureUnit(variable);
            GL13.glActiveTexture(GL13.GL_TEXTURE1 + textureUnit);
            OpenGLTexture.getOrCreate(texture).bindTexture();
            GL41.glProgramUniform1i(this.program, location, 1 + textureUnit);
        } else {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL41.glProgramUniform1i(this.program, location, 0);
        }
    }

    @Override
    public void draw(Mesh mesh) {
        GL20.glUseProgram(this.program);
        super.draw(mesh);
    }

    /**
     * Deletes this shader.
     *
     * @see GL20#glDeleteShader(int)
     * @see GL20#glDeleteProgram(int)
     */
    private void delete() {
        for(var shader : this.shaders.values()) {
            GL20.glDetachShader(this.program, shader);
            GL20.glDeleteShader(shader);
        }
        GL20.glDeleteProgram(this.program);
    }

    /**
     * Deletes all shaders that were created.
     * Called when the {@link RenderingSystem} is terminated.
     */
    public static void deleteShaders() {
        for(var shader : SHADERS.values()) {
            shader.delete();
        }
    }
}