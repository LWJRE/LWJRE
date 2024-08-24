package io.github.ardentengine.opengl;

import io.github.ardentengine.core.DisplaySystem;
import io.github.ardentengine.core.RenderingSystem;
import io.github.ardentengine.core.rendering.Mesh;
import io.github.ardentengine.core.rendering.QuadMesh2D;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.rendering.Texture;
import io.github.ardentengine.core.scene.*;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;

/**
 * OpenGL implementation of the rendering system.
 */
public class OpenGLSystem extends RenderingSystem {

    /** Singleton instance. */
    private static OpenGLSystem instance;

    /**
     * Returns the singleton instance.
     *
     * @return The singleton instance.
     */
    public static synchronized OpenGLSystem getInstance() {
        return instance == null ? instance = new OpenGLSystem() : instance;
    }

    // TODO: Should these use ShaderData and MeshData or Shader and Mesh?

    /** Batch used to group 3D objects for more efficient rendering. */
    private final HashMap<ShaderData, HashMap<Mesh, HashSet<VisualInstance3D>>> renderBatch3D = new HashMap<>();
    /** Batch used to group 3D lights to update the lights UBO before rendering. */
    private final HashSet<PointLight3D> lights = new HashSet<>();
    /** Batch used to group 2D objects for more efficient rendering. */
    private final HashMap<Shader, HashSet<VisualInstance2D>> renderBatch2D = new HashMap<>();
    /** Quad mesh used to render 2D objects. */
    private final QuadMesh2D quadMesh = new QuadMesh2D();

    /**
     * Private constructor for singleton instance.
     */
    private OpenGLSystem() {

    }

    @Override
    public void setDefaultClearColor(float red, float green, float blue, float alpha) {
        GL11.glClearColor(red, green, blue, alpha);
    }

    @Override
    public void setCamera(Camera3D camera) {
        // TODO: Find a way to only update this when the camera changes
        var viewMatrix = camera.viewMatrix();
        var projectionMatrix = camera.projectionMatrix();
        ShaderData.setBuffer("Camera3D", new float[] {
            viewMatrix.m00(), viewMatrix.m10(), viewMatrix.m20(), viewMatrix.m30(),
            viewMatrix.m01(), viewMatrix.m11(), viewMatrix.m21(), viewMatrix.m31(),
            viewMatrix.m02(), viewMatrix.m12(), viewMatrix.m22(), viewMatrix.m32(),
            viewMatrix.m03(), viewMatrix.m13(), viewMatrix.m23(), viewMatrix.m33(),
            projectionMatrix.m00(), projectionMatrix.m10(), projectionMatrix.m20(), projectionMatrix.m30(),
            projectionMatrix.m01(), projectionMatrix.m11(), projectionMatrix.m21(), projectionMatrix.m31(),
            projectionMatrix.m02(), projectionMatrix.m12(), projectionMatrix.m22(), projectionMatrix.m32(),
            projectionMatrix.m03(), projectionMatrix.m13(), projectionMatrix.m23(), projectionMatrix.m33()
        });
    }

    /**
     * Private method used to get a shader from a 3D visual instance.
     * Returns the user-defined shader for this visual instance if it was defined, otherwise returns the built-in shader.
     *
     * @param visualInstance The visual instance.
     * @return The shader used by this visual instance.
     */
    private static Shader getShader(VisualInstance3D visualInstance) {
        if(visualInstance.shader != null) {
            return visualInstance.shader;
        }
        return Shader.getOrLoad("io/github/ardentengine/core/shaders/" + visualInstance.shaderType() + ".glsl");
    }

    @Override
    public void render(Mesh mesh, VisualInstance3D visualInstance) {
        this.renderBatch3D.computeIfAbsent(ShaderData.getOrCreate(getShader(visualInstance)), shader -> new HashMap<>())
            .computeIfAbsent(mesh, key -> new HashSet<>())
            .add(visualInstance);
    }

