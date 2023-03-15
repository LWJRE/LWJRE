package gamma.engine.graphics.resources;

import de.javagl.obj.*;
import gamma.engine.core.resources.Resource;
import gamma.engine.core.resources.ResourceLoader;
import gamma.engine.core.resources.Resources;
import gamma.engine.core.utils.FileUtils;

import java.util.List;
import java.util.Map;

/**
 * Class that represents a 3D model.
 *
 * @author Nico
 */
public final class Model implements Resource {

	/**
	 * Loads a model from the classpath or return the same instance if it was already loaded.
	 *
	 * @param path Path to the model file
	 * @return The requested model
	 */
	public static Model getOrLoad(String path) {
		Resource resource = Resources.getOrLoad(path);
		if(resource instanceof Model model)
			return model;
		throw new RuntimeException("Resource " + path + " is not a model");
	}

	/** List of meshes that make up this model */
	private final List<Mesh> meshes;

	private Model(List<Mesh> meshes) {
		this.meshes = meshes;
	}

	/**
	 * Draws all the meshes that make up this model.
	 */
	public void draw() {
		this.meshes.forEach(Mesh::drawElements);
	}

	public static final ResourceLoader<Model> MODEL_LOADER = path -> {
		Map<String, Obj> modelData = ObjSplitting.splitByMaterialGroups(FileUtils.readResource(path, ObjReader::read));
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
	};
}
