package io.github.view.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

public class Mesh extends Resource {

	private final int vertexArray;
	private final ArrayList<Integer> vertexBuffers = new ArrayList<>();
	private final HashSet<Integer> attributes = new HashSet<>();
	private final DrawableMesh drawableMesh;

	private Mesh(float[] vertices, int size, DrawableMesh drawableMesh) {
		this.vertexArray = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vertexArray);
		this.storeData(vertices, size, 0);
		this.drawableMesh = drawableMesh;
	}

	private Mesh(float[] vertices, int size, int[] indices) {
		this(vertices, size, () -> GL11.glDrawElements(GL11.GL_TRIANGLES, indices.length, GL11.GL_UNSIGNED_INT, 0));
		this.storeData(indices);
	}

	private void bind() {
		GL30.glBindVertexArray(this.vertexArray);
		this.attributes.forEach(GL20::glEnableVertexAttribArray);
	}

	public void bind(Consumer<DrawableMesh> action) {
		this.bind();
		action.accept(this.drawableMesh);
		this.unbind();
	}

	private void unbind() {
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

	// TODO: Triangle strips mode

	public static Mesh createMesh2D(float[] vertices) {
		return new Mesh(vertices, 2, () -> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 2));
	}

	public static Mesh createMesh2D(float[] vertices, int[] indices) {
		return new Mesh(vertices, 2, indices);
	}

	public static Mesh createTexturedMesh2D(float[] vertices, float[] uvs) {
		Mesh mesh = createMesh2D(vertices);
		mesh.storeData(uvs, 2, 1);
		return mesh;
	}

	public static Mesh createTexturedMesh2D(float[] vertices, int[] indices, float[] uvs) {
		Mesh mesh = createMesh2D(vertices, indices);
		mesh.storeData(uvs, 2, 1);
		return mesh;
	}

	public static Mesh createMesh3D(float[] vertices) {
		return new Mesh(vertices, 3, () -> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertices.length / 3));
	}

	public static Mesh createMesh3D(float[] vertices, int[] indices) {
		return new Mesh(vertices, 3, indices);
	}

	public static Mesh createMesh3D(float[] vertices, float[] normals) {
		Mesh mesh = createMesh3D(vertices);
		mesh.storeData(normals, 3, 2);
		return mesh;
	}

	public static Mesh createMesh3D(float[] vertices, int[] indices, float[] normals) {
		Mesh mesh = createMesh3D(vertices, indices);
		mesh.storeData(normals, 3, 2);
		return mesh;
	}

	public static Mesh createTexturedMesh3D(float[] vertices, float[] uvs) {
		Mesh mesh = createMesh3D(vertices);
		mesh.storeData(uvs, 2, 1);
		return mesh;
	}

	public static Mesh createTexturedMesh3D(float[] vertices, int[] indices, float[] uvs) {
		Mesh mesh = createMesh3D(vertices, indices);
		mesh.storeData(uvs, 2, 1);
		return mesh;
	}

	public static Mesh createTexturedMesh3D(float[] vertices, float[] uvs, float[] normals) {
		Mesh mesh = createTexturedMesh3D(vertices, uvs);
		mesh.storeData(normals, 3, 2);
		return mesh;
	}

	public static Mesh createTexturedMesh3D(float[] vertices, int[] indices, float[] uvs, float[] normals) {
		Mesh mesh = createTexturedMesh3D(vertices, indices, uvs);
		mesh.storeData(normals, 3, 2);
		return mesh;
	}

	public interface DrawableMesh {

		void draw();
	}
}
