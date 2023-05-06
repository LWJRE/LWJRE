package gamma.engine.rendering;

import gamma.engine.tree.PointLight3D;
import gamma.engine.tree.Renderer3D;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec2i;

import java.util.HashMap;
import java.util.HashSet;

public final class RenderingSystem {

	private static final HashMap<Mesh, HashSet<Renderer3D>> RENDER_BATCH = new HashMap<>();

	private static final HashSet<PointLight3D> LIGHTS = new HashSet<>();

	public static void addToBatch(Mesh mesh, Renderer3D renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).add(renderer);
		} else {
			HashSet<Renderer3D> batch = new HashSet<>();
			batch.add(renderer);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	public static void addToBatch(PointLight3D light) {
		LIGHTS.add(light);
	}

	public static void removeFromBatch(Mesh mesh, Renderer3D renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).remove(renderer);
		}
	}

	public static void removeFromBatch(Renderer3D renderer) {
		RENDER_BATCH.values().forEach(batch -> batch.remove(renderer));
	}

	public static void removeFromBatch(PointLight3D light) {
		LIGHTS.remove(light);
	}

	public static void init() {
		GL.createCapabilities();
	}

	public static void render() {
		// TODO: Give option for depth test and backface culling
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		// TODO: Give default clear color option
		GL11.glClearColor(0.0f, 0.5f, 1.0f, 1.0f);
		int i = 0;
		for(PointLight3D light : LIGHTS) {
			Shader.setUniformStatic("point_lights[" + i + "].position", light.globalPosition());
			Shader.setUniformStatic("point_lights[" + i + "].ambient", light.color);
			Shader.setUniformStatic("point_lights[" + i + "].diffuse", light.color);
			Shader.setUniformStatic("point_lights[" + i + "].specular", light.color);
			i++;
		}
		Shader.setUniformStatic("lights_count", i);
		RENDER_BATCH.forEach((mesh, batch) -> {
			mesh.bind();
			batch.forEach(renderer -> renderer.render(mesh));
		});
	}

	public static void resizeViewport(int width, int height) {
		// TODO: Give options for viewport scaling in ApplicationProperties
		Vec2i windowSize = new Vec2i(width, height);
		float aspectWidth = windowSize.x();
		float aspectHeight = aspectWidth / (16.0f / 9.0f);
		if(aspectHeight > windowSize.y()) {
			aspectHeight = windowSize.y();
			aspectWidth = aspectHeight * (16.0f / 9.0f);
		}
		Vec2f viewportSize = new Vec2f(aspectWidth, aspectHeight);
		float viewportX = (windowSize.x() / 2.0f) - (viewportSize.x() / 2.0f);
		float viewportY = (windowSize.y() / 2.0f) - (viewportSize.y() / 2.0f);
		GL11.glViewport((int) viewportX, (int) viewportY, (int) viewportSize.x(), (int) viewportSize.y());
	}

	public static void cleanUp() {
		DeletableResource.deleteAll();
	}
}