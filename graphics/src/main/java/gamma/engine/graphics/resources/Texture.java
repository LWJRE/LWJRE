package gamma.engine.graphics.resources;

import gamma.engine.core.utils.FileUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;

import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * Class representing an OpenGL texture object.
 *
 * @author Nico
 */
public final class Texture {

	/** Map of all the loaded textures */
	private static final HashMap<String, Texture> TEXTURES = new HashMap<>();

	/**
	 * Loads a texture or gets the same instance if it was already loaded.
	 *
	 * @param file Path to the texture file
	 * @return The requested texture
	 */
	public static Texture getOrLoad(String file) {
		if(TEXTURES.containsKey(file)) {
			return TEXTURES.get(file);
		}
		Texture texture = loadTexture(file);
		TEXTURES.put(file, texture);
		return texture;
	}

	/** Texture id */
	private final int id;

	/**
	 * Constructs a texture from the given pixels buffer.
	 *
	 * @param pixels Byte buffer of pixels
	 * @param width Width of the texture
	 * @param height Height of the texture
	 * @param properties Texture properties
	 */
	public Texture(ByteBuffer pixels, int width, int height, TextureProperties properties) {
		this.id = GL15.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		properties.apply();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
	}

	/**
	 * Constructs a texture from the given pixels buffer.
	 *
	 * @param pixels Byte buffer of pixels
	 * @param width Width of the texture
	 * @param height Height of the texture
	 */
	public Texture(ByteBuffer pixels, int width, int height) {
		this(pixels, width, height, new TextureProperties());
	}

	/**
	 * Constructs a texture from the given array of pixels.
	 *
	 * @param pixels A float array where every 3 values are the rgb components of a pixel
	 * @param width Width of the texture
	 * @param height Height of the texture
	 * @param properties Texture properties
	 */
	public Texture(float[] pixels, int width, int height, TextureProperties properties) {
		this.id = GL15.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		properties.apply();
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_FLOAT, pixels);
	}

	/**
	 * Constructs a texture from the given array of pixels.
	 *
	 * @param pixels A float array where every 3 values are the rgb components of a pixel
	 * @param width Width of the texture
	 * @param height Height of the texture
	 */
	public Texture(float[] pixels, int width, int height) {
		this(pixels, width, height, new TextureProperties());
	}

	/**
	 * Binds this texture.
	 * Textures need to be bound before they can be used in shaders.
	 */
	public void bind() {
		this.bind(0);
	}

	/**
	 * Binds this texture to the given texture unit.
	 * Textures need to be bound before they can be used in shaders.
	 *
	 * @param textureUnit Texture unit
	 */
	public void bind(int textureUnit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
	}

	/**
	 * Loads an image file as a texture.
	 *
	 * @param file Path to the image file
	 * @return The requested texture.
	 */
	private static Texture loadTexture(String file) {
		return FileUtils.readImageOptional(file).map(image -> {
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * image.getWidth() * image.getHeight());
			for(int pixel : image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth())) {
				buffer.put((byte) ((pixel >> 16) & 0xff));
				buffer.put((byte) ((pixel >> 8) & 0xff));
				buffer.put((byte) (pixel & 0xff));
				buffer.put((byte) ((pixel >> 24) & 0xff));
			}
			return new Texture(buffer.flip(), image.getWidth(), image.getHeight(), new TextureProperties(file + ".properties"));
		}).orElse(new Texture(new float[] {0.0f,0.0f,0.0f, 1.0f,1.0f,1.0f, 1.0f,1.0f,1.0f, 0.0f,0.0f,0.0f}, 2, 2));
	}
}
