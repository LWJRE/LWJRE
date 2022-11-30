package io.github.view.resources;

import io.github.view.Application;
import io.github.view.math.Color;
import io.github.view.math.Matrix4;
import io.github.view.math.Vector3;
import io.github.view.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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
		if(!Application.isRenderingThread())
			throw new RuntimeException("Uniform variables can only be loaded from the rendering thread");
		if(this.uniformVariables.containsKey(variable)) {
			return this.uniformVariables.get(variable);
		} else {
			int location = GL20.glGetUniformLocation(this.program, variable);
			this.uniformVariables.put(variable, location);
			return location;
		}
	}

	public void loadUniform(String name, Vector3 vector) {
		GL20.glUniform3f(this.getUniformLocation(name), vector.x(), vector.y(), vector.z());
	}

	public void loadUniform(String name, Color color) {
		GL20.glUniform3f(this.getUniformLocation(name), color.r(), color.g(), color.b());
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
		if(!Application.isRenderingThread())
			throw new RuntimeException("Shaders can only be deleted from the rendering thread");
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

		// TODO: Replace with createOrLoad
		public Shader create() {
			if(!Application.isRenderingThread())
				throw new RuntimeException("Shaders can only be created from the rendering thread");
			int mainVertex = createShader(this.mainVertex, GL20.GL_VERTEX_SHADER, !this.vertex.isEmpty());
			int mainFragment = createShader(this.mainFragment, GL20.GL_FRAGMENT_SHADER, !this.vertex.isEmpty());
			ArrayList<Integer> shaders = new ArrayList<>();
			shaders.add(mainVertex);
			shaders.add(mainFragment);
			shaders.addAll(this.vertex.stream().map(file -> createShader(file, GL20.GL_VERTEX_SHADER, false)).toList());
			shaders.addAll(this.fragment.stream().map(file -> createShader(file, GL20.GL_FRAGMENT_SHADER, false)).toList());
			return new Shader(shaders);
		}

		private static int createShader(String file, int type, boolean editCode) {
			String shaderCode = getOrLoad(file);
			if(editCode) {
				shaderCode = shaderCode.replaceFirst("#ifdef VERTEX", "#define VERTEX\n#ifdef VERTEX");
				shaderCode = shaderCode.replaceFirst("#ifdef FRAGMENT", "#define FRAGMENT\n#ifdef FRAGMENT");
			}
			int shader = GL20.glCreateShader(type);
			GL20.glShaderSource(shader, shaderCode);
			GL20.glCompileShader(shader);
			if (GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				System.err.println("Could not compile shader");
				System.out.println(GL20.glGetShaderInfoLog(shader));
				GL20.glDeleteShader(shader);
				// TODO: Give better compilation feedback
				throw new RuntimeException("Shader compilation exception");
			}
			return shader;
		}

		private static String getOrLoad(String file) {
			if(SHADER_CODE.containsKey(file)) {
				return SHADER_CODE.get(file);
			}
			String shaderCode = FileUtils.readString(file);
			SHADER_CODE.put(file, shaderCode);
			return shaderCode;
		}
	}
}