package gamma.engine.core.resources;

import de.javagl.obj.*;
import gamma.engine.core.utils.FileUtils;
import vecmatlib.color.Color;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

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

	private final Map<Mesh, Material> modelData;

	public Model(Map<Mesh, Material> modelData) {
		this.modelData = modelData;
	}

	/**
	 * Draws all the meshes that make up this model.
	 */
	public void draw() {
		this.modelData.keySet().forEach(Mesh::drawElements);
	}

	public void draw(BiConsumer<Mesh, Material> biConsumer) {
		this.modelData.forEach(biConsumer);
	}

	public static final ResourceLoader<Model> MODEL_LOADER = path -> {
		Obj obj = FileUtils.readResource(path, ObjReader::read);
		Map<String, Mtl> mtls = obj.getMtlFileNames().stream()
				.map(mtlFile -> FileUtils.readResource(path.substring(0, path.lastIndexOf('/') + 1) + mtlFile, MtlReader::read))
				.flatMap(Collection::stream)
				.collect(Collectors.toMap(Mtl::getName, mtl -> mtl));
		Map<Mesh, Material> model = ObjSplitting.splitByMaterialGroups(obj).entrySet().stream().collect(Collectors.toMap(entry -> {
			Mesh mesh = new Mesh();
			mesh.setVertices3D(ObjData.getVerticesArray(entry.getValue()));
			mesh.setIndices(ObjData.getFaceVertexIndicesArray(entry.getValue()));
			mesh.setTextures(ObjData.getTexCoordsArray(entry.getValue(), 2));
			mesh.setNormals(ObjData.getNormalsArray(entry.getValue()));
			return mesh;
		}, entry -> {
			Mtl mtl = mtls.get(entry.getKey());
			FloatTuple ka = mtl.getKa();
			FloatTuple kd = mtl.getKd();
			FloatTuple ks = mtl.getKs();
			return new Material(new Color(ka.getX(), ka.getY(), ka.getZ()), new Color(kd.getX(), kd.getY(), kd.getZ()), new Color(ks.getX(), ks.getY(), ks.getZ()), 0.0f);
		}));
		return new Model(model);
	};
}
