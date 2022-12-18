package io.github.view.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashSet;

public class Mesh3D extends Resource {

	private final int vertexArray;
	private final Runnable drawMethod;
	private final ArrayList<Integer> vertexBuffers = new ArrayList<>();
	private final HashSet<Integer> attributes = new HashSet<>();

	private Mesh3D(Runnable drawMethod) {
		this.vertexArray = GL30.glGenVertexArrays();
		this.drawMethod = drawMethod;
		GL30.glBindVertexArray(this.vertexArray);
	}

	public void draw(Runnable onDraw) {
		GL30.glBindVertexArray(this.vertexArray);
		this.attributes.forEach(GL20::glEnableVertexAttribArray);
		onDraw.run();
		this.drawMethod.run();
		this.attributes.forEach(GL20::glDisableVertexAttribArray);
		GL30.glBindVertexArray(0);
	}

	@Override
	protected void delete() {
		GL30.glDeleteVertexArrays(this.vertexArray);
		this.vertexBuffers.forEach(GL15::glDeleteBuffers);
	}

	private void storeData(float[] data, int size, int index) {
		int vertexBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBuffer);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data.length).put(data).flip(), GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(index, size, GL11.GL_FLOAT, false, 0, 0);
		this.attributes.add(index);
		this.vertexBuffers.add(vertexBuffer);
	}

	private void storeData(int[] indices) {
		int vertexBuffer = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexBuffer);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL15.GL_STATIC_DRAW);
		this.vertexBuffers.add(vertexBuffer);
	}

	public static Builder create(float[] vertices) {
		return new Builder(vertices, null, () -> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 3));
	}

	public static Builder create(float[] vertices, int[] indices) {
		return new Builder(vertices, indices, () -> GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0));
	}

	public static final class Builder {

		private final float[] vertices;
		private final int[] indices;
		private final Runnable drawMethod;
		private float[] textureCoordinates;
		private float[] normals;

		public Builder(float[] vertices, int[] indices, Runnable drawMethod) {
			this.vertices = vertices;
			this.indices = indices;
			this.drawMethod = drawMethod;
		}

		public Builder textureCoordinates(float[] textureCoordinates) {
			this.textureCoordinates = textureCoordinates;
			return this;
		}

		public Builder normals(float[] normals) {
			this.normals = normals;
			return this;
		}

		public Mesh3D finish() {
			Mesh3D mesh = new Mesh3D(this.drawMethod);
			mesh.storeData(this.vertices, 3, 0);
			mesh.storeData(this.indices);
			if(this.textureCoordinates != null)
				mesh.storeData(this.textureCoordinates, 2, 1);
			if(this.normals != null)
				mesh.storeData(this.normals, 3, 1);
			return mesh;
		}
	}
}
