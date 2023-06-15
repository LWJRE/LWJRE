package io.github.lwjre.resources;

import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.lwjre.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;

public class ModelLoader implements ResourceLoader {
	
	private static final List<String> MATERIAL_KEYS = List.of(
			Assimp.AI_MATKEY_NAME,
			Assimp.AI_MATKEY_TWOSIDED,
			Assimp.AI_MATKEY_SHADING_MODEL,
			Assimp.AI_MATKEY_ENABLE_WIREFRAME,
			Assimp.AI_MATKEY_BLEND_FUNC,
			Assimp.AI_MATKEY_OPACITY,
			Assimp.AI_MATKEY_TRANSPARENCYFACTOR,
			Assimp.AI_MATKEY_BUMPSCALING,
			Assimp.AI_MATKEY_SHININESS,
			Assimp.AI_MATKEY_REFLECTIVITY,
			Assimp.AI_MATKEY_SHININESS_STRENGTH,
			Assimp.AI_MATKEY_REFRACTI,
			Assimp.AI_MATKEY_COLOR_DIFFUSE,
			Assimp.AI_MATKEY_COLOR_AMBIENT,
			Assimp.AI_MATKEY_COLOR_SPECULAR,
			Assimp.AI_MATKEY_COLOR_EMISSIVE,
			Assimp.AI_MATKEY_COLOR_TRANSPARENT,
			Assimp.AI_MATKEY_COLOR_REFLECTIVE,
			Assimp.AI_MATKEY_USE_COLOR_MAP,
			Assimp.AI_MATKEY_BASE_COLOR,
			Assimp.AI_MATKEY_USE_METALLIC_MAP,
			Assimp.AI_MATKEY_METALLIC_FACTOR,
			Assimp.AI_MATKEY_USE_ROUGHNESS_MAP,
			Assimp.AI_MATKEY_ROUGHNESS_FACTOR,
			Assimp.AI_MATKEY_ANISOTROPY_FACTOR,
			Assimp.AI_MATKEY_SPECULAR_FACTOR,
			Assimp.AI_MATKEY_GLOSSINESS_FACTOR,
			Assimp.AI_MATKEY_SHEEN_COLOR_FACTOR,
			Assimp.AI_MATKEY_SHEEN_ROUGHNESS_FACTOR,
			Assimp.AI_MATKEY_CLEARCOAT_FACTOR,
			Assimp.AI_MATKEY_CLEARCOAT_ROUGHNESS_FACTOR,
			Assimp.AI_MATKEY_TRANSMISSION_FACTOR,
			Assimp.AI_MATKEY_VOLUME_THICKNESS_FACTOR,
			Assimp.AI_MATKEY_VOLUME_ATTENUATION_DISTANCE,
			Assimp.AI_MATKEY_VOLUME_ATTENUATION_COLOR,
			Assimp.AI_MATKEY_USE_EMISSIVE_MAP,
			Assimp.AI_MATKEY_EMISSIVE_INTENSITY,
			Assimp.AI_MATKEY_USE_AO_MAP
	);

	@Override
	public Object load(String path) {
		AIScene aiScene = loadScene(path);
		Material[] materials = readMaterials(aiScene);
		HashMap<Mesh, Material> modelData = readMeshes(aiScene, materials);
		Assimp.aiReleaseImport(aiScene);
		return new Model(modelData);
	}

	protected AIScene loadScene(String path) {
		AIScene aiScene = FileUtils.readResource(path, inputStream -> {
			byte[] bytes = inputStream.readAllBytes();
			ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
			return Assimp.aiImportFileFromMemory(buffer, 0, ""); // TODO: Add flags in a .properties file
		});
		if(aiScene == null) {
			throw new RuntimeException("Failed to load model " + path + ": " + Assimp.aiGetErrorString());
		}
		return aiScene;
	}

	protected Material[] readMaterials(AIScene aiScene) {
		Material[] materials = new Material[aiScene.mNumMaterials()];
		PointerBuffer materialsBuffer = aiScene.mMaterials();
		if(materialsBuffer != null) {
			for(int i = 0; i < materials.length; i++) {
				AIMaterial aiMaterial = AIMaterial.create(materialsBuffer.get(i));
				materials[i] = new Material();
				for(String materialKey : MATERIAL_KEYS) {
					if(materialKey.startsWith("?mat")) {
						AIString string = AIString.create();
						if(Assimp.aiGetMaterialString(aiMaterial, materialKey, Assimp.aiTextureType_NONE, 0, string) == 0) {
							materials[i].setParam(materialKey.substring(materialKey.indexOf('.') + 1), string.dataString());
						}
					} else if(materialKey.startsWith("$mat")) {
						float[] array = new float[1];
						if(Assimp.aiGetMaterialFloatArray(aiMaterial, materialKey, Assimp.aiTextureType_NONE, 0, array, new int[] {1}) == 0) {
							materials[i].setParam(materialKey.substring(materialKey.indexOf('.') + 1), array[0]);
						}
					} else if(materialKey.startsWith("$clr")) {
						AIColor4D color = AIColor4D.create();
						if(Assimp.aiGetMaterialColor(aiMaterial, materialKey, Assimp.aiTextureType_NONE, 0, color) == 0) {
							materials[i].setParam(materialKey.substring(materialKey.indexOf('.') + 1), new Color4f(color.r(), color.g(), color.b(), color.a()));
						}
					} else if(materialKey.startsWith("$tex")) {
						// TODO: Load materials with textures
					}
				}
			}
		}
		return materials;
	}

	protected HashMap<Mesh, Material> readMeshes(AIScene aiScene, Material[] materials) {
		HashMap<Mesh, Material> modelData = new HashMap<>();
		PointerBuffer meshesBuffer = aiScene.mMeshes();
		if(meshesBuffer != null) {
			for(int i = 0; i < aiScene.mNumMeshes(); i++) {
				AIMesh aiMesh = AIMesh.create(meshesBuffer.get(i));
				AIVector3D.Buffer verticesBuffer = aiMesh.mVertices();
				Mesh.Builder mesh = new Mesh.Builder();
				float[] vertices = new float[verticesBuffer.capacity() * 3];
				for(int v = 0; v < vertices.length / 3; v++) {
					AIVector3D vertex = verticesBuffer.get(v);
					vertices[v * 3] = vertex.x();
					vertices[v * 3 + 1] = vertex.y();
					vertices[v * 3 + 2] = vertex.z();
				}
				mesh.vertices3D(vertices);
				IntBuffer indicesBuffer = BufferUtils.createIntBuffer(aiMesh.mNumFaces() * 3);
				AIFace.Buffer facesBuffer = aiMesh.mFaces();
				for(int f = 0; f < aiMesh.mNumFaces(); f++) {
					AIFace face = facesBuffer.get(f);
					indicesBuffer.put(face.mIndices());
				}
				int[] indices = new int[indicesBuffer.flip().limit()];
				indicesBuffer.get(indices);
				mesh.indices(indices);
				// TODO: Load vertex textures
				AIVector3D.Buffer normalsBuffer = aiMesh.mNormals();
				if(normalsBuffer != null) {
					float[] normals = new float[normalsBuffer.capacity() * 3];
					for(int n = 0; n < normals.length / 3; n++) {
						AIVector3D normal = normalsBuffer.get(n);
						normals[n * 3] = normal.x();
						normals[n * 3 + 1] = normal.y();
						normals[n * 3 + 2] = normal.z();
					}
					mesh.normals(normals);
				}
				modelData.put(mesh.create(), materials[aiMesh.mMaterialIndex()]);
			}
		}
		return modelData;
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".dae"}; // TODO: Test with .fbx
	}
}
