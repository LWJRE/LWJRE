package engine.core.resources;

import engine.core.utils.Color;
import engine.core.utils.FileUtils;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec3f;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Model {

	private static final HashMap<String, Model> MODELS = new HashMap<>();

	public static Model getOrLoad(String file) {
		if(MODELS.containsKey(file)) {
			return MODELS.get(file);
		} else {
			Model model = loadObj(file);
			MODELS.put(file, model);
			return model;
		}
	}

	private final Map<Material, Mesh3D> meshes;

	private Model(Map<Material, Mesh3D> meshes) {
		this.meshes = meshes;
	}

	public void forEach(BiConsumer<Material, Mesh3D> consumer) {
		this.meshes.forEach(consumer);
	}

	private static Model loadObj(String file) {
		ArrayList<Vec3f> vertices = new ArrayList<>();
		ArrayList<Vec2f> textures = new ArrayList<>();
		ArrayList<Vec3f> normals = new ArrayList<>();
		HashMap<String, Material> materials = new HashMap<>();
		HashMap<Material, Mesh3D.Builder> model = new HashMap<>();
		Stack<Mesh3D.Builder> currentMesh = new Stack<>();
		TreeMap<Integer, Vec2f> actualTextures = new TreeMap<>();
		TreeMap<Integer, Vec3f> actualNormals = new TreeMap<>();
		FileUtils.streamLines(file, stringStream -> stringStream.map(string -> string.split(" ")).forEach(line -> {
			if(line[0].equals("mtllib")) {
				// Load mtl file when a line starts with 'mtllib'
				materials.putAll(getOrLoadMtl(file.substring(0, file.lastIndexOf("/") + 1) + line[1]));
			} else if(line[0].equals("v")) {
				// Add a vertex in the list of all vertices when a line starts with 'v'
				vertices.add(new Vec3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])));
			} else if(line[0].equals("vt")) {
				// Add a texture coordinate in the list of all texture coordinates when a line starts with 'vt'
				textures.add(new Vec2f(Float.parseFloat(line[1]), 1 - Float.parseFloat(line[2])));
			} else if(line[0].equals("vn")) {
				// Add a normal in the list of all normals when a line starts with 'vn'
				normals.add(new Vec3f(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])));
			} else if(line[0].equals("usemtl")) {
				// Change the current material when a line starts with 'usemtl'
				if (currentMesh.isEmpty() || !model.containsKey(materials.get(line[1]))) {
					// If no mesh is current, or if that material has not already been used,
					// create a new mesh, set it as current, and add it to the model
					Mesh3D.Builder mesh = new Mesh3D.Builder();
					currentMesh.push(mesh);
					model.put(materials.get(line[1]), mesh);
				} else {
					// If a mesh is current and the material has already been used, set that mesh as current
					Mesh3D.Builder mesh = model.get(materials.get(line[1]));
					currentMesh.push(mesh);
				}
			} else if(line[0].equals("f")) {
				// If no mesh is current, create a new one with all current vertices
				if (currentMesh.isEmpty()) {
					Mesh3D.Builder mesh = new Mesh3D.Builder();
					currentMesh.push(mesh);
					model.put(null, mesh);
				}
				// Add index to current mesh for all 3 vertices of the face
				for(int i = 1; i <= 3; i++) {
					String[] vertex = line[i].split("/");
					int vertexIndex = Integer.parseInt(vertex[0]) - 1;
					currentMesh.peek().addIndex(vertexIndex);
					if(!vertex[1].isEmpty()) {
						int textureIndex = Integer.parseInt(vertex[1]) - 1;
						actualTextures.put(vertexIndex, textures.get(textureIndex));
					}
					if(!vertex[2].isEmpty()) {
						int normalIndex = Integer.parseInt(vertex[2]) - 1;
						actualNormals.put(vertexIndex, normals.get(normalIndex));
					}
				}
			}
		}));
		return new Model(model.entrySet().stream().collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> entry.getValue()
						.addVertices(vertices)
						.addTextureCoordinates(actualTextures.values())
						.addNormals(actualNormals.values())
						.create()
		)));
	}

	private static final HashMap<String, HashMap<String, Material>> MATERIAL_LIBRARIES = new HashMap<>();

	public static HashMap<String, Material> getOrLoadMtl(String file) {
		if(MATERIAL_LIBRARIES.containsKey(file)) {
			return MATERIAL_LIBRARIES.get(file);
		} else {
			HashMap<String, Material> materialLibrary = loadMtl(file);
			MATERIAL_LIBRARIES.put(file, materialLibrary);
			return materialLibrary;
		}
	}

	private static HashMap<String, Material> loadMtl(String file) {
		HashMap<String, Material> materials = new HashMap<>();
		Stack<Material> current = new Stack<>();
		FileUtils.streamLines(file, stringStream -> stringStream.map(string -> string.split(" ")).forEach(line -> {
			if(line[0].equals("newmtl")) {
				Material material = new Material();
				materials.put(line[1], material);
				current.push(material);
			} else if(line[0].equals("Ka") && !current.isEmpty()) {
				Color color = new Color(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
				current.peek().setAmbient(color);
			} else if(line[0].equals("Kd") && !current.isEmpty()) {
				Color color = new Color(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
				current.peek().setDiffuse(color);
			} else if(line[0].equals("Ks") && !current.isEmpty()) {
				Color color = new Color(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
				current.peek().setSpecular(color);
			}
		}));
		return materials;
	}
}
