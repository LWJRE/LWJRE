package io.github.ardentengine.opengl;

import io.github.ardentengine.core.math.Matrix3x4;
import io.github.ardentengine.core.rendering.*;
import io.github.ardentengine.core.scene.PointLight3D;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public final class Renderer3D {

    private static Renderer3D instance;

    public static synchronized Renderer3D getInstance() {
        return instance == null ? instance = new Renderer3D() : instance;
    }

    /** Batch used to group 3D objects for more efficient rendering. */
    private final HashMap<Shader, HashMap<Material, HashMap<Mesh, HashSet<Matrix3x4>>>> renderBatch = new HashMap<>();
    /** Batch used to group 3D lights to update the lights UBO before rendering. */
    private final HashSet<PointLight3D> lights = new HashSet<>();

    private final Shader defaultShader = new Shader();
    private final Material3D defaultMaterial = new Material3D();

    private Renderer3D() {
        // TODO: Move this code somewhere else
        var classLoader = Thread.currentThread().getContextClassLoader();
        try(
            var vertexFile = classLoader.getResourceAsStream("io/github/ardentengine/core/shaders/default_shader_3d.vert");
            var fragmentFile = classLoader.getResourceAsStream("io/github/ardentengine/core/shaders/default_shader_3d.frag")
        ) {
            if(vertexFile == null || fragmentFile == null) {
                throw new RuntimeException(); // TODO: Better error handling
            }
            this.defaultShader.setVertexCode(new String(vertexFile.readAllBytes()));
            this.defaultShader.setFragmentCode(new String(fragmentFile.readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToBatch(Mesh mesh, Material material, Matrix3x4 transform) {
        if(material == null) {
            material = mesh.material() != null ? mesh.material() : this.defaultMaterial;
        }
        var shader = material instanceof ShaderMaterial shaderMaterial ? shaderMaterial.shader() : this.defaultShader;
        this.renderBatch.computeIfAbsent(shader, key -> new HashMap<>())
            .computeIfAbsent(material, key -> new HashMap<>())
            .computeIfAbsent(mesh, key -> new HashSet<>())
            .add(transform);
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
                ShaderProgram.setBuffer("LightData", lightsBuffer.flip());
                ShaderProgram.setBuffer("LightData", this.lights.size(), 4096); // TODO: Hardcoded number
                this.lights.clear();
            }
            for(var shader : this.renderBatch.keySet()) {
                var shaderProgram = ShaderProgram.getOrCreate(shader);
                var materialBatch = this.renderBatch.get(shader);
                shaderProgram.start();
                for(var material : materialBatch.keySet()) {
                    var meshBatch = materialBatch.get(material);
                    material.getParameters().forEach(shaderProgram::setUniform);
                    for(var mesh : meshBatch.keySet()) {
                        var meshData = MeshData.getOrCreate(mesh);
                        meshData.bind();
                        for(var transform : meshBatch.get(mesh)) {
                            shaderProgram.setUniform("transformation_matrix", transform);
                            meshData.draw();
                        }
                        meshData.unbind();
                    }
                }
            }
            // TODO: Don't clear the whole thing to avoid recreating instances of HashMap every frame
            this.renderBatch.clear();
        }
    }
}