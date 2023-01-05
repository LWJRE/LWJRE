package engine.core.resources;

import engine.core.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import vecmatlib.*;
import vecmatlib.matrix.Mat4f;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.function.Consumer;

public final class Shader extends GLResource {

	private final int program;
	private final int[] shaders;

	private final HashMap<String, Integer> uniformVariables = new HashMap<>();

	private Shader(ArrayList<Integer> shaders) {
		this.program = GL20.glCreateProgram();
		this.shaders = shaders.stream().mapToInt(shader -> {
			GL20.glAttachShader(this.program, shader);
			return shader;
		}).toArray();
		GL20.glLinkProgram(this.program);
		GL20.glValidateProgram(this.program);
	}

	public void runProgram(Consumer<RunningShader> shader) {
		GL20.glUseProgram(this.program);
		shader.accept(new RunningShader());
	}

	@Override
	public void delete() {
		for(int shader : this.shaders) {
			GL20.glDetachShader(this.program, shader);
			GL20.glDeleteShader(shader);
		}
		GL20.glDeleteShader(this.program);
	}

	public class RunningShader {

		private RunningShader() {}

		private int getUniformLocation(String variable) {
			if(Shader.this.uniformVariables.containsKey(variable)) {
				return Shader.this.uniformVariables.get(variable);
			} else {
				int location = GL20.glGetUniformLocation(Shader.this.program, variable);
				Shader.this.uniformVariables.put(variable, location);
				return location;
			}
		}

		public void loadUniform(String name, float value) {
			GL20.glUniform1f(this.getUniformLocation(name), value);
		}

		public void loadUniform(String name, int value) {
			GL20.glUniform1i(this.getUniformLocation(name), value);
		}

		public void loadUniform(String name, float x, float y) {
			GL20.glUniform2f(this.getUniformLocation(name), x, y);
		}

		public void loadUniform(String name, int x, int y) {
			GL20.glUniform2i(this.getUniformLocation(name), x, y);
		}

		public void loadUniform(String name, Float2 vec2) {
			this.loadUniform(name, vec2.x(), vec2.y());
		}

		public void loadUniform(String name, Int2 vec2) {
			this.loadUniform(name, vec2.x(), vec2.y());
		}

		public void loadUniform(String name, float x, float y, float z) {
			GL20.glUniform3f(this.getUniformLocation(name), x, y, z);
		}

		public void loadUniform(String name, int x, int y, int z) {
			GL20.glUniform3i(this.getUniformLocation(name), x, y, z);
		}

		public void loadUniform(String name, Float3 vec3) {
			this.loadUniform(name, vec3.x(), vec3.y(), vec3.z());
		}

		public void loadUniform(String name, Int3 vec3) {
			this.loadUniform(name, vec3.x(), vec3.y(), vec3.z());
		}

		public void loadUniform(String name, float x, float y, float z, float w) {
			GL20.glUniform4f(this.getUniformLocation(name), x, y, z, w);
		}

		public void loadUniform(String name, int x, int y, int z, int w) {
			GL20.glUniform4i(this.getUniformLocation(name), x, y, z, w);
		}

		public void loadUniform(String name, Float4 vec4) {
			this.loadUniform(name, vec4.x(), vec4.y(), vec4.z(), vec4.w());
		}

		public void loadUniform(String name, Int4 vec4) {
			this.loadUniform(name, vec4.x(), vec4.y(), vec4.z(), vec4.w());
		}

		public void loadUniform(String name, Mat4f matrix) {
			FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
			buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
			buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
			buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
			buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
			GL20.glUniformMatrix4fv(this.getUniformLocation(name), true, buffer.flip());
		}
	}

