package io.github.view.resources;

import io.github.view.Application;
import io.github.view.utils.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.HashMap;

public final class Shader extends Resource {

	private static final HashMap<String, Shader> SHADERS = new HashMap<>();

	public static Shader getOrLoad(String file) {
		if(SHADERS.containsKey(file)) {
			return SHADERS.get(file);
		}
		Shader shader = loadShader(file);
		SHADERS.put(file, shader);
		return shader;
	}

	public static final int VERTEX = GL20.GL_VERTEX_SHADER;
	public static final int FRAGMENT = GL20.GL_FRAGMENT_SHADER;

	public final int id;

	public Shader(String shaderCode, int type) {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Shaders can only be created from the rendering thread");
		this.id = GL20.glCreateShader(type);
		GL20.glShaderSource(id, shaderCode);
		GL20.glCompileShader(id);
		if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Could not compile shader");
			System.out.println(GL20.glGetShaderInfoLog(id));
			GL20.glDeleteShader(id);
		}
	}

	@Override
	public void delete() {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Shaders can only be deleted from the rendering thread");
		GL20.glDeleteShader(this.id);
	}

	private static Shader loadShader(String file) {
		String shaderCode = FileUtils.readString(file);
		if(shaderCode.contains("#define VERTEX"))
			return new Shader(shaderCode, VERTEX);
		if(shaderCode.contains("#define FRAGMENT"))
			return new Shader(shaderCode, FRAGMENT);
		throw new RuntimeException("Shader is missing a '#define' to define the shader stage");
	}
}