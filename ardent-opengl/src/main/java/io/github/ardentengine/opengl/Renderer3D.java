package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.Mesh;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.scene.PointLight3D;
import io.github.ardentengine.core.scene.VisualInstance3D;
import org.lwjgl.BufferUtils;

import java.util.HashMap;
import java.util.HashSet;

public final class Renderer3D {

    private static Renderer3D instance;

    public static synchronized Renderer3D getInstance() {
        return instance == null ? instance = new Renderer3D() : instance;
    }

    // TODO: Should these use ShaderData and MeshData or Shader and Mesh?

    /** Batch used to group 3D objects for more efficient rendering. */
    private final HashMap<ShaderData, HashMap<Mesh, HashSet<VisualInstance3D>>> renderBatch = new HashMap<>();
    /** Batch used to group 3D lights to update the lights UBO before rendering. */
    private final HashSet<PointLight3D> lights = new HashSet<>();

    private Renderer3D() {

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
        // TODO: Come up with a better idea for shaders
        return Shader.getOrLoad("io/github/ardentengine/core/shaders/" + visualInstance.shaderType() + ".glsl");
    }

    public void addToBatch(Mesh mesh, VisualInstance3D instance) {
        this.renderBatch.computeIfAbsent(ShaderData.getOrCreate(getShader(instance)), shader -> new HashMap<>())
            .computeIfAbsent(mesh, key -> new HashSet<>())
            .add(instance);
    }

    public void addToBatch(PointLight3D light) {
        this.lights.add(light);
    }

    public void renderingProcess() {
        if(!this.renderBatch.isEmpty()) {
            if(!this.lights.isEmpty()) {
                // TODO: Add a maximum number of lights and sort them according to their distance from the camera
                var lightsBuffer = BufferUtils.createFloatBuffer(8 * this.lights.size());
                for(var light : this.lights) {
                    var position = light.globalPosition();
                    lightsBuffer.put(position.x()).put(position.y()).put(position.z()).put(0.0f);
                    lightsBuffer.put(light.color.r()).put(light.color.g()).put(light.color.b()).put(0.0f);
                }
                ShaderData.setBuffer("LightData", lightsBuffer.flip());
                ShaderData.setBuffer("LightData", this.lights.size(), 4096); // TODO: Hardcoded number
                this.lights.clear();
            }
            for(var shader : this.renderBatch.keySet()) {
                shader.start();
                var shaderBatch = this.renderBatch.get(shader);
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
            // TODO: Don't clear the whole thing to avoid recreating instances of HashMap every frame
            this.renderBatch.clear();
        }
    }
}