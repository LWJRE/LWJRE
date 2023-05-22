package gamma.engine.rendering;

import gamma.engine.resources.FileUtils;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

/**
 * Debug renderer used to draw debug shapes.
 *
 * @author Nico
 */
public final class DebugRenderer {

	/** Batch of rendering functions to call on the next {@link DebugRenderer#render()} call */
	private static final HashMap<Mesh, HashSet<Consumer<Mesh>>> RENDER_BATCH = new HashMap<>();

	/** Debug shader used by the debug renderer */
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

	/**
	 * Adds a mesh to the render batch.
	 * The next {@link DebugRenderer#render()} call will call {@link Consumer#accept(Object)} with the given {@link Mesh}.
	 * Note that the {@code DebugRenderer} must be cleared every frame with {@link DebugRenderer#clear()}.
	 *
	 * @param mesh The debug shape to render
	 * @param renderer The rendering function
	 */
	public static void addToBatch(Mesh mesh, Consumer<Mesh> renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).add(renderer);
		} else {
			HashSet<Consumer<Mesh>> batch = new HashSet<>();
			batch.add(renderer);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	public static void render() {
		// TODO: Give rendering options
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glLineWidth(3.0f);
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
