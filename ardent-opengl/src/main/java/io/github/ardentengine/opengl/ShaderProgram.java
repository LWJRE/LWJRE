package io.github.ardentengine.opengl;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.math.*;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.rendering.Texture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

/**
 * OpenGL implementation of a shader.
 */
public class ShaderProgram {

    /** Keeps track of created shaders for them to be deleted when the rendering system is terminated. */
    private static final HashMap<Shader, ShaderProgram> SHADERS = new HashMap<>();

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
    public static ShaderProgram getOrCreate(Shader shader) {
        return SHADERS.computeIfAbsent(shader, ShaderProgram::new);
    }

    /** Shader program object. */
    private final int program;

    /** Shader objects store in a map by their type. */
    private final HashMap<Integer, Integer> shaders = new HashMap<>();
    /** Location of uniform variables stored in a map by their name. */
    private final HashMap<String, Integer> uniformLocations = new HashMap<>();
    /** Type of uniform variables stored in a map by their name. */
    private final HashMap<String, Integer> uniformTypes = new HashMap<>();

    /** Texture units used by uniform variables stored in a map by their name. */
    private final HashMap<String, Integer> textureUnits = new HashMap<>();
    /** Number of textures used to increase texture units every time a texture is loaded. */
    private int textures = 0;

    /**
     * Creates and compiles an OpenGL shader.
     *
     * @see GL20#glCreateProgram()
     */
    private ShaderProgram(Shader shader) {
        this.program = GL20.glCreateProgram();
        this.compile(GL20.GL_VERTEX_SHADER, shader.vertexCode());
        this.compile(GL20.GL_FRAGMENT_SHADER, shader.fragmentCode());
        GL20.glLinkProgram(this.program);
        GL20.glValidateProgram(this.program);
        // TODO: Automate this
        this.bindUniformBuffer("Camera3D");
        this.bindUniformBuffer("Camera2D");
        this.bindUniformBuffer("LightData");
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

    private int getUniformType(String variable) {
        return this.uniformTypes.computeIfAbsent(variable, name -> {
            try(var memoryStack = MemoryStack.stackPush()) {
                var size = memoryStack.mallocInt(1);
                var type = memoryStack.mallocInt(1);
                GL20.glGetActiveUniform(this.program, this.getUniformLocation(name), size, type);
                return type.get();
            }
        });
    }

    private void setUniform(String variable, float x, float y, float z, float w) {
        var location = this.getUniformLocation(variable);
        switch(this.getUniformType(variable)) {
            case GL11.GL_FLOAT -> GL41.glProgramUniform1f(this.program, location, x);
            case GL20.GL_FLOAT_VEC2 -> GL41.glProgramUniform2f(this.program, location, x, y);
            case GL20.GL_FLOAT_VEC3 -> GL41.glProgramUniform3f(this.program, location, x, y, z);
            case GL20.GL_FLOAT_VEC4 -> GL41.glProgramUniform4f(this.program, location, x, y, z, w);
            case GL11.GL_INT -> GL41.glProgramUniform1i(this.program, location, (int) x);
            case GL20.GL_INT_VEC2 -> GL41.glProgramUniform2i(this.program, location, (int) x, (int) y);
            case GL20.GL_INT_VEC3 -> GL41.glProgramUniform3i(this.program, location, (int) x, (int) y, (int) z);
            case GL20.GL_INT_VEC4 -> GL41.glProgramUniform4i(this.program, location, (int) x, (int) y, (int) z, (int) w);
        }
    }

    private void setUniform(String variable, int x, int y, int z, int w) {
        var location = this.getUniformLocation(variable);
        switch(this.getUniformType(variable)) {
            case GL11.GL_FLOAT -> GL41.glProgramUniform1f(this.program, location, x);
            case GL20.GL_FLOAT_VEC2 -> GL41.glProgramUniform2f(this.program, location, x, y);
            case GL20.GL_FLOAT_VEC3 -> GL41.glProgramUniform3f(this.program, location, x, y, z);
            case GL20.GL_FLOAT_VEC4 -> GL41.glProgramUniform4f(this.program, location, x, y, z, w);
            case GL11.GL_INT -> GL41.glProgramUniform1i(this.program, location, x);
            case GL20.GL_INT_VEC2 -> GL41.glProgramUniform2i(this.program, location, x, y);
            case GL20.GL_INT_VEC3 -> GL41.glProgramUniform3i(this.program, location, x, y, z);
            case GL20.GL_INT_VEC4 -> GL41.glProgramUniform4i(this.program, location, x, y, z, w);
        }
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
    private void setUniform(String variable, Texture texture) {
        var location = this.getUniformLocation(variable);
        // TODO: Texture can no longer be null with the new setup
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

    private void setUniform(String variable, FloatBuffer buffer) {
        var location = this.getUniformLocation(variable);
        // TODO: Invert multiplications in shaders instead of transposing matrices?
        switch(this.getUniformType(variable)) {
            case GL20.GL_FLOAT_MAT2 -> GL41.glProgramUniformMatrix2fv(this.program, location, true, buffer);
            case GL21.GL_FLOAT_MAT3x2 -> GL41.glProgramUniformMatrix3x2fv(this.program, location, true, buffer);
            case GL20.GL_FLOAT_MAT3 -> GL41.glProgramUniformMatrix3fv(this.program, location, true, buffer);
            case GL21.GL_FLOAT_MAT4x3 -> GL41.glProgramUniformMatrix4x3fv(this.program, location, true, buffer);
            case GL20.GL_FLOAT_MAT4 -> GL41.glProgramUniformMatrix4fv(this.program, location, true, buffer);
        }
    }

    public void setUniform(String variable, Object value) {
        if(value instanceof Float) {
            this.setUniform(variable, (float) value, 0.0f, 0.0f, 0.0f);
        } else if(value instanceof Vector2 vector) {
            this.setUniform(variable, vector.x(), vector.y(), 0.0f, 0.0f);
        } else if(value instanceof Vector3 vector) {
            this.setUniform(variable, vector.x(), vector.y(), vector.z(), 0.0f);
        } else if(value instanceof Vector4 vector) {
            this.setUniform(variable, vector.x(), vector.y(), vector.z(), vector.w());
        } else if(value instanceof Integer) {
            this.setUniform(variable, (int) value, 0, 0, 0);
        } else if(value instanceof Vector2i vector) {
            this.setUniform(variable, vector.x(), vector.y(), 0, 0);
        } else if(value instanceof Vector3i vector) {
            this.setUniform(variable, vector.x(), vector.y(), vector.z(), 0);
        } else if(value instanceof Vector4i vector) {
            this.setUniform(variable, vector.x(), vector.y(), vector.z(), vector.w());
        } else if(value instanceof Color color) {
            this.setUniform(variable, color.r(), color.g(), color.b(), color.a());
        } else if(value instanceof Texture texture) {
            this.setUniform(variable, texture);
        } else if(value instanceof Matrix2 matrix) {
            var buffer = BufferUtils.createFloatBuffer(4);
            buffer.put(matrix.m00()); buffer.put(matrix.m01());
            buffer.put(matrix.m10()); buffer.put(matrix.m11());
            this.setUniform(variable, buffer.flip());
        } else if(value instanceof Matrix2x3 matrix) {
            var buffer = BufferUtils.createFloatBuffer(6);
            buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
            buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
            this.setUniform(variable, buffer.flip());
        } else if(value instanceof Matrix3 matrix) {
            var buffer = BufferUtils.createFloatBuffer(9);
            buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
            buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
            buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22());
            this.setUniform(variable, buffer.flip());
        } else if(value instanceof Matrix3x4 matrix) {
            var buffer = BufferUtils.createFloatBuffer(12);
            buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
            buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
            buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
            this.setUniform(variable, buffer.flip());
        } else if(value instanceof Matrix4 matrix) {
            var buffer = BufferUtils.createFloatBuffer(16);
            buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
            buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
            buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
            buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
            this.setUniform(variable, buffer.flip());
        } else {
            Logger.error("Variable of type " + value.getClass().getName() + " not supported in shaders");
        }
        // TODO: Boolean value
        // TODO: Allow null to be passed to textures
    }

    // TODO: Redo uniform buffers in a better way because this is very ugly

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