    @Override
    public void setCamera(Camera2D camera) {
        // TODO: Find a way to only update this when the camera changes
        var viewMatrix = camera.viewMatrix();
        var projectionMatrix = camera.projectionMatrix();
        ShaderData.setBuffer("Camera2D", new float[] {
            viewMatrix.m00(), viewMatrix.m10(), viewMatrix.m20(), 0.0f,
            viewMatrix.m01(), viewMatrix.m11(), viewMatrix.m21(), 0.0f,
            viewMatrix.m02(), viewMatrix.m12(), viewMatrix.m22(), 0.0f,
            projectionMatrix.m00(), projectionMatrix.m10(), projectionMatrix.m20(), projectionMatrix.m30(),
            projectionMatrix.m01(), projectionMatrix.m11(), projectionMatrix.m21(), projectionMatrix.m31(),
            projectionMatrix.m02(), projectionMatrix.m12(), projectionMatrix.m22(), projectionMatrix.m32(),
            projectionMatrix.m03(), projectionMatrix.m13(), projectionMatrix.m23(), projectionMatrix.m33()
        });
        // TODO: The 2D camera should have a default value
    }

    /**
     * Private method used to get a shader from a 2D visual instance.
     * Returns the user-defined shader for this visual instance if it was defined, otherwise returns the built-in shader.
     *
     * @param visualInstance The visual instance.
     * @return The shader used by this visual instance.
     */
    private static Shader getShader(VisualInstance2D visualInstance) {
        if(visualInstance.shader != null) {
            return visualInstance.shader;
        }
        return Shader.getOrLoad("io/github/ardentengine/core/shaders/" + visualInstance.shaderType() + ".glsl");
    }

    @Override
    public void render(VisualInstance2D visualInstance) {
        this.renderBatch2D.computeIfAbsent(getShader(visualInstance), shader -> new HashSet<>())
            .add(visualInstance);
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
    public void updateLight(PointLight3D light) {
        this.lights.add(light);
    }

    @Override
    protected void initialize() {
        GL.createCapabilities();
        this.setDefaultClearColor(0.3f, 0.3f, 0.3f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    @Override
    protected void process() {
        // TODO: Add viewport scaling options
        var windowSize = DisplaySystem.getInstance().getWindowSize();
        GL11.glViewport(0, 0, windowSize.x(), windowSize.y());
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        var lightsBuffer = BufferUtils.createFloatBuffer(8 * this.lights.size());
        for(var light : this.lights) {
            var position = light.globalPosition();
            lightsBuffer.put(position.x()).put(position.y()).put(position.z()).put(0.0f);
            lightsBuffer.put(light.color.r()).put(light.color.g()).put(light.color.b()).put(0.0f);
        }
        ShaderData.setBuffer("LightData", lightsBuffer.flip());
        ShaderData.setBuffer("LightData", this.lights.size(), 4096); // TODO: Hardcoded number
        for(var shader : this.renderBatch3D.keySet()) {
            shader.start();
            var shaderBatch = this.renderBatch3D.get(shader);
            // TODO: Load material properties into shader
            for(var mesh : shaderBatch.keySet()) {
                var meshData = MeshData.getOrCreate(mesh);
                meshData.bind();
                for(var instance : shaderBatch.get(mesh)) {
                    shader.set("transformation_matrix", instance.globalTransform());
                    instance.getShaderParameters().forEach(shader::set);
                    meshData.draw();
                }
                meshData.unbind();
            }
        }
        var quadMesh = MeshData.getOrCreate(this.quadMesh);
        quadMesh.bind();
        for(var shader : this.renderBatch2D.keySet()) {
            var shaderData = ShaderData.getOrCreate(shader);
            shaderData.start();
            for(var instance : this.renderBatch2D.get(shader)) {
                shaderData.set("transformation_matrix", instance.globalTransform());
                shaderData.set("z_index", instance.zIndex);
                instance.getShaderParameters().forEach(shaderData::set);
                quadMesh.draw();
            }
        }
        quadMesh.unbind();
        // TODO: Don't clear the whole thing to avoid recreating instances of HashMap every frame
        this.lights.clear();
        this.renderBatch3D.clear();
        this.renderBatch2D.clear();
    }

    @Override
    protected void terminate() {
        MeshData.deleteMeshes();
        ShaderData.deleteShaders();
        TextureData.deleteTextures();
    }
}