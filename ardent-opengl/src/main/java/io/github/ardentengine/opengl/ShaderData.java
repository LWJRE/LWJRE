package io.github.ardentengine.opengl;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.math.*;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.rendering.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Objects;

/**
 * OpenGL implementation of a shader.
 */
public class ShaderData {

    /** Keeps track of created shaders for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Shader, ShaderData> SHADERS = new HashMap<>();

    /** Keeps track of created uniform buffer objects. */
    private static final HashMap<String, Integer> UNIFORM_BUFFERS = new HashMap<>();
    /** Keeps track of uniform block bindings. */
    private static final HashMap<String, Integer> BLOCK_BINDINGS = new HashMap<>();
    /** Number of uniform blocks used to ensure each of them has a unique index. */
    private static int uniformBlocksCount = 0;

    /**
     * Returns the shader data corresponding to the given shader or returns a new one if it does not exist.
     *
     * @param shader Shader object.
     * @return The corresponding shader data.
     */
    public static ShaderData getOrCreate(Shader shader) {
        return SHADERS.computeIfAbsent(shader, ShaderData::new);
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
     * Creates and compiles an OpenGL shader.
     *
     * @see GL20#glCreateProgram()
     */
    private ShaderData(Shader shader) {
        this.program = GL20.glCreateProgram();
        if(!shader.getVertexCode().isEmpty()) {
            this.compile(GL20.GL_VERTEX_SHADER, shader.getVertexCode());
        }
        if(!shader.getFragmentCode().isEmpty()) {
            this.compile(GL20.GL_FRAGMENT_SHADER, shader.getFragmentCode());
        }
        GL20.glLinkProgram(this.program);
        GL20.glValidateProgram(this.program);
        // TODO: Automate this
        this.bindUniformBuffer("Camera3D");
        this.bindUniformBuffer("Camera2D");
        this.bindUniformBuffer("LightData");
    }

    // TODO: Shaders may be recompiled if the code changes

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
            System.out.println(GL20.glGetShaderInfoLog(shader));
            GL20.glDeleteShader(shader);
        } else {
            this.shaders.put(type, shader);
            GL20.glAttachShader(this.program, shader);
        }
    }

    /**
     * Creates a uniform buffer object and binds it to a uniform block with the given name.
     * <p>
     *     This method must be called before {@code setBuffer} methods are called to work properly.
     * </p>
     *
     * @param uniformBlock Name of the uniform block.
     */
    private void bindUniformBuffer(String uniformBlock) {
        var index = GL31.glGetUniformBlockIndex(this.program, uniformBlock);
        if(index >= 0) {
            int ubo = UNIFORM_BUFFERS.computeIfAbsent(uniformBlock, key -> GL15.glGenBuffers());
            var size = GL31.glGetActiveUniformBlocki(this.program, index, GL31.GL_UNIFORM_BLOCK_DATA_SIZE);
            int binding = BLOCK_BINDINGS.computeIfAbsent(uniformBlock, key -> uniformBlocksCount++);
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, ubo);
            GL15.glBufferData(GL31.GL_UNIFORM_BUFFER, BufferUtils.createByteBuffer(size), GL15.GL_DYNAMIC_DRAW);
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
            GL31.glBindBufferRange(GL31.GL_UNIFORM_BUFFER, binding, ubo, 0, size);
            GL31.glUniformBlockBinding(this.program, index, binding);
        }
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

    /**
     * Sets the value of a float uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, float value) {
        GL41.glProgramUniform1f(this.program, this.getUniformLocation(variable), value);
    }

    /**
     * Sets the value of a vec2 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     */
    public void set(String variable, float x, float y) {
        GL41.glProgramUniform2f(this.program, this.getUniformLocation(variable), x, y);
    }

    /**
     * Sets the value of a vec2 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Vector2 value) {
        value = Objects.requireNonNullElse(value, Vector2.ZERO);
        this.set(variable, value.x(), value.y());
    }

    /**
     * Sets the value of a vec3 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     */
    public void set(String variable, float x, float y, float z) {
        GL41.glProgramUniform3f(this.program, this.getUniformLocation(variable), x, y, z);
    }

    /**
     * Sets the value of a vec3 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Vector3 value) {
        value = Objects.requireNonNullElse(value, Vector3.ZERO);
        this.set(variable, value.x(), value.y(), value.z());
    }

    /**
     * Sets the value of a vec4 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     * @param w w component of the variable.
     */
    public void set(String variable, float x, float y, float z, float w) {
        GL41.glProgramUniform4f(this.program, this.getUniformLocation(variable), x, y, z, w);
    }

    /**
     * Sets the value of a vec4 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Vector4 value) {
        value = Objects.requireNonNullElse(value, Vector4.ZERO);
        this.set(variable, value.x(), value.y(), value.z(), value.w());
    }

    /**
     * Sets the value of a vec4 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Color value) {
        value = Objects.requireNonNullElse(value, new Color(0.0f, 0.0f, 0.0f, 0.0f));
        this.set(variable, value.r(), value.g(), value.b(), value.a());
    }

    /**
     * Sets the value of an int uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, int value) {
        GL41.glProgramUniform1i(this.program, this.getUniformLocation(variable), value);
    }

    /**
     * Sets the value of a vec2i uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     */
    public void set(String variable, int x, int y) {
        GL41.glProgramUniform2i(this.program, this.getUniformLocation(variable), x, y);
    }

    /**
     * Sets the value of a vec2i uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Vector2i value) {
        value = Objects.requireNonNullElse(value, Vector2i.ZERO);
        this.set(variable, value.x(), value.y());
    }

    /**
     * Sets the value of a vec3i uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     */
    public void set(String variable, int x, int y, int z) {
        GL41.glProgramUniform3i(this.program, this.getUniformLocation(variable), x, y, z);
    }

    /**
     * Sets the value of a vec3i uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Vector3i value) {
        value = Objects.requireNonNullElse(value, Vector3i.ZERO);
        this.set(variable, value.x(), value.y(), value.z());
    }

    /**
     * Sets the value of a vec4i uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param x X component of the variable.
     * @param y Y component of the variable.
     * @param z Z component of the variable.
     * @param w W component of the variable.
     */
    public void set(String variable, int x, int y, int z, int w) {
        GL41.glProgramUniform4i(this.program, this.getUniformLocation(variable), x, y, z, w);
    }

    /**
     * Sets the value of a vec4i uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param value Value to assign.
     */
    public void set(String variable, Vector4i value) {
        value = Objects.requireNonNullElse(value, Vector4i.ZERO);
        this.set(variable, value.x(), value.y(), value.z(), value.w());
    }

    // TODO: Invert multiplications in shaders instead of transposing matrices
    // EG: vertex * transform instead of transform * vertex

    /**
     * Sets the value of a mat2 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param matrix Matrix to assign to the variable.
     */
    public void set(String variable, Matrix2 matrix) {
        matrix = Objects.requireNonNullElse(matrix, Matrix2.ZERO);
        var buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(matrix.m00()); buffer.put(matrix.m01());
        buffer.put(matrix.m10()); buffer.put(matrix.m11());
        GL41.glProgramUniformMatrix2fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    /**
     * Sets the value of a mat3x2 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param matrix Matrix to assign to the variable.
     */
    public void set(String variable, Matrix2x3 matrix) {
        matrix = Objects.requireNonNullElse(matrix, Matrix2x3.ZERO);
        var buffer = BufferUtils.createFloatBuffer(6);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
        GL41.glProgramUniformMatrix3x2fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    /**
     * Sets the value of a mat3 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param matrix Matrix to assign to the variable.
     */
    public void set(String variable, Matrix3 matrix) {
        matrix = Objects.requireNonNullElse(matrix, Matrix3.ZERO);
        var buffer = BufferUtils.createFloatBuffer(9);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22());
        GL41.glProgramUniformMatrix3fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    /**
     * Sets the value of a mat4x3 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param matrix Matrix to assign to the variable.
     */
    public void set(String variable, Matrix3x4 matrix) {
        matrix = Objects.requireNonNullElse(matrix, Matrix3x4.ZERO);
        var buffer = BufferUtils.createFloatBuffer(12);
        buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
        buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
        buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
        GL41.glProgramUniformMatrix4x3fv(this.program, this.getUniformLocation(variable), true, buffer.flip());
    }

    /**
     * Sets the value of a mat4 uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param matrix Matrix to assign to the variable.
     */
    public void set(String variable, Matrix4 matrix) {
        matrix = Objects.requireNonNullElse(matrix, Matrix4.ZERO);
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

    /**
     * Sets the value of a sampler2D uniform variable.
     *
     * @param variable Name of the uniform variable.
     * @param texture Texture to assign to the sampler.
     */
    public void set(String variable, Texture texture) {
        var location = this.getUniformLocation(variable);
        if(texture != null) {
            var textureUnit = this.getTextureUnit(variable);
            GL13.glActiveTexture(GL13.GL_TEXTURE1 + textureUnit);
            TextureData.getOrCreate(texture).bindTexture();
            GL41.glProgramUniform1i(this.program, location, 1 + textureUnit);
        } else {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL41.glProgramUniform1i(this.program, location, 0);
        }
    }

    public void set(String variable, Object value) {
        // TODO: Not a good implementation
        if(value instanceof Float f) {
            this.set(variable, f.floatValue());
        } else if(value instanceof Vector2 vec) {
            this.set(variable, vec);
        } else if(value instanceof Vector3 vec) {
            this.set(variable, vec);
        } else if(value instanceof Vector4 vec) {
            this.set(variable, vec);
        } else if(value instanceof Integer i) {
            this.set(variable, i.intValue());
        } else if(value instanceof Vector2i vec) {
            this.set(variable, vec);
        } else if(value instanceof Vector3i vec) {
            this.set(variable, vec);
        } else if(value instanceof Vector4i vec) {
            this.set(variable, vec);
        } else if(value instanceof Matrix2 mat) {
            this.set(variable, mat);
        } else if(value instanceof Matrix2x3 mat) {
            this.set(variable, mat);
        } else if(value instanceof Matrix3 mat) {
            this.set(variable, mat);
        } else if(value instanceof Matrix3x4 mat) {
            this.set(variable, mat);
        } else if(value instanceof Matrix4 mat) {
            this.set(variable, mat);
        } else if(value instanceof Texture texture) {
            this.set(variable, texture);
        }
    }

    /**
     * Sets the data of a uniform buffer object bound to a uniform block with the given name if it exists.
     * Data contained in UBOs can be accessed by all shaders.
     * This method has no effect if no UBO is bound to a uniform block with the given name.
     *
     * @param name Name of the uniform block in the shader. Used here as a key.
     * @param buffer Data to store in the UBO. The {@link FloatBuffer#flip()} method must be called on the given buffer before passing it to this method.
     * @param offset Offset at which to put the data in bytes.
     */
    public static void setBuffer(String name, FloatBuffer buffer, long offset) {
        UNIFORM_BUFFERS.computeIfPresent(name, (key, ubo) -> {
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, ubo);
            GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, offset, buffer);
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
            return ubo;
        });
    }

    /**
     * Sets the data of a uniform buffer object bound to a uniform block with the given name if it exists.
     * Data contained in UBOs can be accessed by all shaders.
     * This method has no effect if no UBO is bound to a uniform block with the given name.
     *
     * @param name Name of the uniform block in the shader. Used here as a key.
     * @param data Data to store in the UBO. The {@link FloatBuffer#flip()} method must be called on the given buffer before passing it to this method.
     */
    public static void setBuffer(String name, FloatBuffer data) {
        setBuffer(name, data, 0);
    }

    /**
     * Sets the data of a uniform buffer object bound to a uniform block with the given name if it exists.
     * Data contained in UBOs can be accessed by all shaders.
     * This method has no effect if no UBO is bound to a uniform block with the given name.
     *
     * @param name Name of the uniform block in the shader. Used here as a key.
     * @param data Data to store in the UBO.
     * @param offset Offset at which to put the data in bytes.
     */
    public static void setBuffer(String name, float[] data, long offset) {
        setBuffer(name, BufferUtils.createFloatBuffer(data.length).put(data).flip(), offset);
    }

    /**
     * Sets the data of a uniform buffer object bound to a uniform block with the given name if it exists.
     * Data contained in UBOs can be accessed by all shaders.
     * This method has no effect if no UBO is bound to a uniform block with the given name.
     *
     * @param name Name of the uniform block in the shader. Used here as a key.
     * @param data Data to store in the UBO.
     */
    public static void setBuffer(String name, float[] data) {
        setBuffer(name, data, 0);
    }

    /**
     * Sets the data of a uniform buffer object bound to a uniform block with the given name if it exists.
     * Data contained in UBOs can be accessed by all shaders.
     * This method has no effect if no UBO is bound to a uniform block with the given name.
     *
     * @param name Name of the uniform block in the shader. Used here as a key.
     * @param buffer Data to store in the UBO. The {@link IntBuffer#flip()} method must be called on the given buffer before passing it to this method.
     * @param offset Offset at which to put the data in bytes.
     */
    public static void setBuffer(String name, IntBuffer buffer, long offset) {
        UNIFORM_BUFFERS.computeIfPresent(name, (key, ubo) -> {
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, ubo);
            GL15.glBufferSubData(GL31.GL_UNIFORM_BUFFER, offset, buffer);
            GL15.glBindBuffer(GL31.GL_UNIFORM_BUFFER, 0);
            return ubo;
        });
    }

    /**
     * Sets the data of a uniform buffer object bound to a uniform block with the given name if it exists.
     * Data contained in UBOs can be accessed by all shaders.
     * This method has no effect if no UBO is bound to a uniform block with the given name.
     *
     * @param name Name of the uniform block in the shader. Used here as a key.
     * @param data Data to store in the UBO.
     * @param offset Offset at which to put the data in bytes.
     */
    public static void setBuffer(String name, int data, long offset) {
        setBuffer(name, BufferUtils.createIntBuffer(1).put(data).flip(), offset);
    }

    /**
     * Starts this shader program.
     * Subsequent draw calls will use this shader.
     */
    public void start() {
        GL20.glUseProgram(this.program);
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
     * Called when the {@link OpenGLSystem} is terminated.
     */
    public static void deleteShaders() {
        for(var shader : SHADERS.values()) {
            shader.delete();
        }
        for(var ubo : UNIFORM_BUFFERS.values()) {
            GL15.glDeleteBuffers(ubo);
        }
    }
}