package gamma.engine.rendering;

import gamma.engine.resources.FileUtils;
import gamma.engine.resources.ResourceLoader;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import vecmatlib.color.Color4f;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implementation of a {@link ResourceLoader} to load model files using {@link Assimp}.
 *
 * @author Nico
 */
public final class ModelLoader implements ResourceLoader {

	@Override
	public Object load(String path) {
		AIScene aiScene = FileUtils.readResource(path, inputStream -> {
			byte[] bytes = inputStream.readAllBytes();
			ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
			return Assimp.aiImportFileFromMemory(buffer, 0, ""); // TODO: Add flags in a .properties file
		});
		if(aiScene == null)
			throw new RuntimeException("Failed to load model " + path + ": " + Assimp.aiGetErrorString());
		ArrayList<Material> materials = new ArrayList<>(aiScene.mNumMaterials());
		PointerBuffer materialsBuffer = aiScene.mMaterials();
		if(materialsBuffer != null) {
			for(int i = 0; i < aiScene.mNumMaterials(); i++) {
				AIMaterial aiMaterial = AIMaterial.create(materialsBuffer.get(i));
				AIColor4D ambient = AIColor4D.create();
				Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_AMBIENT, Assimp.aiTextureType_NONE, 0, ambient);
				AIColor4D diffuse = AIColor4D.create();
				Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_DIFFUSE, Assimp.aiTextureType_NONE, 0, diffuse);
				AIColor4D specular = AIColor4D.create();
				Assimp.aiGetMaterialColor(aiMaterial, Assimp.AI_MATKEY_COLOR_SPECULAR, Assimp.aiTextureType_NONE, 0, specular);
				materials.add(new Material(
						new Color4f(ambient.r(), ambient.g(), ambient.b(), ambient.a()),
						new Color4f(diffuse.r(), diffuse.g(), diffuse.b(), diffuse.a()),
						new Color4f(specular.r(), specular.g(), specular.b(), specular.a()),
						0.0f
				));
			}
		}
		HashMap<Mesh, Material> modelData = new HashMap<>();
		PointerBuffer meshesBuffer = aiScene.mMeshes();
		if(meshesBuffer != null) {
			for(int i = 0; i < aiScene.mNumMeshes(); i++) {
				AIMesh aiMesh = AIMesh.create(meshesBuffer.get(i));
				AIVector3D.Buffer verticesBuffer = aiMesh.mVertices();
				Mesh mesh = new Mesh();
				float[] vertices = new float[verticesBuffer.capacity() * 3];
				for(int v = 0; v < vertices.length / 3; v++) {
					AIVector3D vertex = verticesBuffer.get(v);
					vertices[v * 3] = vertex.x();
					vertices[v * 3 + 1] = vertex.y();
					vertices[v * 3 + 2] = vertex.z();
				}
				mesh.setVertices3D(vertices);
				IntBuffer indicesBuffer = BufferUtils.createIntBuffer(aiMesh.mNumFaces() * 3);
				AIFace.Buffer facesBuffer = aiMesh.mFaces();
				for(int f = 0; f < aiMesh.mNumFaces(); f++) {
					AIFace face = facesBuffer.get(f);
					indicesBuffer.put(face.mIndices());
				}
				mesh.setIndices(indicesBuffer.flip());
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
					mesh.setNormals(normals);
				}
				modelData.put(mesh, materials.get(aiMesh.mMaterialIndex()));
			}
		}
		Assimp.aiReleaseImport(aiScene);
		return new Model(modelData);
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".obj", ".dae"};
	}
}
