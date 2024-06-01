package io.github.hexagonnico.opengl;

import io.github.hexagonnico.core.rendering.*;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

/**
 * OpenGL implementation of the {@link RenderingApi}.
 */
public class OpenGLApi implements RenderingApi {

    private static final HashMap<Mesh, OpenGLMesh> MESHES = new HashMap<>();
    private static final HashMap<Shader, OpenGLShader> SHADERS = new HashMap<>();
    private static final HashMap<Texture, OpenGLTexture> TEXTURES = new HashMap<>();

    @Override
    public void setDefaultClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void clearScreen() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public MeshData createMesh(Mesh mesh) {
        return MESHES.computeIfAbsent(mesh, key -> new OpenGLMesh());
    }

    public static OpenGLMesh getMeshData(Mesh mesh) {
        return MESHES.get(mesh); // TODO: Log error if the mesh is invalid
    }

    @Override
    public ShaderData createShader(Shader shader) {
        return SHADERS.computeIfAbsent(shader, key -> new OpenGLShader());
    }

    public static OpenGLShader getShaderData(Shader shader) {
        return SHADERS.get(shader); // TODO: Log error if the shader is invalid
    }

    @Override
    public TextureData createTexture(Texture texture) {
        return TEXTURES.computeIfAbsent(texture, key -> new OpenGLTexture());
    }

    public static OpenGLTexture getTextureData(Texture texture) {
        return TEXTURES.get(texture); // TODO: Log error if the texture is invalid
    }

    @Override
    public void render(Mesh mesh, Shader shader) {
        // TODO: Log error if mesh and shader are invalid (if the HashMap does not contain them)
        SHADERS.get(shader).start();
        MESHES.get(mesh).draw();
    }

    public static void deleteResources() {
        for(var mesh : MESHES.values()) {
            mesh.delete();
        }
        for(var shader : SHADERS.values()) {
            shader.delete();
        }
        for(var texture : TEXTURES.values()) {
            texture.delete();
        }
    }
}
