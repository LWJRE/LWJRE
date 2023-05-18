package gamma.engine.rendering;

import gamma.engine.resources.FileUtils;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public final class DebugRenderer {

	private static final HashMap<Mesh, HashSet<Consumer<Mesh>>> RENDER_BATCH = new HashMap<>();

	public static final Shader SHADER;

	static {
		try {
			SHADER = new Shader(
					FileUtils.readResourceAsString("gamma/engine/shaders/debug_vertex.glsl"),
					FileUtils.readResourceAsString("gamma/engine/shaders/debug_fragment.glsl")
			);
		} catch (ShaderCompilationException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public static void addToBatch(Mesh mesh, Consumer<Mesh> renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).add(renderer);
		} else {
			HashSet<Consumer<Mesh>> batch = new HashSet<>();
			batch.add(renderer);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	public static void removeFromBatch(Mesh mesh, Consumer<Mesh> renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).remove(renderer);
		}
	}

	public static void render() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		SHADER.start();
		RENDER_BATCH.forEach((mesh, batch) -> {
			mesh.bind();
			batch.forEach(renderer -> renderer.accept(mesh));
		});
	}

	public static void clear() {
		RENDER_BATCH.clear();
	}
}
