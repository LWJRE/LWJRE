package io.github.lwjre.resources;

import io.github.lwjre.utils.FileUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.assimp.AIFile;
import org.lwjgl.assimp.AIFileIO;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

/**
 * Implementation of a {@link ResourceLoader} to load model files using {@link Assimp}.
 *
 * @author Nico
 */
public final class ObjLoader extends ModelLoader {

	@Override
	protected AIScene loadScene(String path) {
		AIFileIO aiFileIO = AIFileIO.create().OpenProc((pFileIO, fileName, openMode) -> {
			String fileNameUtf8 = MemoryUtil.memUTF8(fileName);
			byte[] bytes = FileUtils.readResourceBytes(fileNameUtf8); // TODO: This causes a fatal error if the file does not exist
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
		AIScene aiScene = Assimp.aiImportFileEx(path, 0, aiFileIO); // TODO: Add flags in a .properties file
		aiFileIO.OpenProc().free();
		aiFileIO.CloseProc().free();
		if(aiScene == null) {
			throw new RuntimeException("Failed to load model " + path + ": " + Assimp.aiGetErrorString());
		}
		return aiScene;
	}

	@Override
	public String[] getExtensions() {
		return new String[] {".obj"};
	}
}
