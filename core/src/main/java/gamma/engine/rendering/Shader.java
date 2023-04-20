package gamma.engine.rendering;

import gamma.engine.resources.FileUtils;
import gamma.engine.resources.ResourceLoader;
import gamma.engine.resources.Resources;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vecmatlib.*;
import vecmatlib.matrix.Mat3f;
import vecmatlib.matrix.Mat4f;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.IntConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that represents an OpenGL shader program object.
 *
 * @author Nico
 */
public final class Shader extends DeletableResource {

	/** The main fragment shader's code */
	private static final String MAIN_FRAGMENT = FileUtils.readResourceAsString("gamma/engine/shaders/main_fragment.glsl");
	/** The main vertex shader's code */
	private static final String MAIN_VERTEX = FileUtils.readResourceAsString("gamma/engine/shaders/main_vertex.glsl");

	/** Set of all shaders needed by {@code setUniformStatic} methods */
	private static final HashSet<Shader> SHADERS = new HashSet<>();

	public static Shader getOrLoad(String path) {
		Object resource = Resources.getOrLoad(path);
		if(resource != null) {
			if(resource instanceof Shader shader)
				return shader;
			System.err.println("Resource " + path + " is not a shader");
		}
		return defaultShader();
	}

	public static Shader defaultShader() {
		return getOrLoad("gamma/engine/shaders/default_shader.glsl");
	}

	/** The shader that is currently running, needed to load or store uniform variables */
	private static Shader current;

	/** Id of the shader program */
	private transient final int program;
	/** Id of the vertex shader */
	private transient final int vertex;
	/** Id of the fragment shader */
	private transient final int fragment;

	/** Map that contains the location of uniform variables */
	private transient final HashMap<String, Integer> uniformLocations = new HashMap<>();
	/** Map that contains uniform variables to be loaded once the program starts */
	private transient final HashMap<Integer, Runnable> uniformCache = new HashMap<>();

