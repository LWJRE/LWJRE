package engine.core.resources;

import engine.core.utils.PropertiesFile;
import org.lwjgl.opengl.GL11;

public class TextureProperties extends PropertiesFile {

	public TextureProperties() {
		super();
	}

	public TextureProperties(String filePath) {
		super(filePath);
	}

	public final void apply() {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, this.getMinFilter());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, this.getMagFilter());
	}

	public final int getMinFilter() {
		return this.get("minFilter", GL11.GL_LINEAR);
	}

	public final TextureProperties setMinFilter(int filter) {
		return (TextureProperties) this.set("minFilter", filter);
	}

	public final int getMagFilter() {
		return this.get("magFilter", GL11.GL_LINEAR);
	}

	public final TextureProperties setMagFilter(int filter) {
		return (TextureProperties) this.set("magFilter", filter);
	}
}
