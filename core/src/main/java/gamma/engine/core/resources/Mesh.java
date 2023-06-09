package gamma.engine.core.resources;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.IntBuffer;
import java.util.HashMap;

/**
 * Class representing a mesh resource.
 *
 * @author Nico
 */
public class Mesh extends GLResource {

	/** Which mesh is currently bound */
	private static Mesh bound;

	/** Vertex array object*/
	private transient final int vertexArray;
	/** HashMap with attributes as keys and vertex buffer objects as values */
	private transient final HashMap<Integer, Integer> vertexBuffers = new HashMap<>();

	/** Number of vertices used to render this mesh with {@link Mesh#drawTriangles()}. */
	private transient int vertexCount = 0;
	/** Number of elements used to render this mesh with {@link Mesh#drawElements()}. */
	private transient int elementCount = 0;

	/**
	 * Creates a mesh.
	 */
	public Mesh() {
		this.vertexArray = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(this.vertexArray);
	}

	/**
	 * Binds this mesh if it was not already bound.
	 * Meshes are bound when other methods in the mesh class are called.
	 * Meshes need to be bound before they can be used.
	 */
	public void bind() {
		if(bound != this) {
			GL30.glBindVertexArray(this.vertexArray);
			bound = this;
		}
	}

	public void drawTriangles() {
		this.drawTriangles(this.vertexCount);
	}

	public void drawTriangles(int count) {
		this.bind();
		this.vertexBuffers.keySet().forEach(attribute -> {
			if(attribute != -1)
				GL20.glEnableVertexAttribArray(attribute);
		});
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, count);
	}

	public void drawElements() {
		this.drawElements(this.elementCount);
	}

	public void drawElements(int count) {
		this.bind();
		this.vertexBuffers.keySet().forEach(attribute -> {
			if(attribute != -1)
				GL20.glEnableVertexAttribArray(attribute);
		});
		GL11.glDrawElements(GL11.GL_TRIANGLES, count, GL11.GL_UNSIGNED_INT, 0);
	}

	/**
	 * Stores the given float array in a buffer object as 3D vertices.
	 *
	 * @param vertices Array of floats containing 3D vertices
	 */
	public void setVertices3D(float[] vertices) {
		this.vertexCount = vertices.length / 3;
		this.bind();
		this.storeData(vertices, 0, 3);
	}

	/**
	 * Stores the given float array in a buffer object as 2D vertices.
	 *
	 * @param vertices Array of floats containing 2D vertices
	 */
	public void setVertices2D(float[] vertices) {
		this.vertexCount = vertices.length / 2;
		this.bind();
		this.storeData(vertices, 0, 2);
	}

	/**
	 * Stores an index buffer in a buffer object.
	 *
	 * @param indices Int array with the indices
	 */
	public void setIndices(int[] indices) {
		this.elementCount = indices.length;
		this.bind();
		this.storeData(indices);
	}

	public void setIndices(IntBuffer indices) {
		this.elementCount = indices.capacity();
		this.bind();
		this.storeData(indices);
	}

	/**
	 * Stores texture coordinates (UVs) in a buffer object.
	 *
	 * @param textures Array of texture coordinates
	 */
	public void setTextures(float[] textures) {
		this.bind();
		this.storeData(textures, 1, 2);
	}

	/**
	 * Stores an array of 3D normals in a buffer object.
	 *
	 * @param normals Array of normals
	 */
	public void setNormals(float[] normals) {
		this.bind();
		this.storeData(normals, 2, 3);
	}

	/**
	 * Stores data in an attribute list.
	 *
	 * @param data The data to store
	 * @param attribute Index of the attribute list
	 * @param size 2 for 2D coordinates, 3 for 3D coordinates
	 */
	private void storeData(float[] data, int attribute, int size) {
		int vbo = this.vertexBuffers.containsKey(attribute) ? this.vertexBuffers.get(attribute) : GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, BufferUtils.createFloatBuffer(data.length).put(data).flip(), GL15.GL_DYNAMIC_DRAW);
		GL20.glVertexAttribPointer(attribute, size, GL11.GL_FLOAT, false, 0, 0);
		this.vertexBuffers.put(attribute, vbo);
	}

	/**
	 * Store data in an attribute list.
	 *
	 * @param indices Array of indices
	 */
	private void storeData(int[] indices) {
		int vbo = this.vertexBuffers.containsKey(-1) ? this.vertexBuffers.get(-1) : GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, BufferUtils.createIntBuffer(indices.length).put(indices).flip(), GL15.GL_DYNAMIC_DRAW);
		this.vertexBuffers.put(-1, vbo);
	}

	private void storeData(IntBuffer indices) {
		int vbo = this.vertexBuffers.containsKey(-1) ? this.vertexBuffers.get(-1) : GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_DYNAMIC_DRAW);
		this.vertexBuffers.put(-1, vbo);
	}

	@Override
	protected void delete() {
		GL30.glDeleteVertexArrays(this.vertexArray);
		this.vertexBuffers.values().forEach(GL15::glDeleteBuffers);
	}
}
