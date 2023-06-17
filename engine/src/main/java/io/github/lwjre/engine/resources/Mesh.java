package io.github.lwjre.engine.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;

public class Mesh extends GLResource {

	/** Which mesh is currently bound */
	private static Mesh bound;

	/** Vertex array object*/
	private final int vertexArray;
	private final Iterable<Integer> vertexBuffers;
	private final Iterable<Integer> attributes;
	private final Runnable drawFunction;

	private Mesh(int vao, Iterable<Integer> vbo, Iterable<Integer> attributes, Runnable drawFunction) {
		this.vertexArray = vao;
		this.vertexBuffers = vbo;
		this.attributes = attributes;
		this.drawFunction = drawFunction;
	}

	public void bind() {
		if(bound != this) {
			GL30.glBindVertexArray(this.vertexArray);
			bound = this;
		}
	}

	public void draw() {
		this.bind();
		this.attributes.forEach(GL20::glEnableVertexAttribArray);
		this.drawFunction.run();
	}

	@Override
	protected void delete() {
		GL30.glDeleteVertexArrays(this.vertexArray);
		this.vertexBuffers.forEach(GL15::glDeleteBuffers);
	}

	public static class Builder {

		private float[] vertices;
		private int coordinateSize;
		private int[] indices;
		private float[] uvs;
		private float[] normals;

		public Builder vertices3D(float[] vertices) {
			this.vertices = vertices;
			this.coordinateSize = 3;
			return this;
		}

		public Builder vertices2D(float[] vertices) {
			this.vertices = vertices;
			this.coordinateSize = 2;
			return this;
		}

		public Builder indices(int[] indices) {
			this.indices = indices;
			return this;
		}

		public Builder uvs(float[] uvs) {
			this.uvs = uvs;
			return this;
		}

		public Builder normals(float[] normals) {
			this.normals = normals;
			return this;
		}

		public Mesh create() {
			int vao = GL30.glGenVertexArrays();
			GL30.glBindVertexArray(vao);
			ArrayList<Integer> vbo = new ArrayList<>();
			ArrayList<Integer> attributes = new ArrayList<>();
			Runnable drawFunction = () -> {};
			// TODO: Triangle strips?
			if(this.vertices != null && this.vertices.length > 0) {
				vbo.add(storeData(this.vertices, this.coordinateSize, 0));
				attributes.add(0);
				int verticesCount = this.vertices.length;
				drawFunction = () -> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verticesCount);
			}
			if(this.indices != null && this.indices.length > 0) {
				vbo.add(storeData(this.indices));
				int elementCount = this.indices.length;
				drawFunction = () -> GL11.glDrawElements(GL11.GL_TRIANGLES, elementCount, GL11.GL_UNSIGNED_INT, 0);
			}
			if(this.uvs != null && this.uvs.length > 0) {
				vbo.add(storeData(this.uvs, 2, 1));
				attributes.add(1);
			}
			if(this.normals != null && this.normals.length > 0) {
				vbo.add(storeData(this.normals, 3, 2));
				attributes.add(2);
			}
			return new Mesh(vao, vbo, attributes, drawFunction);
		}

		private static int storeData(float[] data, int size, int attribute) {
			int vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data.length).put(data).flip(), GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(attribute, size, GL11.GL_FLOAT, false, 0, 0);
			return vbo;
		}

		private static int storeData(int[] indices) {
			int vbo = GL15.glGenBuffers();
			GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL15.GL_STATIC_DRAW);
			return vbo;
		}
	}
}
