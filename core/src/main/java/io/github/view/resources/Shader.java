package io.github.view.resources;

import io.github.view.math.Float2;
import io.github.view.math.Float3;
import io.github.view.math.Float4;
import io.github.view.math.Matrix4;
import io.github.view.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public final class Shader extends Resource {

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

	public void start() {
		GL20.glUseProgram(this.program);
	}

	private int getUniformLocation(String variable) {
		if(this.uniformVariables.containsKey(variable)) {
			return this.uniformVariables.get(variable);
		} else {
			int location = GL20.glGetUniformLocation(this.program, variable);
			this.uniformVariables.put(variable, location);
			return location;
		}
	}

	public void loadUniform(String name, float value) {
		GL20.glUniform1f(this.getUniformLocation(name), value);
	}

	public void loadUniform(String name, Float2 vec2) {
		this.loadUniform(name, vec2.x(), vec2.y());
	}

	public void loadUniform(String name, float x, float y) {
		GL20.glUniform2f(this.getUniformLocation(name), x, y);
	}

	public void loadUniform(String name, Float3 vec3) {
		this.loadUniform(name, vec3.x(), vec3.y(), vec3.z());
	}

	public void loadUniform(String name, float x, float y, float z) {
		GL20.glUniform3f(this.getUniformLocation(name), x, y, z);
	}

	public void loadUniform(String name, Float4 vec4) {
		this.loadUniform(name, vec4.x(), vec4.y(), vec4.z(), vec4.w());
	}

	public void loadUniform(String name, float x, float y, float z, float w) {
		GL20.glUniform4f(this.getUniformLocation(name), x, y, z, w);
	}

	public void loadUniform(String name, Matrix4 matrix) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrix.m00()); buffer.put(matrix.m01()); buffer.put(matrix.m02()); buffer.put(matrix.m03());
		buffer.put(matrix.m10()); buffer.put(matrix.m11()); buffer.put(matrix.m12()); buffer.put(matrix.m13());
		buffer.put(matrix.m20()); buffer.put(matrix.m21()); buffer.put(matrix.m22()); buffer.put(matrix.m23());
		buffer.put(matrix.m30()); buffer.put(matrix.m31()); buffer.put(matrix.m32()); buffer.put(matrix.m33());
		GL20.glUniformMatrix4fv(this.getUniformLocation(name), true, buffer.flip());
	}

	@Override
	public void delete() {
		for(int shader : this.shaders) {
			GL20.glDetachShader(this.program, shader);
			GL20.glDeleteShader(shader);
		}
		GL20.glDeleteShader(this.program);
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