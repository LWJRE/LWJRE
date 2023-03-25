package gamma.engine.core.resources;

import gamma.engine.core.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryUtil;
import vecmatlib.color.Color;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that represents a 3D model.
 *
 * @author Nico
 */
public record Model(Map<Mesh, Material> modelData) {

	/**
	 * Loads a model from the classpath or return the same instance if it was already loaded.
	 *
	 * @see Resources#getOrLoad(String)
	 *
	 * @param path Path to the model file
	 * @return The requested model
	 * @throws RuntimeException if the resource at the given path is not a model
	 */
	public static Model getOrLoad(String path) {
		Object resource = Resources.getOrLoad(path);
		if(resource instanceof Model model)
			return model;
		throw new RuntimeException("Resource " + path + " is not a model");
	}

	/**
	 * Implementation of a {@link ResourceLoader} for loading 3D models using the {@link Assimp} library.
	 * Calling {@code Model.ASSIMP_LOADER.load(String)} loads the model from the classpath,
	 * for getting models normally it is better to use {@link Model#getOrLoad(String)}.
	 */
	public static final ResourceLoader<Model> ASSIMP_LOADER = path -> {
		AIFileIO fileIO = AIFileIO.create().OpenProc((pFileIO, fileName, openMode) -> {
			String fileNameStr = MemoryUtil.memUTF8(fileName);
			byte[] bytes = FileUtils.readResourceBytes(fileNameStr);
			ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes).flip();
			return AIFile.create().ReadProc((pFile, pBuffer, size, count) -> {
				long max = Math.min(buffer.remaining() / size, count);
				MemoryUtil.memCopy(MemoryUtil.memAddress(buffer), pBuffer, max * size);
				buffer.position(buffer.position() + (int) (max * size));
				return max;
			}).SeekProc((pFile, offset, origin) -> {
				if(origin == Assimp.aiOrigin_CUR) {
					buffer.position(buffer.position() + (int) offset);
				} else if(origin == Assimp.aiOrigin_SET) {
					buffer.position((int) offset);
				} else if(origin == Assimp.aiOrigin_END) {
					buffer.position(buffer.limit() + (int) offset);
				}
				return 0;
			}).FileSizeProc(pFile -> buffer.limit()).address();
		}).CloseProc((pFileIO, pFile) -> {
			AIFile aiFile = AIFile.create(pFile);
			aiFile.ReadProc().free();
			aiFile.SeekProc().free();
			aiFile.FileSizeProc().free();
		});
		AIScene aiScene = Assimp.aiImportFileEx(path, 0, fileIO); // TODO: Add flags in a .properties file
		fileIO.OpenProc().free();
		fileIO.CloseProc().free();
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
						new Color(ambient.r(), ambient.g(), ambient.b(), ambient.a()),
						new Color(diffuse.r(), diffuse.g(), diffuse.b(), diffuse.a()),
						new Color(specular.r(), specular.g(), specular.b(), specular.a()),
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
	};
}