	/**
	 * Constructs a shader object.
	 *
	 * @param vertexShader Already processed vertex shader code
	 * @param fragmentShader Already processed fragment shader code
	 * @throws ShaderCompilationException if there is a compilation error in the given shader code
	 */
	public Shader(String vertexShader, String fragmentShader) throws ShaderCompilationException {
		this.vertex = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(this.vertex, vertexShader);
		GL20.glCompileShader(this.vertex);
		if(GL20.glGetShaderi(this.vertex, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(this.vertex));
			GL20.glDeleteShader(this.vertex);
			throw new ShaderCompilationException("Could not compile vertex shader");
		}
		this.fragment = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		GL20.glShaderSource(this.fragment, fragmentShader);
		GL20.glCompileShader(this.fragment);
		if(GL20.glGetShaderi(this.fragment, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.out.println(GL20.glGetShaderInfoLog(this.fragment));
			GL20.glDeleteShader(this.fragment);
			throw new ShaderCompilationException("Could not compile fragment shader");
		}
		this.program = GL20.glCreateProgram();
		GL20.glAttachShader(this.program, this.vertex);
		GL20.glAttachShader(this.program, this.fragment);
		GL20.glLinkProgram(this.program);
		GL20.glValidateProgram(this.program);
		SHADERS.add(this);
	}

	/**
	 * Starts this shader program.
	 * Everything rendered after starting the shader will use this shader program.
	 */
	public void start() {
		GL20.glUseProgram(this.program);
		this.uniformCache.values().forEach(Runnable::run);
		this.uniformCache.clear();
		current = this;
	}

	/**
	 * Gets the location of a uniform variable and caches it for future access.
	 *
	 * @see GL20#glGetUniformLocation(int, CharSequence)
	 *
	 * @param variable Name of the uniform variable
	 * @return An integer representing the location of the uniform variable, used to load values
	 */
	private int getUniformLocation(String variable) {
		if(this.uniformLocations.containsKey(variable))
			return this.uniformLocations.get(variable);
		int location = GL20.glGetUniformLocation(this.program, variable);
		this.uniformLocations.put(variable, location);
		return location;
	}

	/**
	 * Sets the value of a uniform variables.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param consumer Function that loads the uniform variable given the location
	 */
	private void setUniform(String variable, IntConsumer consumer) {
		int location = this.getUniformLocation(variable);
		if(current == this) {
			consumer.accept(location);
		} else {
			this.uniformCache.put(location, () -> consumer.accept(location));
		}
	}

	/**
	 * Sets the value of a {@code float} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, float value) {
		this.setUniform(variable, location -> GL20.glUniform1f(location, value));
	}

	/**
	 * Sets the value of a {@code float} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, float value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of an {@code int} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, int value) {
		this.setUniform(variable, location -> GL20.glUniform1i(location, value));
	}

	/**
	 * Sets the value of an {@code int} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, int value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Float2 value) {
		this.setUniform(variable, value.x(), value.y());
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, Float2 value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 */
	public void setUniform(String variable, float x, float y) {
		this.setUniform(variable, location -> GL20.glUniform2f(location, x, y));
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 */
	public static void setUniformStatic(String variable, float x, float y) {
		SHADERS.forEach(shader -> shader.setUniform(variable, x, y));
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Int2 value) {
		this.setUniform(variable, value.x(), value.y());
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, Int2 value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 */
	public void setUniform(String variable, int x, int y) {
		this.setUniform(variable, location -> GL20.glUniform2i(location, x, y));
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 */
	public static void setUniformStatic(String variable, int x, int y) {
		SHADERS.forEach(shader -> shader.setUniform(variable, x, y));
	}

	/**
	 * Sets the value of a {@code vec3} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Float3 value) {
		this.setUniform(variable, value.x(), value.y(), value.z());
	}

	/**
	 * Sets the value of a {@code vec3} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, Float3 value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 */
	public void setUniform(String variable, float x, float y, float z) {
		this.setUniform(variable, location -> GL20.glUniform3f(location, x, y, z));
	}

	/**
	 * Sets the value of a {@code vec3} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 */
	public static void setUniformStatic(String variable, float x, float y, float z) {
		SHADERS.forEach(shader -> shader.setUniform(variable, x, y, z));
	}

	/**
	 * Sets the value of a {@code vec3i} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Int3 value) {
		this.setUniform(variable, value.x(), value.y(), value.z());
	}

	/**
	 * Sets the value of a {@code vec3i} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, Int3 value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec3i} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 */
	public void setUniform(String variable, int x, int y, int z) {
		this.setUniform(variable, location -> GL20.glUniform3i(location, x, y, z));
	}

	/**
	 * Sets the value of a {@code vec3i} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 */
	public static void setUniformStatic(String variable, int x, int y, int z) {
		SHADERS.forEach(shader -> shader.setUniform(variable, x, y, z));
	}

	/**
	 * Sets the value of a {@code vec4} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Float4 value) {
		this.setUniform(variable, value.x(), value.y(), value.z(), value.w());
	}

	/**
	 * Sets the value of a {@code vec4} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, Float4 value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec4} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 * @param w W component of the vector
	 */
	public void setUniform(String variable, float x, float y, float z, float w) {
		this.setUniform(variable, location -> GL20.glUniform4f(location, x, y, z, w));
	}

	/**
	 * Sets the value of a {@code vec4} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 * @param w W component of the vector
	 */
	public static void setUniformStatic(String variable, float x, float y, float z, float w) {
		SHADERS.forEach(shader -> shader.setUniform(variable, x, y, z, w));
	}

	/**
	 * Sets the value of a {@code vec4i} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Int4 value) {
		this.setUniform(variable, value.x(), value.y(), value.z(), value.w());
	}

	/**
	 * Sets the value of a {@code vec4i} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public static void setUniformStatic(String variable, Int4 value) {
		SHADERS.forEach(shader -> shader.setUniform(variable, value));
	}

	/**
	 * Sets the value of a {@code vec4i} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 * @param w W component of the vector
	 */
	public void setUniform(String variable, int x, int y, int z, int w) {
		this.setUniform(variable, location -> GL20.glUniform4i(location, x, y, z, w));
	}

	/**
	 * Sets the value of a {@code vec4i} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 * @param z Z component of the vector
	 * @param w W component of the vector
	 */
	public static void setUniformStatic(String variable, int x, int y, int z, int w) {
		SHADERS.forEach(shader -> shader.setUniform(variable, x, y, z, w));
	}

	/**
	 * Sets the value of a {@code mat3} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param matrix The matrix to load
	 */
	public void setUniform(String variable, Mat3f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(9);
		buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02());
		buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12());
		buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22());
		this.setUniform(variable, location -> GL20.glUniformMatrix3fv(location, true, buffer.flip()));
	}

	/**
	 * Sets the value of a {@code mat3} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param matrix The matrix to load
	 */
	public static void setUniformStatic(String variable, Mat3f matrix) {
		SHADERS.forEach(shader -> shader.setUniform(variable, matrix));
	}

	/**
	 * Sets the value of a {@code mat4} uniform variable.
	 * The value is loaded immediately if the shader is running, otherwise it is cached.
	 *
	 * @param variable Name of the uniform variable
	 * @param matrix The matrix to load
	 */
	public void setUniform(String variable, Mat4f matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
		buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
		buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
		buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
		this.setUniform(variable, location -> GL20.glUniformMatrix4fv(location, true, buffer.flip()));
	}

	/**
	 * Sets the value of a {@code mat4} uniform variable in all the loaded shaders.
	 *
	 * @param variable Name of the uniform variable
	 * @param matrix The matrix to load
	 */
	public static void setUniformStatic(String variable, Mat4f matrix) {
		SHADERS.forEach(shader -> shader.setUniform(variable, matrix));
	}

	@Override
	protected void delete() {
		GL20.glDetachShader(this.program, this.vertex);
		GL20.glDetachShader(this.program, this.fragment);
		GL20.glDeleteShader(this.vertex);
		GL20.glDeleteShader(this.fragment);
		GL20.glDeleteProgram(this.program);
	}

	/**
	 * Implementation of a {@link ResourceLoader} to load {@code .glsl} files.
	 * The loader is added to the {@link Resources} class in a static initializer.
	 * {@code Shader.SHADER_LOADER.load(String)} should generally not be called directly,
	 * shaders are supposed to be loaded with {@link Shader#getOrLoad(String)}.
	 */
	public static final ResourceLoader<Shader> SHADER_LOADER = path -> {
		try {
			String shaderCode = FileUtils.readResourceAsString(path);
			Matcher vertexRegex = Pattern.compile("((#define\\s+VERTEX)(.|\\s)+?(#undef\\s+VERTEX|\\z))").matcher(shaderCode);
			Matcher fragmentRegex = Pattern.compile("((#define\\s+FRAGMENT)(.|\\s)+?(#undef\\s+FRAGMENT|\\z))").matcher(shaderCode);
			if(vertexRegex.find() && fragmentRegex.find()) {
				String vertexCode = MAIN_VERTEX.replace("void vertex_shader();", vertexRegex.group(1));
				String fragmentCode = MAIN_FRAGMENT.replace("vec4 fragment_shader();", fragmentRegex.group(1));
				// TODO: Get version from settings
				vertexCode = vertexCode.replaceAll("#version \\d+", "#version 450");
				fragmentCode = fragmentCode.replaceAll("#version \\d+", "#version 450");
				return new Shader(vertexCode, fragmentCode);
			}
			throw new RuntimeException("Incorrect format in shader " + path);
		} catch (ShaderCompilationException e) {
			throw new RuntimeException("Could not compile shader " + path, e);
		}
	};
}
