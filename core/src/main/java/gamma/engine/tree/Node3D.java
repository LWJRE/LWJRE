package gamma.engine.tree;

import gamma.engine.annotations.EditorRange;
import gamma.engine.annotations.EditorVariable;
import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

public class Node3D extends Node {

	@EditorVariable(name = "Position")
	@EditorRange
	public Vec3f position = Vec3f.Zero();

	@EditorVariable(name = "Rotation")
	@EditorRange
	public Vec3f rotation = Vec3f.Zero();

	@EditorVariable(name = "Scale")
	@EditorRange
	public Vec3f scale = Vec3f.One();

	public Vec3f localPosition() {
		return this.position;
	}

	public Vec3f globalPosition() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalTransformation().multiply(new Vec4f(this.position, 1.0f)).xyz();
		}
		return this.position;
	}

	public Mat4f localTranslation() {
		return Mat4f.translation(this.position);
	}

	public Vec3f rotationDegrees() {
		return new Vec3f((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	public void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vec3f((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public void setRotationDegrees(Vec3f degrees) {
		this.setRotationDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	public void rotateDegrees(float x, float y, float z) {
		this.rotation = this.rotation.plus((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public void rotateDegrees(Vec3f degrees) {
		this.rotateDegrees(degrees.x(), degrees.y(), degrees.z());
	}

	public Mat4f localRotation() {
		return Mat4f.rotation(this.rotation);
	}

	public Mat4f globalRotation() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalRotation().multiply(this.localRotation());
		}
		return this.localRotation();
	}

	public Vec3f globalScale() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalScale().multiply(this.scale);
		}
		return this.scale;
	}

	public Mat4f scalingMatrix() {
		return Mat4f.scaling(this.scale);
	}

	public Mat4f localTransformation() {
		return this.localTranslation().multiply(this.localRotation()).multiply(this.scalingMatrix());
	}

	public Mat4f globalTransformation() {
		if(this.getParent() instanceof Node3D parent) {
			return parent.globalTransformation().multiply(this.localTransformation());
		}
		return this.localTransformation();
	}
}
