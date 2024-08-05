package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.*;
import io.github.ardentengine.core.scene.PointLight3D;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

/**
 * OpenGL implementation of the {@link RenderingApi}.
 */
public class OpenGLApi implements RenderingApi {

    @Override
    public void setDefaultClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void draw(Mesh mesh) {
        MeshData.getOrCreate(mesh).draw();
    }

    @Override
    public void update(Mesh mesh) {
        MeshData.requestUpdate(mesh);
    }

    @Override
    public void update(Texture texture) {
        TextureData.requestUpdate(texture);
    }

    @Override
    public ShaderData getShaderData(Shader shader) {
        return OpenGLShader.getOrCreate(shader);
    }

    private UniformBufferObject lightUbo;

    private final HashMap<PointLight3D, Integer> lights = new HashMap<>();
    private int lightsCount = 0;

    @Override
    public void updateLight(PointLight3D light) {
        // TODO: This needs a better solution
        if(this.lightUbo == null) {
            this.lightUbo = new UniformBufferObject("Lights", 4100);
        }
        var lightIndex = this.lights.computeIfAbsent(light, key -> this.lightsCount++);
        this.lightUbo.putData(new float[] {
            light.position.x(), light.position.y(), light.position.z(), 0.0f,
            light.color.r(), light.color.g(), light.color.b(), 0.0f
        }, 32L * lightIndex);
        this.lightUbo.putData(new int[] {this.lightsCount}, 4096L);
    }
}