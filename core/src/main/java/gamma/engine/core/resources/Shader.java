package gamma.engine.core.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import vecmatlib.*;
import vecmatlib.matrix.Mat4f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Class representing a shader program resource.
 *
 * @author Nico
 */
public final class Shader {

	/** List of all the shaders created used to load static uniforms */
	private static final ArrayList<Shader> SHADERS = new ArrayList<>();

	/** Id of this shader program object */
	private final int program;

	/** Map of uniform variables and their locations */
	private final HashMap<String, Integer> uniformLocations = new HashMap<>();
	/** Map of values to load in uniform variables when starting the shader */
	private final HashMap<Integer, Runnable> uniformVariables = new HashMap<>();

	/**
	 * Constructs a shader object.
	 * This constructor should not be called, shaders should be created with {@link ShaderLoader#getOrLoad(String)}.
	 *
	 * @param shaders Shader files
	 */
	private Shader(Collection<Integer> shaders) {
		this.program = GL20.glCreateProgram();
		shaders.forEach(shader -> GL20.glAttachShader(this.program, shader));
		GL20.glLinkProgram(this.program);
		GL20.glValidateProgram(this.program);
		SHADERS.add(this);
	}

	/**
	 * Starts this shader.
	 * This method should be called before rendering something that uses this shader.
	 */
	public void start() {
		GL20.glUseProgram(this.program);
		this.uniformVariables.values().forEach(Runnable::run);
	}

	/**
	 * Gets the location of a uniform variable.
	 *
	 * @param variable Name of the variable
	 * @return Location of the uniform variable
	 */
	private int uniformLocation(String variable) {
		if(this.uniformLocations.containsKey(variable)) {
			return this.uniformLocations.get(variable);
		} else {
			int location = GL20.glGetUniformLocation(Shader.this.program, variable);
			this.uniformLocations.put(variable, location);
			return location;
		}
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param value Value of the uniform variable
	 */
	public void loadUniform(String name, float value) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform1f(location, value));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param value Value of the uniform variable
	 */
	public void loadUniform(String name, int value) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform1i(location, value));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 */
	public void loadUniform(String name, float x, float y) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform2f(location, x, y));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 */
	public void loadUniform(String name, int x, int y) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform2i(location, x, y));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param vec2 Value of the uniform variable
	 */
	public void loadUniform(String name, Float2 vec2) {
		this.loadUniform(name, vec2.x(), vec2.y());
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param vec2 Value of the uniform variable
	 */
	public void loadUniform(String name, Int2 vec2) {
		this.loadUniform(name, vec2.x(), vec2.y());
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 */
	public void loadUniform(String name, float x, float y, float z) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform3f(location, x, y, z));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 */
	public void loadUniform(String name, int x, int y, int z) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform3i(location, x, y, z));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param vec3 Value of the uniform variable
	 */
	public void loadUniform(String name, Float3 vec3) {
		this.loadUniform(name, vec3.x(), vec3.y(), vec3.z());
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param vec3 Value of the uniform variable
	 */
	public void loadUniform(String name, Int3 vec3) {
		this.loadUniform(name, vec3.x(), vec3.y(), vec3.z());
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 * @param w W value
	 */
	public void loadUniform(String name, float x, float y, float z, float w) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform4f(location, x, y, z, w));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 * @param w W value
	 */
	public void loadUniform(String name, int x, int y, int z, int w) {
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniform4i(location, x, y, z, w));
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param vec4 Value of the uniform variable
	 */
	public void loadUniform(String name, Float4 vec4) {
		this.loadUniform(name, vec4.x(), vec4.y(), vec4.z(), vec4.w());
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param vec4 Value of the uniform variable
	 */
	public void loadUniform(String name, Int4 vec4) {
		this.loadUniform(name, vec4.x(), vec4.y(), vec4.z(), vec4.w());
	}

	/**
	 * Loads a uniform variable into this shader program.
	 * This method should be called before {@link Shader#start()}.
	 *
	 * @param name Name of the uniform variable
	 * @param matrix Value of the uniform variable
	 */
	public void loadUniform(String name, Mat4f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
		buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
		buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
		buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
		int location = this.uniformLocation(name);
		this.uniformVariables.put(location, () -> GL20.glUniformMatrix4fv(location, true, buffer.flip()));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param value Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, float value) {
		SHADERS.forEach(shader -> shader.loadUniform(name, value));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param value Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, int value) {
		SHADERS.forEach(shader -> shader.loadUniform(name, value));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 */
	public static void loadStaticUniform(String name, float x, float y) {
		SHADERS.forEach(shader -> shader.loadUniform(name, x, y));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 */
	public static void loadStaticUniform(String name, int x, int y) {
		SHADERS.forEach(shader -> shader.loadUniform(name, x, y));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param vec2 Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Float2 vec2) {
		SHADERS.forEach(shader -> shader.loadUniform(name, vec2));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param vec2 Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Int2 vec2) {
		SHADERS.forEach(shader -> shader.loadUniform(name, vec2));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 */
	public static void loadStaticUniform(String name, float x, float y, float z) {
		SHADERS.forEach(shader -> shader.loadUniform(name, x, y, z));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 */
	public static void loadStaticUniform(String name, int x, int y, int z) {
		SHADERS.forEach(shader -> shader.loadUniform(name, x, y, z));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param vec3 Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Float3 vec3) {
		SHADERS.forEach(shader -> shader.loadUniform(name, vec3));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param vec3 Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Int3 vec3) {
		SHADERS.forEach(shader -> shader.loadUniform(name, vec3));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 * @param w W value
	 */
	public static void loadStaticUniform(String name, float x, float y, float z, float w) {
		SHADERS.forEach(shader -> shader.loadUniform(name, x, y, z, w));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param x X value
	 * @param y Y value
	 * @param z Z value
	 * @param w W value
	 */
	public static void loadStaticUniform(String name, int x, int y, int z, int w) {
		SHADERS.forEach(shader -> shader.loadUniform(name, x, y, z, w));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param vec4 Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Float4 vec4) {
		SHADERS.forEach(shader -> shader.loadUniform(name, vec4));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param vec4 Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Int4 vec4) {
		SHADERS.forEach(shader -> shader.loadUniform(name, vec4));
	}

	/**
	 * Loads a uniform variable in all shaders.
	 *
	 * @param name Name of the uniform variable
	 * @param matrix Value of the uniform variable
	 */
	public static void loadStaticUniform(String name, Mat4f matrix) {
		SHADERS.forEach(shader -> shader.loadUniform(name, matrix));
	}
}