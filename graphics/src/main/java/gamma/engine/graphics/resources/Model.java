package gamma.engine.graphics.resources;

import de.javagl.obj.*;
import gamma.engine.core.utils.EditorRepresent;
import gamma.engine.core.utils.Resources;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that represents a 3D model.
 *
 * @author Nico
 */
public final class Model implements EditorRepresent {

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
			System.out.println("Model " + path + " was already loaded");
			return MODELS.get(path);
		} else {
			Model model = loadModel(path);
			MODELS.put(path, model);
			return model;
		}
	}

	/** List of meshes that make up this model */
	private final List<Mesh> meshes;
	public final String path;

	private Model(List<Mesh> meshes, String path) {
		this.meshes = meshes;
		this.path = path;
	}

	/**
	 * Creates a model with the given meshes.
	 * This constructor is only used in {@link Model#loadModel(String)}.
	 *
	 * @param meshes List of meshes
	 */
	public Model(List<Mesh> meshes) {
		this(meshes, "");
	}

	/**
	 * Draws all the meshes that make up this model.
	 */
	public void draw() {
		this.meshes.forEach(Mesh::drawElements);
	}

	@Override
	public JComponent guiRepresent(Field field, Object owner) {
		JTextField textField = new JTextField();
		try {
			field.setAccessible(true);
			textField.setText(((Model) field.get(owner)).path);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		textField.setMaximumSize(new Dimension(textField.getPreferredSize().width, 20));
		textField.addActionListener(actionEvent -> {
			try {
				field.set(owner, Model.getOrLoad(textField.getText()));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return textField;
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
		return new Model(model, path);
	}
}
