package io.github.view.resources;

import io.github.view.utils.FileUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public final class ModelLoader {

	private static final HashMap<String, Mesh> MODELS = new HashMap<>();

	public static Mesh getOrLoadObj(String file) {
		if(MODELS.containsKey(file)) {
			return MODELS.get(file);
		} else {
			Mesh model = loadObj(file);
			MODELS.put(file, model);
			return model;
		}
	}

	private static Mesh loadObj(String file) {
		ArrayList<Float> vertices = new ArrayList<>();
		ArrayList<Integer> indices = new ArrayList<>();
		ArrayList<Float> texture = new ArrayList<>();
		ArrayList<Float> normals = new ArrayList<>();
		FileUtils.streamLines(file, stream -> {
			stream.forEach(line -> {
				if (line.startsWith("v ")) {
					String[] data = line.split(" ");
					vertices.add(Float.parseFloat(data[1]));
					vertices.add(Float.parseFloat(data[2]));
					vertices.add(Float.parseFloat(data[3]));
				} else if (line.startsWith("vt ")) {
					String[] data = line.split(" ");
					texture.add(Float.parseFloat(data[1]));
					texture.add(Float.parseFloat(data[2]));
				} else if (line.startsWith("vn ")) {
					String[] data = line.split(" ");
					normals.add(Float.parseFloat(data[1]));
					normals.add(Float.parseFloat(data[2]));
					normals.add(Float.parseFloat(data[3]));
				} else if (line.startsWith("f ")) {
					String[] data = line.split(" ");
					for (int i = 1; i < data.length; i++) {
						String[] faceData = data[i].split("/");
						int index = Integer.parseInt(faceData[0]) - 1;
						if (!faceData[1].isEmpty()) {
							int textureIndex = Integer.parseInt(faceData[1]) - 1;
							Collections.swap(texture, index * 2, textureIndex * 2);
							Collections.swap(texture, index * 2 + 1, textureIndex * 2 + 1);
						}
						if (!faceData[2].isEmpty()) {
							int normalIndex = Integer.parseInt(faceData[2]) - 1;
							Collections.swap(normals, index * 3, normalIndex * 3);
							Collections.swap(normals, index * 3 + 1, normalIndex * 3 + 1);
							Collections.swap(normals, index * 3 + 2, normalIndex * 3 + 2);
						}
						indices.add(index);
					}
				}
			});
		});
		if(!texture.isEmpty()) {
			if(!normals.isEmpty()) {
				return Mesh.createTexturedMesh3D(listToArray(vertices), indices.stream().mapToInt(i -> i).toArray(), listToArray(texture), listToArray(normals));
			}
			return Mesh.createTexturedMesh3D(listToArray(vertices), indices.stream().mapToInt(i -> i).toArray(), listToArray(texture));
		}
		if(!normals.isEmpty()) {
			return Mesh.createMesh3D(listToArray(vertices), indices.stream().mapToInt(i -> i).toArray(), listToArray(normals));
		}
		return Mesh.createMesh3D(listToArray(vertices), indices.stream().mapToInt(i -> i).toArray());
	}

	private static float[] listToArray(ArrayList<Float> list) {
		float[] array = new float[list.size()];
		for(int i = 0; i < array.length; i++) {
			array[i] = list.get(i);
		}
		return array;
	}
}
