package io.github.ardentengine.core.scene;

import io.github.ardentengine.core.rendering.Mesh;

public class MeshRenderer extends Node3D {

    public Mesh mesh;

    @Override
    protected void onUpdate(float delta) {
        if(this.mesh != null) {
            // TODO: Mesh shader
            // TODO: Camera3D
            this.mesh.draw();
        }
        super.onUpdate(delta);
    }
}
