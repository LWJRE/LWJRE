package io.github.view.resources;

import io.github.view.Application;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.HashMap;
import java.util.List;
import java.util.function.IntSupplier;

public class VertexObject extends Resource {

	private final int vertexArray;
	private final List<Integer> vertexBuffers;

	private VertexObject(int vertexArray, List<Integer> vertexBuffers) {
		this.vertexArray = vertexArray;
		this.vertexBuffers = vertexBuffers;
	}

	// TODO: Method for drawing

	public void bind() {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Vertex objects can only be bound from the rendering thread");
		GL30.glBindVertexArray(this.vertexArray);
	}

	@Override
	protected void delete() {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Vertex objects can only be deleted from the rendering thread");
		GL30.glDeleteVertexArrays(this.vertexArray);
		this.vertexBuffers.forEach(GL15::glDeleteBuffers);
	}

	public static class Builder {

		private final HashMap<Integer, IntSupplier> vertexBuffers = new HashMap<>();

		public Builder vertices2D(float[] vertices) {
			this.vertexBuffers.put(0, () -> storeData(vertices, 2, 0));
			return this;
		}

		public Builder vertices3D(float[] vertices) {
			this.vertexBuffers.put(0, () -> storeData(vertices, 3, 0));
			return this;
		}

		public Builder indices(int[] indices) {
			this.vertexBuffers.put(-1, () -> storeData(indices));
			return this;
		}

		public Builder uvs(float[] uvs) {
			this.vertexBuffers.put(1, () -> storeData(uvs, 2, 1));
			return this;
		}

		public Builder normals(float[] normals) {
			this.vertexBuffers.put(2, () -> storeData(normals, 3, 2));
			return this;
		}

		private static int storeData(float[] data, int size, int index) {
			int vertexBuffer = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data.length).put(data).flip(), GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
			return vertexBuffer;
		}

		private static int storeData(int[] indices) {
			int vertexBuffer = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexBuffer);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL15.GL_STATIC_DRAW);
			return vertexBuffer;
		}

		public VertexObject create() {
			if(!Application.isRenderingThread())
				throw new RuntimeException("Vertex objects can only be created from the rendering thread");
			int vertexArray = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vertexArray);
			List<Integer> vertexBuffers = this.vertexBuffers.values().stream().map(IntSupplier::getAsInt).toList();
			GL30.glBindVertexArray(0);
			return new VertexObject(vertexArray, vertexBuffers);
		}
	}
}
