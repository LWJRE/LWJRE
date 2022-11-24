package io.github.view.core;

import io.github.view.resources.Shader;
import io.github.view.resources.ShaderProgram;
import io.github.view.resources.Texture;
import io.github.view.resources.VertexObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class TestComponent extends Component3D {

	private VertexObject mesh;

	private ShaderProgram shader;
	private Texture texture;

	public TestComponent(Entity3D entity) {
		super(entity);
	}

	@Override
	public void prepareRendering() {
		super.prepareRendering();
		this.mesh = new VertexObject.Builder().vertices3D(new float[] {-0.5f,-0.5f,0.0f, -0.5f,0.5f,0.0f, 0.5f,-0.5f,0.0f, 0.5f,0.5f,0.0f}).create();
		Shader vertexShader = Shader.getOrLoad("/shaders/test_vertex.glsl");
		Shader fragmentShader = Shader.getOrLoad("/shaders/test_fragment.glsl");
		this.shader = new ShaderProgram(vertexShader, fragmentShader);
		this.texture = Texture.getOrLoad("/test.png");
		this.texture.bind();
		ShaderProgram.start(this.shader);
	}

	@Override
	public void render() {
		super.render();
		this.mesh.bind();
		GL20.glEnableVertexAttribArray(0);
		GL20.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL20.glDisableVertexAttribArray(0);
	}

	@Override
	public void cleanUpRendering() {
		super.cleanUpRendering();
		ShaderProgram.stop();
	}
}
