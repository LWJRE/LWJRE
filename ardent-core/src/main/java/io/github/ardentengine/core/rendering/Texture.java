package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.resources.ResourceManager;

/**
 * Base texture class.
 * Other classes may extend this one to provide different ways of updating the texture.
 */
public abstract class Texture {

    public static Texture getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof Texture) {
            return (Texture) resource;
        }
        System.err.println("Resource " + resourcePath + " is not a texture");
        return null;
    }

    /**
     * Texture data used internally to abstract the representation of a texture across different rendering APIs.
     */
    protected final TextureData textureData = RenderingServer.createTexture();

    /**
     * Returns the width of this texture.
     *
     * @return The width of this texture.
     */
    public abstract int getWidth();

    /**
     * Returns the height of this texture.
     *
     * @return The height of this texture.
     */
    public abstract int getHeight();

    /**
     * Binds this texture for it to be used by the current rendering API.
     * This method is called before the texture is used in a shader.
     * Texture classes may override this method to update the texture if it has been modified.
     */
    public void bind() {
        this.textureData.bindTexture();
    }
}
