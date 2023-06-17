package io.github.lwjre.engine.nodes;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.hexagonnico.vecmatlib.vector.Vec3f;
import io.github.lwjre.engine.annotations.EditorRange;
import io.github.lwjre.engine.annotations.EditorVariable;
import io.github.lwjre.engine.debug.DebugRenderer;
import io.github.lwjre.engine.servers.RenderingServer;

/**
 * Node that represents an omnidirectional point light in a 3D space, such as a light bulb or a candle.
 *
 * @author Nico
 */
public class PointLight3D extends Node3D {

	/**
	 * The light's ambient color.
	 */
	@EditorVariable(name = "Ambient")
	public Color3f ambient = Color3f.White();

	/**
	 * The light's diffuse color.
	 */
	@EditorVariable(name = "Diffuse")
	public Color3f diffuse = Color3f.White();

	/**
	 * The light's specular color.
	 */
	@EditorVariable(name = "Specular")
	public Color3f specular = Color3f.White();

	/**
	 * Multiplier of the light's colors that represent the intensity of the light.
	 */
	@EditorVariable(name = "Energy")
	@EditorRange(min = 0.0f)
	public float energy = 1.0f;

	/**
	 * A vector containing the light's constant, linear, and quadratic attenuation.
	 */
	@EditorVariable(name = "Attenuation")
	@EditorRange(min = 0.0f)
	public Vec3f attenuation = new Vec3f(1.0f, 0.0f, 0.0f);

	@Override
	protected void onEnter() {
		super.onEnter();
		RenderingServer.addToBatch(this);
	}

	@Override
	protected void onExit() {
		super.onExit();
		RenderingServer.removeFromBatch(this);
	}

	@Override
	protected void onEditorProcess() {
		Mat4f shape = Mat4f.translation(this.globalPosition()).multiply(Mat4f.scaling(this.energy));
		DebugRenderer.drawCube(shape, 1.0f, 0.5f, 0.0f);
		super.onEditorProcess();
	}
}
