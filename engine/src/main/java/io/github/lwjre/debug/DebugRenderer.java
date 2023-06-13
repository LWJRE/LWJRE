package io.github.lwjre.debug;

import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.hexagonnico.vecmatlib.matrix.Mat4f;
import io.github.lwjre.resources.Mesh;
import io.github.lwjre.resources.Shader;
import io.github.lwjre.resources.ShaderCompilationException;
import io.github.lwjre.utils.FileUtils;

import java.util.HashMap;
import java.util.HashSet;

public final class DebugRenderer {

	private static final HashMap<Mesh, HashSet<Runnable>> RENDER_BATCH = new HashMap<>();
	private static final Shader SHADER;

	static {
		try {
			String debugVertex = FileUtils.readResourceAsString("io/github/lwjre/shaders/debug_vertex.glsl");
			String debugFragment = FileUtils.readResourceAsString("io/github/lwjre/shaders/debug_fragment.glsl");
			SHADER = new Shader(debugVertex, debugFragment);
		} catch (ShaderCompilationException e) {
			throw new RuntimeException(e);
		}
	}

	public static void drawCube(Mat4f transform, Color4f color) {
		drawMesh(CubeMesh.INSTANCE, transform, color);
	}

	public static void drawCube(Mat4f transform, float red, float green, float blue, float alpha) {
		drawCube(transform, new Color4f(red, green, blue, alpha));
	}

	public static void drawCube(Mat4f transform, float red, float green, float blue) {
		drawCube(transform, red, green, blue, 1.0f);
	}

	public static void drawCone(Mat4f transform, Color4f color) {
		drawMesh(ConeMesh.INSTANCE, transform, color);
	}

	public static void drawCone(Mat4f transform, float red, float green, float blue, float alpha) {
		drawCone(transform, new Color4f(red, green, blue, alpha));
	}

	public static void drawCone(Mat4f transform, float red, float green, float blue) {
		drawCone(transform, red, green, blue, 1.0f);
	}

	public static void drawMesh(Mesh mesh, Mat4f transform, Color4f color) {
		if(!RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.put(mesh, new HashSet<>());
		}
		RENDER_BATCH.get(mesh).add(() -> {
			SHADER.setUniform("transformation_matrix", transform);
			SHADER.setUniform("color", color);
			mesh.draw();
		});
	}

	public static void render() {
		SHADER.start();
		RENDER_BATCH.forEach((mesh, batch) -> batch.forEach(Runnable::run));
		RENDER_BATCH.clear();
	}
}
