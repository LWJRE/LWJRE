package io.github.view.core;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class TestComponent extends Component {

	private int vao, vbo;

	public TestComponent(Entity entity) {
		super(entity);
	}

	@Override
	public void prepareRendering() {
		super.prepareRendering();
		float[] vertices = new float[] {-0.5f,-0.5f,0.0f, -0.5f,0.5f,0.0f, 0.5f,-0.5f,0.0f, 0.5f,0.5f,0.0f};
		this.vao = GL30.glGenVertexArrays();
		this.vbo = GL15.glGenBuffers();
		GL30.glBindVertexArray(this.vao);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(vertices.length).put(vertices).flip(), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
	}

	@Override
	public void render() {
		super.render();
		GL30.glBindVertexArray(this.vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}

	@Override
	public void cleanUpRendering() {
		super.cleanUpRendering();
		GL30.glDeleteVertexArrays(this.vao);
		GL15.glDeleteBuffers(this.vbo);
	}
}
