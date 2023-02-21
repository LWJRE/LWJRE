package gamma.engine.graphics.resources;

import gamma.engine.core.utils.Resources;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.*;

/**
 * Class that represents a shader resource.
 *
 * @author Nico
 */
public final class Shader extends DeletableResource {

	/** Id of the shader program */
	private transient final int program;
	/* Ids of shaders */
	private transient final ArrayList<Integer> shaders;

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
//		this.uniformVariables.values().forEach(Runnable::run);
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
