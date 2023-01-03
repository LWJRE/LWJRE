package engine.graphics;

import engine.graphics.resources.Mesh3D;
import engine.graphics.resources.Shader;
import engine.graphics.tree.PointLight3D;

import java.util.ArrayList;
import java.util.HashMap;

public final class RenderingSystem3D {

	private static final HashMap<Mesh3D, HashMap<Object, ArrayList<Runnable>>> BATCH = new HashMap<>();
	public static final ArrayList<PointLight3D> LIGHTS = new ArrayList<>();

	public static void addToBatch(Mesh3D mesh, Object key, Runnable renderer) {
		if(!BATCH.containsKey(mesh)) {
			BATCH.put(mesh, new HashMap<>());
		}
		if(!BATCH.get(mesh).containsKey(key)) {
			BATCH.get(mesh).put(key, new ArrayList<>());
		}
		BATCH.get(mesh).get(key).add(renderer);
	}

	public static void addLight(PointLight3D light) {
		LIGHTS.add(light);
	}

	public static void renderingProcess() {
		Shader.loadStaticUniform("lights_count", LIGHTS.size());
		for(int i = 0; i < LIGHTS.size(); i++) {
			Shader.loadStaticUniform("point_lights[" + i + "].position", LIGHTS.get(i).globalPosition());
			Shader.loadStaticUniform("point_lights[" + i + "].attenuation", 1.0f, 0.25f, 0.0f);
			Shader.loadStaticUniform("point_lights[" + i + "].ambient", LIGHTS.get(i).getColor());
			Shader.loadStaticUniform("point_lights[" + i + "].diffuse", LIGHTS.get(i).getColor());
			Shader.loadStaticUniform("point_lights[" + i + "].specular", LIGHTS.get(i).getColor());
		}
		BATCH.keySet().forEach(mesh -> BATCH.get(mesh).values().forEach(list -> list.forEach(mesh::draw)));
	}

	public static void removeFromBatch(Object key) {
		BATCH.values().forEach(map -> map.remove(key));
	}

	public static void removeLight(PointLight3D light) {
		LIGHTS.remove(light);
	}
}
