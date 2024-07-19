package io.github.ardentengine.core.rendering;

import io.github.ardentengine.core.logging.Logger;
import io.github.ardentengine.core.resources.ResourceManager;

/**
 * Base texture class.
 * Other classes may extend this one to provide different ways of updating the texture.
 */
public abstract class Texture {

    /**
     * Utility method to get a texture resource using {@link ResourceManager#getOrLoad(String)}.
     * <p>
     *     Loads the texture at the given path or returns the same instance if it was already loaded.
     * </p>
     * <p>
     *     Returns null and logs an error if the resource at the given path is not of class {@code Texture}.
     * </p>
     * @param resourcePath Path at which to load the texture resource. Must point to an image file or a {@code .yaml} resource file in the classpath.
     * @return The requested texture.
     */
    public static Texture getOrLoad(String resourcePath) {
        var resource = ResourceManager.getOrLoad(resourcePath);
        if(resource instanceof Texture) {
            return (Texture) resource;
        }
        Logger.error("Resource " + resourcePath + " is not a texture");
        return null;
    }

    /**
     * Texture data used internally to abstract the representation of a texture across different rendering APIs.
     */
    protected final TextureData textureData = RenderingServer.getTextureData(this);

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
     * Updates this texture.
     * <p>
     *     Textures are updated before they are used in a shader.
     *     Different types of textures may override this method to provide their own update logic.
     * </p>
     */
    protected void updateTexture() {

    }
}