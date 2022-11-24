package io.github.view.resources;

import io.github.view.Application;
import io.github.view.math.Matrix4;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.HashMap;

public class ShaderProgram extends Resource {

	// TODO: getOrLoad for ShaderProgram (getOrCreate)

	public static void start(ShaderProgram program) {
		GL20.glUseProgram(program.id);
	}

	public static void stop() {
		GL20.glUseProgram(0);
	}

	public final int id;
	private final int[] shaders;

	private final HashMap<String, Integer> uniformVariables = new HashMap<>();

	public ShaderProgram(Shader... shaders) {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Shader programs can only be created from the rendering thread");
		this.id = GL20.glCreateProgram();
		this.shaders = new int[shaders.length];
		for(int i = 0; i < shaders.length; i++) {
			GL20.glAttachShader(this.id, shaders[i].id);
			this.shaders[i] = shaders[i].id;
		}
		GL20.glLinkProgram(this.id);
		GL20.glValidateProgram(this.id);
	}

	private int getUniformLocation(String variable) {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Uniform variables can only be loaded from the rendering thread");
		if(this.uniformVariables.containsKey(variable)) {
			return this.uniformVariables.get(variable);
		} else {
			int location = GL20.glGetUniformLocation(this.id, variable);
			this.uniformVariables.put(variable, location);
			return location;
		}
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
			throw new RuntimeException("Shader programs can only be deleted from the rendering thread");
		for(int shader : this.shaders) {
			GL20.glDetachShader(this.id, shader);
		}
		GL20.glDeleteProgram(this.id);
	}
}
