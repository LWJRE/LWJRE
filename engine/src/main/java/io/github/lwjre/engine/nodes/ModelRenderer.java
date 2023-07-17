package io.github.lwjre.engine.nodes;

import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.hexagonnico.vecmatlib.vector.Vec4f;
import io.github.lwjre.engine.annotations.EditorVariable;
import io.github.lwjre.engine.resources.Model;
import io.github.lwjre.engine.servers.RenderingServer;

/**
 * Node that represents an object rendered as a 3D {@link Model}.
 *
 * @author Nico
 */
public class ModelRenderer extends Renderer3D {

	/**
	 * The model used by this renderer.
	 */
	@EditorVariable(name = "Model")
	private Model model = new Model();

	/**
	 * If true, this model will only be rendered if it is inside the camera's frustum.
	 */
	@EditorVariable(name = "Use frustum culling")
	public boolean useFrustumCulling = true;

	@Override
	protected void onUpdate(float delta) {
		super.onUpdate(delta);
		if(!this.useFrustumCulling || this.isInsideFrustum()) {
			this.addToBatch();
		}
	}

	@Override
	protected void onEditorProcess() {
		super.onEditorProcess();
		this.addToBatch();
	}

	/**
	 * Adds this model to the rendering batch in the {@link RenderingServer}.
	 */
	private void addToBatch() {
		this.model.modelData().forEach((mesh, material) -> RenderingServer.addToBatch(mesh, () -> {
			this.shader().start();
			this.shader().setUniform("transformation_matrix", this.globalTransform());
			this.shader().setUniform("material.ambient", material.ambientColor());
			this.shader().setUniform("material.diffuse", material.diffuseColor());
			this.shader().setUniform("material.specular", material.specularColor());
			this.shader().setUniform("material.shininess", material.shininess());
			mesh.draw();
		}));
	}

	/**
	 * Sets this object's model.
	 * If the given model is null, then an empty {@link Model#Model()} will be used.
	 *
	 * @param model The model to set to this object
	 */
	public void setModel(Model model) {
		this.model = model != null ? model : new Model();
	}

	/**
	 * Sets this object's model to the one at the given path.
	 *
	 * @see Model#getOrLoad(String)
	 *
	 * @param path Path to the model to use in the classpath
	 */
	public void setModel(String path) {
		this.setModel(Model.getOrLoad(path));
	}

	/**
	 * Gets the model used by this object.
	 *
	 * @return The model used by this object
	 */
	public Model model() {
		return this.model;
	}

	/**
	 * Checks if this model is inside the camera frustum.
	 * Computes the frustum planes from the current camera and checks if the bounding sphere of this object's model is inside the camera frustum.
	 * If no {@link Camera3D} is current, this method will assume the object is not inside the camera frustum.
	 *
	 * @return True if this object is inside the camera frustum, false if it is not inside the camera frustum or there is no current camera
	 *
	 * @see Camera3D#getCurrent()
	 */
	public boolean isInsideFrustum() {
		Camera3D camera = Camera3D.getCurrent();
		if(camera != null) {
			Mat4f matrix = camera.projectionMatrix().multiply(camera.viewMatrix());
			Vec4f left = new Vec4f(matrix.m30() + matrix.m00(), matrix.m31() + matrix.m01(), matrix.m32() + matrix.m02(), matrix.m33() + matrix.m03());
			Vec4f right = new Vec4f(matrix.m30() - matrix.m00(), matrix.m31() - matrix.m01(), matrix.m32() - matrix.m02(), matrix.m33() - matrix.m03());
			Vec4f top = new Vec4f(matrix.m30() - matrix.m10(), matrix.m31() - matrix.m11(), matrix.m32() - matrix.m12(), matrix.m33() - matrix.m13());
			Vec4f bottom = new Vec4f(matrix.m30() + matrix.m10(), matrix.m31() + matrix.m11(), matrix.m32() + matrix.m12(), matrix.m33() + matrix.m13());
			Vec4f near = new Vec4f(matrix.m30() + matrix.m20(), matrix.m31() + matrix.m21(), matrix.m32() + matrix.m22(), matrix.m33() + matrix.m23());
			Vec4f far = new Vec4f(matrix.m30() - matrix.m20(), matrix.m31() - matrix.m21(), matrix.m32() - matrix.m22(), matrix.m33() - matrix.m23());
			Vec3f globalPosition = this.globalPosition();
			Vec3f globalScale = this.globalScale();
			float radius = this.model.boundingRadius() * Math.max(Math.max(globalScale.x(), globalScale.y()), globalScale.z());
			return this.isInsidePlane(left, globalPosition, radius) &&
				this.isInsidePlane(right, globalPosition, radius) &&
				this.isInsidePlane(top, globalPosition, radius) &&
				this.isInsidePlane(bottom, globalPosition, radius) &&
				this.isInsidePlane(near, globalPosition, radius) &&
				this.isInsidePlane(far, globalPosition, radius);
		}
		return false;
	}

	/**
	 * Checks if the signed distance between the given plane and the given point is greater than the negative radius.
	 * Used to check if a sphere is inside the frustum.
	 *
	 * @param plane The plane to check
	 * @param point The point to check
	 * @param radius The radius of the sphere
	 * @return True if the sphere is "inside" the plane, otherwise false
	 */
	private boolean isInsidePlane(Vec4f plane, Vec3f point, float radius) {
		double signedDistance = (plane.x() * point.x() + plane.y() * point.y() + plane.z() * point.z() + plane.w()) / Math.sqrt(plane.x() * plane.x() + plane.y() * plane.y() + plane.z() * plane.z());
		return signedDistance > -radius;
	}
}
