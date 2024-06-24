package io.github.hexagonnico.core.scene;

import io.github.hexagonnico.core.rendering.QuadMesh2D;
import io.github.hexagonnico.core.rendering.Shader;
import io.github.hexagonnico.core.rendering.Texture;
import io.github.scalamath.colorlib.Col4f;
import io.github.scalamath.colorlib.Color;

public class Sprite2D extends Node2D {

    public Texture spriteTexture;
    public Texture normalMap;

    public Color modulate = new Col4f(1.0f, 1.0f, 1.0f, 1.0f);

    public Shader shader = Shader.getBuiltinShader("sprite_shader");

    @Override
    protected void onUpdate(float delta) {
        if(this.shader == null) {
            this.shader = Shader.getBuiltinShader("sprite_shader");
        }
        this.shader.set("transformation_matrix", this.globalTransform());
        if(this.spriteTexture != null) {
            this.shader.set("texture_size", this.spriteTexture.getWidth(), this.spriteTexture.getHeight());
        } else {
            this.shader.set("texture_size", 0, 0);
        }
        this.shader.set("sprite_texture", this.spriteTexture);
//        this.shader.set("modulate", this.modulate);
        this.shader.set("projection_matrix", Camera2D.currentProjection());
        this.shader.set("view_matrix", Camera2D.currentView());
        this.shader.set("normal_map", this.normalMap);
        this.shader.draw(QuadMesh2D.getInstance());
    }
}
