package io.github.ardentengine.opengl;

import io.github.ardentengine.core.rendering.QuadMesh2D;
import io.github.ardentengine.core.rendering.Shader;
import io.github.ardentengine.core.scene.VisualInstance2D;

import java.util.ArrayList;

public final class Renderer2D {

    private static Renderer2D instance;

    public static synchronized Renderer2D getInstance() {
        return instance == null ? instance = new Renderer2D() : instance;
    }

    private final ArrayList<VisualInstance2D> renderBatch = new ArrayList<>();
    // TODO: Make something better than QuadMesh2D
    private final QuadMesh2D quadMesh = new QuadMesh2D();

    private Renderer2D() {

    }

    public void addToBatch(VisualInstance2D instance) {
        this.renderBatch.add(instance);
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
        // TODO: Come up with a better idea for shaders
        return Shader.getOrLoad("io/github/ardentengine/core/shaders/" + visualInstance.shaderType() + ".glsl");
    }

    public void renderingProcess() {
        if(!this.renderBatch.isEmpty()) {
            var quadMesh = MeshData.getOrCreate(this.quadMesh);
            quadMesh.bind();
            // Render objects in the reverse order to ensure objects that are rendered last are rendered on top
            for(int i = this.renderBatch.size() - 1; i >= 0; i--) {
                var instance = this.renderBatch.get(i);
                var shader = ShaderData.getOrCreate(getShader(instance));
                shader.start();
                shader.set("transformation_matrix", instance.globalTransform());
                // FIXME: This not the effective z index
                shader.set("z_index", instance.zIndex());
                instance.getShaderParameters().forEach(shader::set);
                quadMesh.draw();
            }
            quadMesh.unbind();
            this.renderBatch.clear();
        }
    }
}