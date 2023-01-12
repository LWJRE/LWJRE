package gamma.engine.core.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;

import java.util.ArrayList;
import java.util.HashSet;

public class Mesh3D extends GLResource {

	private static Mesh3D bound;

	private final int vertexArray;
	private final ArrayList<Integer> vertexBuffers = new ArrayList<>();
	private final HashSet<Integer> attributes = new HashSet<>();

	private Runnable drawMethod;

	private Mesh3D() {
		this.vertexArray = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vertexArray);
	}

	// TODO: Better binding and unbinding

	public void bind() {
		GL30.glBindVertexArray(this.vertexArray);
		this.attributes.forEach(GL20::glEnableVertexAttribArray);
		bound = this;
	}

	public void unbind() {
		this.attributes.forEach(GL20::glDisableVertexAttribArray);
		GL30.glBindVertexArray(0);
		bound = null;
	}

	public void draw(Runnable onDraw) {
		// TODO: Check if currently bound
		if(this.drawMethod != null) {
			onDraw.run();
			this.drawMethod.run();
		}
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

	public static final class Builder {

		private final ArrayList<Float> vertices = new ArrayList<>();
		private final ArrayList<Integer> indices = new ArrayList<>();
		private final ArrayList<Float> textureCoordinates = new ArrayList<>();
		private final ArrayList<Float> normals = new ArrayList<>();

		public Builder addVertex(float x, float y, float z) {
			this.vertices.add(x);
			this.vertices.add(y);
			this.vertices.add(z);
			return this;
		}

		public Builder addVertices(float... vertices) {
			for(int i = 0; i < vertices.length; i += 3) {
				this.addVertex(vertices[i], vertices[i + 1], vertices[i + 2]);
			}
			return this;
		}

		public Builder addVertex(Vec3f vertex) {
			return this.addVertex(vertex.x(), vertex.y(), vertex.z());
		}

		public Builder addVertices(Vec3f... vertices) {
			for(Vec3f vertex : vertices) {
				this.addVertex(vertex);
			}
			return this;
		}

		public Builder addVertices(Iterable<Vec3f> vertices) {
			vertices.forEach(this::addVertex);
			return this;
		}

		public Builder addIndex(int index) {
			this.indices.add(index);
			return this;
		}

		public Builder addIndices(int... indices) {
			for(int index : indices) {
				this.indices.add(index);
			}
			return this;
		}

		public Builder addIndices(Iterable<Integer> indices) {
			indices.forEach(this.indices::add);
			return this;
		}

		public Builder addTextureCoordinate(float x, float y) {
			this.textureCoordinates.add(x);
			this.textureCoordinates.add(y);
			return this;
		}

		public Builder addTextureCoordinates(float... textureCoordinates) {
			for(int i = 0; i < textureCoordinates.length; i += 2) {
				this.addTextureCoordinate(textureCoordinates[i], textureCoordinates[i + 1]);
			}
			return this;
		}

		public Builder addTextureCoordinate(Vec2f textureCoordinate) {
			return this.addTextureCoordinate(textureCoordinate.x(), textureCoordinate.y());
		}

		public Builder addTextureCoordinates(Vec2f... textureCoordinates) {
			for(Vec2f textureCoordinate : textureCoordinates) {
				this.addTextureCoordinate(textureCoordinate);
			}
			return this;
		}

		public Builder addTextureCoordinates(Iterable<Vec2f> textureCoordinates) {
			textureCoordinates.forEach(this::addTextureCoordinate);
			return this;
		}

		public Builder addNormal(float x, float y, float z) {
			this.normals.add(x);
			this.normals.add(y);
			this.normals.add(z);
			return this;
		}

		public Builder addNormals(float... normals) {
			for(int i = 0; i < normals.length; i += 3) {
				this.addNormal(normals[i], normals[i + 1], normals[i + 2]);
			}
			return this;
		}

		public Builder addNormal(Vec3f normal) {
			return this.addNormal(normal.x(), normal.y(), normal.z());
		}

		public Builder addNormals(Vec3f... normals) {
			for(Vec3f normal : normals) {
				this.addNormal(normal);
			}
			return this;
		}

		public Builder addNormals(Iterable<Vec3f> normals) {
			normals.forEach(this::addNormal);
			return this;
		}

		public Mesh3D create() {
			Mesh3D mesh = new Mesh3D();
			if(!this.vertices.isEmpty()) {
				mesh.storeData(unboxFloat(this.vertices), 3, 0);
				mesh.drawMethod = () -> GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, this.vertices.size() / 3);
			}
			if(!this.indices.isEmpty()) {
				mesh.storeData(unboxInt(this.indices));
				mesh.drawMethod = () -> GL11.glDrawElements(GL11.GL_TRIANGLES, this.indices.size(), GL11.GL_UNSIGNED_INT, 0);
			}
			if(!this.textureCoordinates.isEmpty())
				mesh.storeData(unboxFloat(this.textureCoordinates), 2, 1);
			if(!this.normals.isEmpty())
				mesh.storeData(unboxFloat(this.normals), 3, 2);
			return mesh;
		}

		private static float[] unboxFloat(ArrayList<Float> list) {
			float[] array = new float[list.size()];
			for(int i = 0; i < array.length; i++) {
				array[i] = list.get(i);
			}
			return array;
		}

		private static int[] unboxInt(ArrayList<Integer> list) {
			return list.stream().mapToInt(i -> i).toArray();
		}
	}
}
