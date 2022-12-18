package io.github.view.resources;

import io.github.view.math.Color;
import io.github.view.math.Vector2;
import io.github.view.math.Vector3;
import io.github.view.utils.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class Model {

	private static final HashMap<String, Model> MODELS = new HashMap<>();
	private static final HashMap<String, HashMap<String, Material>> MATERIAL_LIBRARIES = new HashMap<>();

	public static Model getOrLoad(String file) {
		if(MODELS.containsKey(file)) {
			return MODELS.get(file);
		} else {
			Model model = loadModel(file);
			MODELS.put(file, model);
			return model;
		}
	}

	public final ArrayList<Mesh3D> meshes = new ArrayList<>();
	public final ArrayList<Material> materials = new ArrayList<>();

	private static Model loadModel(String file) {
		Model model = new Model();
		ArrayList<Vector3> vertices = new ArrayList<>();
		ArrayList<Vector2> texture = new ArrayList<>();
		ArrayList<Vector3> normals = new ArrayList<>();
		HashMap<String, Material> materials = new HashMap<>();
		HashMap<Material, MeshData> meshes = new HashMap<>();
		Stack<MeshData> currentMeshes = new Stack<>();
		currentMeshes.push(new MeshData());
		FileUtils.streamLines(file, stringStream -> stringStream.map(string -> string.split(" ")).forEach(line -> {
			if(line[0].equals("mtllib")) {
				materials.putAll(getOrLoadMaterials(file.substring(0, file.lastIndexOf("/") + 1) + line[1]));
			} else if(line[0].equals("v")) {
				vertices.add(new Vector3(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])));
			} else if(line[0].equals("vt")) {
				texture.add(new Vector2(Float.parseFloat(line[1]), Float.parseFloat(line[2])));
			} else if(line[0].equals("vn")) {
				normals.add(new Vector3(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3])));
			} else if(line[0].equals("usemtl") && !materials.isEmpty()) {
				Material material = materials.get(line[1]);
				if(meshes.containsKey(material)) {
					currentMeshes.push(meshes.get(material));
				} else {
					MeshData meshData = new MeshData();
					meshData.material = material;
					meshes.put(material, meshData);
					currentMeshes.push(meshData);
				}
			} else if(line[0].equals("f") && !currentMeshes.isEmpty()) {
				MeshData currentMesh = currentMeshes.peek();
				if(!meshes.containsValue(currentMesh))
					meshes.put(currentMesh.material, currentMesh);
				for(int i = 1; i <= 3; i++) {
					String[] face = line[i].split("/");
					int index = Integer.parseInt(face[0]) - 1;
					currentMesh.indices.add(index);
					// TODO: Check these two
					if(!face[1].isEmpty())
						Collections.swap(texture, index, Integer.parseInt(face[1]) - 1);
					if(!face[2].isEmpty())
						Collections.swap(normals, index, Integer.parseInt(face[2]) - 1);
				}
			}
		}), exception -> {
			exception.printStackTrace();
		});
		for(MeshData mesh : meshes.values()) {
			// TODO: All meshes contain all vertices even if they don't need them
			Mesh3D.Builder builder = Mesh3D.create(vec3toArray(vertices), toArray(mesh.indices));
			if(!texture.isEmpty()) builder.textureCoordinates(vec2toArray(texture));
			if(!normals.isEmpty()) builder.normals(vec3toArray(normals));
			model.meshes.add(builder.finish());
			model.materials.add(mesh.material);
		}
		return model;
	}

	private static HashMap<String, Material> getOrLoadMaterials(String file) {
		if(MATERIAL_LIBRARIES.containsKey(file)) {
			return MATERIAL_LIBRARIES.get(file);
		} else {
			Stack<Material> currentMaterials = new Stack<>();
			Stack<String> materialsNames = new Stack<>();
			FileUtils.streamLines(file, stringStream -> stringStream.map(string -> string.split(" ")).forEach(line -> {
				if(line[0].equals("newmtl")) {
					currentMaterials.push(new Material());
					materialsNames.push(line[1]);
				} else if(line[0].equals("Ka") && !currentMaterials.isEmpty()) {
					Color color = new Color(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
					currentMaterials.peek().setAmbient(color);
				} else if(line[0].equals("Kd") && !currentMaterials.isEmpty()) {
					Color color = new Color(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
					currentMaterials.peek().setDiffuse(color);
				} else if(line[0].equals("Ks") && !currentMaterials.isEmpty()) {
					Color color = new Color(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
					currentMaterials.peek().setSpecular(color);
				}
			}), exception -> {
				exception.printStackTrace();
			});
			HashMap<String, Material> materials = new HashMap<>();
			for(int i = 0; i < currentMaterials.size(); i++) {
				materials.put(materialsNames.get(i), currentMaterials.get(i));
			}
			MATERIAL_LIBRARIES.put(file, materials);
			return materials;
		}
	}

	private static class MeshData {

		private Material material;
		private final ArrayList<Integer> indices = new ArrayList<>();
	}

	private static float[] vec3toArray(ArrayList<Vector3> list) {
		float[] array = new float[list.size() * 3];
		for(int i = 0; i < list.size(); i++) {
			array[i * 3] = list.get(i).x();
			array[i * 3 + 1] = list.get(i).y();
			array[i * 3 + 2] = list.get(i).z();
		}
		return array;
	}

	private static float[] vec2toArray(ArrayList<Vector2> list) {
		float[] array = new float[list.size() * 2];
		for(int i = 0; i < list.size(); i++) {
			array[i * 2] = list.get(i).x();
			array[i * 2 + 1] = list.get(i).y();
		}
		return array;
	}

	private static int[] toArray(ArrayList<Integer> list) {
		return list.stream().mapToInt(i -> i).toArray();
	}
}
