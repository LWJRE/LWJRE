package gamma.engine.graphics;

import gamma.engine.graphics.resources.Mesh3D;
import gamma.engine.graphics.resources.Shader;
import gamma.engine.graphics.node.PointLight3D;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Static class used to render 3D meshes in batches instead of directly.
 *
 * @author Nico
 */
public final class RenderingSystem3D {

	/** Batch of meshes with their render function */
	private static final HashMap<Mesh3D, ArrayList<Runnable>> BATCH = new HashMap<>();
	/** Batch of lights */
	public static final ArrayList<PointLight3D> LIGHTS = new ArrayList<>();

	/**
	 * Adds a 3D mesh to the render batch.
	 * Meshes in the render batch will be rendered at the end of the frame.
	 *
	 * @param mesh The mesh to add
	 * @param renderer Action to run when this mesh is rendered
	 */
	public static void addToBatch(Mesh3D mesh, Runnable renderer) {
		if(!BATCH.containsKey(mesh)) {
			BATCH.put(mesh, new ArrayList<>());
		}
		BATCH.get(mesh).add(renderer);
	}

	/**
	 * Adds a light to the light batch.
	 *
	 * @param light The light to add
	 */
	public static void addLight(PointLight3D light) {
		LIGHTS.add(light);
	}

	/**
	 * Carries out the rendering process and clears the rendering batch.
	 * Only called once per frame.
	 */
	public static void renderingProcess() {
		Graphics.depthTest(true);
		Graphics.backFaceCulling(true);
		Shader.loadStaticUniform("lights_count", LIGHTS.size());
		for(int i = 0; i < LIGHTS.size(); i++) {
			Shader.loadStaticUniform("point_lights[" + i + "].position", LIGHTS.get(i).globalPosition());
			Shader.loadStaticUniform("point_lights[" + i + "].attenuation", 1.0f, 0.25f, 0.0f);
			Shader.loadStaticUniform("point_lights[" + i + "].ambient", LIGHTS.get(i).getColor());
			Shader.loadStaticUniform("point_lights[" + i + "].diffuse", LIGHTS.get(i).getColor());
			Shader.loadStaticUniform("point_lights[" + i + "].specular", LIGHTS.get(i).getColor());
		}
		BATCH.keySet().forEach(mesh -> {
			mesh.bind();
			BATCH.get(mesh).forEach(mesh::draw);
			mesh.unbind();
		});
		BATCH.clear();
	}

	/**
	 * Removes a light from the light batch.
	 *
	 * @param light The light to remove
	 */
	public static void removeLight(PointLight3D light) {
		LIGHTS.remove(light);
	}
}
