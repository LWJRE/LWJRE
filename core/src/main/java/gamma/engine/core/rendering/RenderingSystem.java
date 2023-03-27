package gamma.engine.core.rendering;

import gamma.engine.core.components.PointLight3D;
import gamma.engine.core.resources.Mesh;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.scene.Component;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;

public final class RenderingSystem {

	private static final HashMap<Mesh, HashMap<Component, Runnable>> RENDER_BATCH = new HashMap<>();

	private static final HashSet<PointLight3D> LIGHTS = new HashSet<>();

	public static void addToBatch(Component key, Mesh mesh, Runnable renderFunction) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).put(key, renderFunction);
		} else {
			HashMap<Component, Runnable> batch = new HashMap<>();
			batch.put(key, renderFunction);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	public static void addToBatch(PointLight3D light) {
		LIGHTS.add(light);
	}

	public static void removeFromBatch(Component key, Mesh mesh) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).remove(key);
		}
	}

	public static void removeFromBatch(PointLight3D light) {
		LIGHTS.remove(light);
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
			Shader.setUniformStatic("point_lights[" + i + "].position", light.position());
			Shader.setUniformStatic("point_lights[" + i + "].ambient", light.color);
			Shader.setUniformStatic("point_lights[" + i + "].diffuse", light.color);
			i++;
		}
		Shader.setUniformStatic("lights_count", i);
		RENDER_BATCH.forEach((mesh, batch) -> {
			mesh.bind();
			batch.values().forEach(Runnable::run);
		});
	}
}