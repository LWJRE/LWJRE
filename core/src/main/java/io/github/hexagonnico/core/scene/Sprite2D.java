package io.github.hexagonnico.core.scene;

import io.github.hexagonnico.core.DisplayServer;
import io.github.hexagonnico.core.rendering.QuadMesh2D;
import io.github.hexagonnico.core.rendering.RenderingServer;
import io.github.hexagonnico.core.rendering.Shader;
import io.github.hexagonnico.core.rendering.Texture;
import io.github.hexagonnico.core.resources.ResourceManager;
import io.github.scalamath.colorlib.Col4f;
import io.github.scalamath.colorlib.Color;
import io.github.scalamath.vecmatlib.Mat3f;
import io.github.scalamath.vecmatlib.Mat4f;

public class Sprite2D extends Node2D {

    private static final QuadMesh2D MESH = new QuadMesh2D();

    public Texture spriteTexture = (Texture) ResourceManager.getOrLoad("icon.png");
    public Texture normalMap;

    public Color modulate = new Col4f(1.0f, 1.0f, 1.0f, 1.0f);

    public Shader shader;

    @Override
    protected void onUpdate(float delta) {
        // TODO: Move access to the shader to a "render" method
        if(this.shader == null) {
            this.shader = Shader.getBuiltinShader("sprite_shader");
        }
        this.shader.set("transformation_matrix", this.globalTransform());
        var windowSize = DisplayServer.getWindowSize().toFloat(); // TODO: Avoid computing this every frame
        this.shader.set("projection_matrix", Mat4f.orthographicProjection(windowSize.x(), windowSize.aspect(), 0.1f, 100.0f));
        if(this.spriteTexture != null) {
            this.shader.set("texture_size", this.spriteTexture.getWidth(), this.spriteTexture.getHeight());
            this.shader.set("sprite_texture", this.spriteTexture);
        }
        this.shader.set("modulate", this.modulate);
        if(Camera2D.current() != null) {
            this.shader.set("view_matrix", Camera2D.current().viewMatrix());
        } else {
            // TODO: Store the "identity view matrix" somewhere
            this.shader.set("view_matrix", new Mat3f(1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f));
        }
        this.shader.set("normal_map", this.normalMap);
        RenderingServer.render(MESH, this.shader);
    }
}
