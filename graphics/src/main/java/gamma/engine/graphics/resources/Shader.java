package gamma.engine.graphics.resources;

import gamma.engine.core.utils.Resources;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vecmatlib.*;
import vecmatlib.matrix.Mat3f;
import vecmatlib.matrix.Mat4f;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.function.IntConsumer;

/**
 * Class that represents a shader resource.
 *
 * @author Nico
 */
public final class Shader extends DeletableResource {

	private static Shader current;

	/** Id of the shader program */
	private transient final int program;
	/* Ids of shaders */
	private transient final ArrayList<Integer> shaders;

	/** Map that contains the location of uniform variables */
	private transient final HashMap<String, Integer> uniformLocations = new HashMap<>();
	/** Map that contains uniform variables to be loaded once the program starts */
	private transient final HashMap<Integer, Runnable> uniformCache = new HashMap<>();

	/**
	 * Creates a shader program. Called in {@link Builder#createOrGet()}.
	 *
	 * @param shaders List of shaders
	 */
	private Shader(ArrayList<Integer> shaders) {
		this.program = GL20.glCreateProgram();
		shaders.forEach(shader -> GL20.glAttachShader(this.program, shader));
		GL20.glLinkProgram(this.program);
		GL20.glValidateProgram(this.program);
		this.shaders = shaders;
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
	 * Gets the location of a uniform variable. See {@link GL20#glGetUniformLocation(int, CharSequence)}.
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
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, float value) {
		this.setUniform(variable, location -> GL20.glUniform1f(location, value));
	}

	/**
	 * Sets the value of an {@code int} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, int value) {
		this.setUniform(variable, location -> GL20.glUniform1i(location, value));
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Float2 value) {
		this.setUniform(variable, value.x(), value.y());
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 */
	public void setUniform(String variable, float x, float y) {
		this.setUniform(variable, location -> GL20.glUniform2f(location, x, y));
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Int2 value) {
		this.setUniform(variable, value.x(), value.y());
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param x X component of the vector
	 * @param y Y component of the vector
	 */
	public void setUniform(String variable, int x, int y) {
		this.setUniform(variable, location -> GL20.glUniform2i(location, x, y));
	}

	/**
	 * Sets the value of a {@code vec3} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Float3 value) {
		this.setUniform(variable, value.x(), value.y(), value.z());
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
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
	 * Sets the value of a {@code vec3i} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Int3 value) {
		this.setUniform(variable, value.x(), value.y(), value.z());
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable.
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
	 * Sets the value of a {@code vec4} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Float4 value) {
		this.setUniform(variable, value.x(), value.y(), value.z(), value.w());
	}

	/**
	 * Sets the value of a {@code vec2} uniform variable.
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
	 * Sets the value of a {@code vec4i} uniform variable.
	 *
	 * @param variable Name of the uniform variable
	 * @param value The value to load
	 */
	public void setUniform(String variable, Int4 value) {
		this.setUniform(variable, value.x(), value.y(), value.z(), value.w());
	}

	/**
	 * Sets the value of a {@code vec2i} uniform variable.
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
	 * Sets the value of a {@code mat3} uniform variable.
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
	 * Sets the value of a {@code mat4} uniform variable.
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

	@Override
	protected void delete() {
		this.shaders.forEach(shader -> {
			GL20.glDetachShader(this.program, shader);
			GL20.glDeleteShader(shader);
		});
		GL20.glDeleteProgram(this.program);
	}

	/** Map of already loaded shaders */
	private static final HashMap<Builder, Shader> SHADERS = new HashMap<>();

	/**
	 * Builder class to create shaders.
	 *
	 * @author Nico
	 */
	public static final class Builder {

		/** Map of lists of shader files divided by type */
		private transient final HashMap<Integer, ArrayList<String>> shaders = new HashMap<>();

		/**
		 * Adds a shader file.
		 *
		 * @param type Type of the shader to add
		 * @param file Path to the shader file
		 */
		private void addShader(int type, String file) {
			if(!this.shaders.containsKey(type))
				this.shaders.put(type, new ArrayList<>());
			this.shaders.get(type).add(file);
		}

		/**
		 * Adds a vertex shader file.
		 *
		 * @param file Path to the vertex shader file
		 * @return {@code this}
		 */
		public Builder vertex(String file) {
			this.addShader(GL20.GL_VERTEX_SHADER, file);
			return this;
		}

		/**
		 * Adds a fragment shader file.
		 *
		 * @param file Path to the fragment shader file
		 * @return {@code this}
		 */
		public Builder fragment(String file) {
			this.addShader(GL20.GL_FRAGMENT_SHADER, file);
			return this;
		}

		/**
		 * Creates or get the shader program.
		 * If a program with the same shaders was already created, this method will return the same instance.
		 * If the program does not exist yet, a new instance is created.
		 *
		 * @return A shader program with the requested shaders.
		 */
		public Shader createOrGet() {
			if(SHADERS.containsKey(this))
				return SHADERS.get(this);
			ArrayList<Integer> result = new ArrayList<>();
			this.shaders.forEach((type, list) -> list.forEach(file -> {
				int shader = GL20.glCreateShader(type);
				String code = Resources.readAsString(file);
				GL20.glShaderSource(shader, code);
				GL20.glCompileShader(shader);
				if(GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
					System.err.println(GL20.glGetShaderInfoLog(shader));
					GL20.glDeleteShader(shader);
				} else {
					result.add(shader);
				}
			}));
			Shader shader = new Shader(result);
			SHADERS.put(this, shader);
			return shader;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Builder builder && builder.shaders.equals(this.shaders);
		}

		@Override
		public int hashCode() {
			return Objects.hash(shaders);
		}
	}

	public static Shader deserialize(Map<Object, Object> map) {
		Builder builder = new Builder();
		Optional.ofNullable((List<?>) map.get("vertex")).ifPresent(list -> list.forEach(obj -> {
			if(obj instanceof String str)
				builder.vertex(str);
		}));
		Optional.ofNullable((List<?>) map.get("fragment")).ifPresent(list -> list.forEach(obj -> {
			if(obj instanceof String str)
				builder.fragment(str);
		}));
		return builder.createOrGet();
	}

	// TODO: Find a way to serialize the shader
}
