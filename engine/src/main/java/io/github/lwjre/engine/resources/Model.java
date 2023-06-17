package io.github.lwjre.engine.resources;

import io.github.lwjre.engine.utils.YamlSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a 3D model. Contains a map of {@link Mesh}es and {@link Material}s.
 *
 * @author Nico
 */
public record Model(Map<Mesh, Material> modelData) {

	static {
		YamlSerializer.scalarRepresent(Model.class, "!getOrLoad", Resources::pathOf);
	}

	/**
	 * Loads the model at the given path in the classpath or returns the same instance if it was already loaded.
	 * Loaded models are stored in a hash map for immediate access after they were loaded for the first time.
	 * If the given path is null or an empty string, or if the resource at the given path is not a model, the model returned will be empty.
	 *
	 * @param path Path of the model to load
	 * @return The requested model
	 */
	public static Model getOrLoad(String path) {
		if(path != null && !(path.isEmpty() || path.isBlank())) {
			Object resource = Resources.getOrLoad(path);
			if(resource instanceof Model model) {
				return model;
			}
		}
		return new Model();
	}

	/**
	 * Creates an empty model.
	 */
	public Model() {
		this(new HashMap<>());
	}

	public Material getMaterial(Mesh mesh) {
		return this.modelData().get(mesh);
	}
}
