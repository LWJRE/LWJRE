package engine.core.tree;

import vecmatlib.matrix.Mat4f;
import vecmatlib.vector.Vec3f;
import vecmatlib.vector.Vec4f;

public class Transform3D extends Node {

	public Vec3f position = Vec3f.Zero();
	public Vec3f rotation = Vec3f.Zero();
	public Vec3f scale = Vec3f.One();

	public final Vec3f localPosition() {
		return this.position;
	}

	public final Vec3f globalPosition() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalTransformation().multiply(new Vec4f(this.position, 1.0f)).xyz();
		return this.position;
	}

	public final Mat4f localTranslation() {
		return Mat4f.translation(this.position);
	}

	public final Vec3f rotationDegrees() {
		return new Vec3f((float) Math.toDegrees(this.rotation.x()), (float) Math.toDegrees(this.rotation.y()), (float) Math.toDegrees(this.rotation.z()));
	}

	public final void setRotationDegrees(float x, float y, float z) {
		this.rotation = new Vec3f((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public final void rotateDegrees(float x, float y, float z) {
		this.rotation = this.rotation.plus((float) Math.toRadians(x), (float) Math.toRadians(y), (float) Math.toRadians(z));
	}

	public final Mat4f localRotation() {
		return Mat4f.rotation(this.rotation);
	}

	public final Mat4f globalRotation() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalRotation().multiply(this.localRotation());
		return this.localRotation();
	}

	public final Vec3f globalScale() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalScale().multiply(this.scale);
		return this.scale;
	}

	public final Mat4f scalingMatrix() {
		return Mat4f.scaling(this.scale);
	}

	public final Mat4f localTransformation() {
		return this.localTranslation().multiply(this.localRotation()).multiply(this.scalingMatrix());
	}

	public final Mat4f globalTransformation() {
		if(this.getParent() instanceof Transform3D parent)
			return parent.globalTransformation().multiply(this.localTransformation());
		return this.localTransformation();
	}
}
