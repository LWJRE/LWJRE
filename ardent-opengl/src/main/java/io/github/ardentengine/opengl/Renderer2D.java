package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.Material;
import io.github.ardentengine.core.rendering.QuadMesh2D;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.rendering.ShaderMaterial;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

public final class Renderer2D {

    private static Renderer2D instance;

    public static synchronized Renderer2D getInstance() {
        return instance == null ? instance = new Renderer2D() : instance;
    }

    private final HashMap<Shader, HashMap<Material, HashSet<DrawData2D>>> renderBatch = new HashMap<>();
    // TODO: Make something better than QuadMesh2D
    private final QuadMesh2D quadMesh = new QuadMesh2D();

    private final Shader defaultShader;
    private final Material defaultMaterial = new Material(); // TODO: Add a Material2D class

    private Renderer2D() {
        // TODO: Move this code somewhere else
        var classLoader = Thread.currentThread().getContextClassLoader();
        try(
            var vertexFile = classLoader.getResourceAsStream("io/github/ardentengine/core/shaders/default_shader_2d.vert");
            var fragmentFile = classLoader.getResourceAsStream("io/github/ardentengine/core/shaders/default_shader_2d.frag")
        ) {
            if(vertexFile == null || fragmentFile == null) {
                throw new RuntimeException(); // TODO: Better error handling
            }
            this.defaultShader = new Shader(new String(vertexFile.readAllBytes()), new String(fragmentFile.readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToBatch(DrawData2D drawData, Material material) {
        var shader = this.defaultShader;
        if(material instanceof ShaderMaterial shaderMaterial && shaderMaterial.shader() != null) {
            shader = shaderMaterial.shader();
        }
        this.renderBatch.computeIfAbsent(shader, key -> new HashMap<>())
            .computeIfAbsent(material != null ? material : this.defaultMaterial, key -> new HashSet<>())
            .add(drawData);
    }

    public void renderingProcess() {
        if(!this.renderBatch.isEmpty()) {
            var quadMesh = MeshData.getOrCreate(this.quadMesh);
            quadMesh.bind();
            for(var shader : this.renderBatch.keySet()) {
                var shaderProgram = ShaderProgram.getOrCreate(shader);
                var materialBatch = this.renderBatch.get(shader);
                shaderProgram.start();
                for(var material : materialBatch.keySet()) {
                    material.getParameters().forEach(shaderProgram::setUniform);
                    for(var drawData : materialBatch.get(material)) {
                        // TODO: Reimplement z index
                        shaderProgram.setUniform("color_texture", drawData.texture());
                        shaderProgram.setUniform("texture_size", drawData.texture().size());
                        shaderProgram.setUniform("vertex_offset", drawData.vertexOffset());
                        shaderProgram.setUniform("vertex_scale", drawData.vertexScale());
                        shaderProgram.setUniform("uv_offset", drawData.uvOffset());
                        shaderProgram.setUniform("uv_scale", drawData.uvScale());
                        shaderProgram.setUniform("transformation_matrix", drawData.transform());
                        quadMesh.draw();
                    }
                }
            }
            quadMesh.unbind();
            this.renderBatch.clear();
        }
    }
}