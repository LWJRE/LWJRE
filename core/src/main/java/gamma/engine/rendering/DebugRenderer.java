package gamma.engine.rendering;

import gamma.engine.EditorListener;
import gamma.engine.resources.FileUtils;
import gamma.engine.scene.Component;
import org.lwjgl.opengl.GL11;
import vecmatlib.color.Color4f;
import vecmatlib.matrix.Mat4f;

import java.util.HashMap;

public final class DebugRenderer implements EditorListener {
	
	private static final Mesh CUBE = new Mesh();
	private static final Shader SHADER;

	private static final HashMap<Component, Mat4f> BATCH = new HashMap<>();

	public static void addToBatch(Component key, Mat4f transform) {
		BATCH.put(key, transform);
	}

	@Override
	public void onEditorProcess() {
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glDisable(GL11.GL_CULL_FACE);
		SHADER.start();
		BATCH.values().forEach(transform -> {
			SHADER.setUniform("transformation_matrix", transform);
			SHADER.setUniform("color", Color4f.Green());
			CUBE.drawElements();
		});
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		BATCH.clear();
	}
	
	static {
		System.out.println("Init debug renderer");
		CUBE.setVertices3D(new float[] {
				-0.5f, 0.5f, -0.5f,
				-0.5f, -0.5f, -0.5f,
				0.5f, -0.5f, -0.5f,
				0.5f, 0.5f, -0.5f,

				-0.5f, 0.5f, 0.5f,
				-0.5f, -0.5f, 0.5f,
				0.5f, -0.5f, 0.5f,
				0.5f, 0.5f, 0.5f,

				0.5f, 0.5f, -0.5f,
				0.5f, -0.5f, -0.5f,
				0.5f, -0.5f, 0.5f,
				0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, -0.5f,
				-0.5f, -0.5f, -0.5f,
				-0.5f, -0.5f, 0.5f,
				-0.5f, 0.5f, 0.5f,

				-0.5f, 0.5f, 0.5f,
				-0.5f, 0.5f, -0.5f,
				0.5f, 0.5f, -0.5f,
				0.5f, 0.5f, 0.5f,

				-0.5f, -0.5f, 0.5f,
				-0.5f, -0.5f, -0.5f,
				0.5f, -0.5f, -0.5f,
				0.5f, -0.5f, 0.5f
		});
		CUBE.setIndices(new int[] {
				0,1,3,
				3,1,2,
				4,5,7,
				7,5,6,
				8,9,11,
				11,9,10,
				12,13,15,
				15,13,14,
				16,17,19,
				19,17,18,
				20,21,23,
				23,21,22
		});
		try {
			SHADER = new Shader(
					FileUtils.readResourceAsString("gamma/engine/shaders/debug_vertex.glsl"),
					FileUtils.readResourceAsString("gamma/engine/shaders/debug_fragment.glsl")
			);
		} catch (ShaderCompilationException e) {
			throw new RuntimeException(e);
		}
	}
}
