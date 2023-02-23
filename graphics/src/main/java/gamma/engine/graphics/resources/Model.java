package gamma.engine.graphics.resources;

import de.javagl.obj.*;
import gamma.engine.core.utils.Resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a 3D model.
 *
 * @author Nico
 */
public final class Model {

	/** All loaded models */
	private static final HashMap<String, Model> MODELS = new HashMap<>();

	/**
	 * Loads a model from the classpath or return the same instance if it was already loaded.
	 *
	 * @param path Path to the model file
	 * @return The requested model
	 */
	public static Model getOrLoad(String path) {
		if(MODELS.containsKey(path)) {
			return MODELS.get(path);
		} else {
			Model model = loadModel(path);
			MODELS.put(path, model);
			return model;
		}
	}

	/** List of meshes that make up this model */
	private final List<Mesh> meshes;

	/**
	 * Creates a model with the given meshes.
	 * This constructor is only used in {@link Model#loadModel(String)}.
	 *
	 * @param meshes List of meshes
	 */
	public Model(List<Mesh> meshes) {
		this.meshes = meshes;
	}

	/**
	 * Draws all the meshes that make up this model.
	 */
	public void draw() {
		this.meshes.forEach(Mesh::drawElements);
	}

	/**
	 * Loads a new model.
	 *
	 * @param path Path to the model file
	 * @return A newly loaded model
	 */
	private static Model loadModel(String path) {
		// TODO: Load .mtl files
		Map<String, Obj> modelData = ObjSplitting.splitByMaterialGroups(Resources.readAs(path, ObjReader::read));
		List<Mesh> model = modelData.values().stream()
				.map(ObjUtils::convertToRenderable)
				.map(obj -> {
					Mesh mesh = new Mesh();
					mesh.setVertices3D(ObjData.getVerticesArray(obj));
					mesh.setIndices(ObjData.getFaceVertexIndicesArray(obj));
					mesh.setTextures(ObjData.getTexCoordsArray(obj, 2));
					mesh.setNormals(ObjData.getNormalsArray(obj));
					return mesh;
				}).toList();
		return new Model(model);
	}
}