	public static void loadStaticUniform(String name, float value) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, value)));
	}

	public static void loadStaticUniform(String name, int value) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, value)));
	}

	public static void loadStaticUniform(String name, float x, float y) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, x, y)));
	}

	public static void loadStaticUniform(String name, int x, int y) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, x, y)));
	}

	public static void loadStaticUniform(String name, Float2 vec2) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, vec2)));
	}

	public static void loadStaticUniform(String name, Int2 vec2) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, vec2)));
	}

	public static void loadStaticUniform(String name, float x, float y, float z) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, x, y, z)));
	}

	public static void loadStaticUniform(String name, int x, int y, int z) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, x, y, z)));
	}

	public static void loadStaticUniform(String name, Float3 vec3) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, vec3)));
	}

	public static void loadStaticUniform(String name, Int3 vec3) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, vec3)));
	}

	public static void loadStaticUniform(String name, float x, float y, float z, float w) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, x, y, z, w)));
	}

	public static void loadStaticUniform(String name, int x, int y, int z, int w) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, x, y, z, w)));
	}

	public static void loadStaticUniform(String name, Float4 vec4) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, vec4)));
	}

	public static void loadStaticUniform(String name, Int4 vec4) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, vec4)));
	}

	public static void loadStaticUniform(String name, Mat4f matrix) {
		PROGRAMS.values().forEach(shader -> shader.runProgram(runningShader -> runningShader.loadUniform(name, matrix)));
	}

	public static Builder main() {
		return new Builder("/shaders/main_vertex.glsl", "/shaders/main_fragment.glsl");
	}

	private static final HashMap<String, String> SHADER_CODE = new HashMap<>();
	private static final HashMap<String, Integer> SHADERS = new HashMap<>();
	private static final HashMap<Builder, Shader> PROGRAMS = new HashMap<>();

	public static class Builder {

		private final String mainVertex;
		private final String mainFragment;
		private final HashSet<String> vertex = new HashSet<>();
		private final HashSet<String> fragment = new HashSet<>();

		private Builder(String mainVertex, String mainFragment) {
			this.mainVertex = mainVertex;
			this.mainFragment = mainFragment;
		}

		public Builder vertexShader(String file) {
			this.vertex.add(file);
			return this;
		}

		public Builder fragmentShader(String file) {
			this.fragment.add(file);
			return this;
		}

		public Shader createOrLoad() {
			if(PROGRAMS.containsKey(this)) {
				return PROGRAMS.get(this);
			} else {
				int mainVertex = createOrGetShader(this.mainVertex, GL20.GL_VERTEX_SHADER, !this.vertex.isEmpty());
				int mainFragment = createOrGetShader(this.mainFragment, GL20.GL_FRAGMENT_SHADER, !this.vertex.isEmpty());
				ArrayList<Integer> shaders = new ArrayList<>();
				shaders.add(mainVertex);
				shaders.add(mainFragment);
				this.vertex.stream().map(file -> createOrGetShader(file, GL20.GL_VERTEX_SHADER, false)).forEach(shaders::add);
				this.fragment.stream().map(file -> createOrGetShader(file, GL20.GL_FRAGMENT_SHADER, false)).forEach(shaders::add);
				Shader shader = new Shader(shaders);
				PROGRAMS.put(this, shader);
				return shader;
			}
		}

		// TODO: Better error handling

		private static int createOrGetShader(String file, int type, boolean editCode) {
			String shaderCode = getOrReadShaderCode(file);
			if(editCode) {
				shaderCode = shaderCode.replaceFirst("#ifdef VERTEX", "#define VERTEX\n#ifdef VERTEX");
				shaderCode = shaderCode.replaceFirst("#ifdef FRAGMENT", "#define FRAGMENT\n#ifdef FRAGMENT");
			}
			if(SHADERS.containsKey(shaderCode)) {
				return SHADERS.get(shaderCode);
			}
			int shader = GL20.glCreateShader(type);
			GL20.glShaderSource(shader, shaderCode);
			GL20.glCompileShader(shader);
			if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				System.err.println("Could not compile shader");
				System.out.println(GL20.glGetShaderInfoLog(shader));
				GL20.glDeleteShader(shader);
				throw new RuntimeException("Shader compilation exception");
			}
			SHADERS.put(shaderCode, shader);
			return shader;
		}

		private static String getOrReadShaderCode(String file) {
			if(SHADER_CODE.containsKey(file)) {
				return SHADER_CODE.get(file);
			} else {
				String shaderCode = FileUtils.readString(file, exception -> {
					throw new RuntimeException("Error loading shader code from file " + file, exception);
				});
				SHADER_CODE.put(file, shaderCode);
				return shaderCode;
			}
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof Builder that &&
					this.mainVertex.equals(that.mainVertex) && this.mainFragment.equals(that.mainFragment) &&
					this.vertex.equals(that.vertex) && this.fragment.equals(that.fragment);
		}

		@Override
		public int hashCode() {
			return Objects.hash(mainVertex, mainFragment, vertex, fragment);
		}
	}
}