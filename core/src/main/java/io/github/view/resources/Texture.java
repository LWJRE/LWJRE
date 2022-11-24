package io.github.view.resources;

import io.github.view.Application;
import io.github.view.utils.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;

public final class Texture extends Resource {

	private static final HashMap<String, Texture> TEXTURES = new HashMap<>();

	public static Texture getOrLoad(String file) {
		if(TEXTURES.containsKey(file)) {
			return TEXTURES.get(file);
		}
		Texture texture = loadTexture(file);
		TEXTURES.put(file, texture);
		return texture;
	}

	public final int id;

	public Texture(ByteBuffer pixels, int width, int height) {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Textures can only be created from the rendering thread");
		this.id = GL15.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
	}

	public void bind() {
		this.bind(0);
	}

	public void bind(int textureUnit) {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Textures can only be bound from the rendering thread");
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}

	@Override
	public void delete() {
		if(!Application.isRenderingThread())
			throw new RuntimeException("Textures can only be deleted from the rendering thread");
		GL11.glDeleteTextures(this.id);
	}

	private static Texture loadTexture(String file) {
		BufferedImage image = FileUtils.readImage(file);
		ByteBuffer buffer = ByteBuffer.allocateDirect(4 * image.getWidth() * image.getHeight());
		for(int pixel : image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth())) {
			buffer.put((byte) ((pixel >> 16) & 0xff));
			buffer.put((byte) ((pixel >> 8) & 0xff));
			buffer.put((byte) (pixel & 0xff));
			buffer.put((byte) ((pixel >> 24) & 0xff));
		}
		return new Texture(buffer.flip(), image.getWidth(), image.getHeight());
	}
}
