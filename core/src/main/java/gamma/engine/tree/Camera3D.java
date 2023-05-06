package gamma.engine.tree;

import gamma.engine.ApplicationProperties;
import gamma.engine.annotations.EditorAngle;
import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import gamma.engine.rendering.Shader;
import vecmatlib.matrix.Mat4f;

public class Camera3D extends Node3D {

	@EditorVariable(name = "Current")
	public boolean current = false;

	@EditorVariable(name = "Fov")
	@EditorAngle(min = 0.0f)
	public float fov = 1.22173f;

	@EditorVariable(name = "Near plane")
	@EditorRange(min = 0.001f)
	public float nearPlane = 0.1f;

	@EditorVariable(name = "Far plane")
	@EditorRange(min = 0.001f)
	public float farPlane = 1000.0f;

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		if(this.current) {
			Shader.setUniformStatic("projection_matrix", this.projectionMatrix());
			Shader.setUniformStatic("view_matrix", this.viewMatrix());
		}
	}

	// TODO: Yaw, pitch, roll

	public Mat4f projectionMatrix() {
		float focalLength = (float) (1.0f / Math.tan(this.fov / 2.0f));
		float aspect = ApplicationProperties.get("window/viewport/x", 16.0f) / ApplicationProperties.get("window/viewport/y", 9.0f);
		return new Mat4f(
				focalLength, 0.0f, 0.0f, 0.0f,
				0.0f, focalLength * aspect, 0.0f, 0.0f,
				0.0f, 0.0f, -(this.farPlane + this.nearPlane) / (this.farPlane - this.nearPlane), -(2 * this.farPlane * this.nearPlane) / (this.farPlane - this.nearPlane),
				0.0f, 0.0f, -1.0f, 0.0f
		);
	}

	public Mat4f viewMatrix() {
		return Mat4f.translation(this.globalPosition().negated()).multiply(this.globalRotation());
	}
}
