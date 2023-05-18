package gamma.engine.rendering;

import gamma.engine.tree.PointLight3D;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import vecmatlib.color.Color3f;
import vecmatlib.color.Color4f;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec2i;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public final class RenderingSystem {

	private static final HashMap<Mesh, HashSet<Consumer<Mesh>>> RENDER_BATCH = new HashMap<>();

	private static final HashSet<PointLight3D> LIGHTS = new HashSet<>();

	public static void addToBatch(Mesh mesh, Consumer<Mesh> renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).add(renderer);
		} else {
			HashSet<Consumer<Mesh>> batch = new HashSet<>();
			batch.add(renderer);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	public static void addToBatch(PointLight3D light) {
		LIGHTS.add(light);
	}

	public static void removeFromBatch(Mesh mesh, Consumer<Mesh> renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).remove(renderer);
		}
	}

	public static void removeFromBatch(Consumer<Mesh> renderer) {
		RENDER_BATCH.values().forEach(batch -> batch.remove(renderer));
	}

	public static void removeFromBatch(PointLight3D light) {
		LIGHTS.remove(light);
	}

	public static void init() {
		GL.createCapabilities();
		// TODO: Give default options
		setClearColor(0.0f, 0.5f, 1.0f);
		deptTest(true);
		backFaceCulling(true);
	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		int i = 0;
		for(PointLight3D light : LIGHTS) {
			Shader.setUniformStatic("point_lights[" + i + "].position", light.globalPosition());
			Shader.setUniformStatic("point_lights[" + i + "].ambient", light.color);
			Shader.setUniformStatic("point_lights[" + i + "].diffuse", light.color);
			Shader.setUniformStatic("point_lights[" + i + "].specular", light.color);
			i++;
		}
		Shader.setUniformStatic("lights_count", i);
		RENDER_BATCH.forEach((mesh, batch) -> {
			mesh.bind();
			batch.forEach(renderer -> renderer.accept(mesh));
		});
	}

	public static void resizeViewport(int width, int height) {
		// TODO: Give options for viewport scaling in ApplicationProperties
		Vec2i windowSize = new Vec2i(width, height);
		float aspectWidth = windowSize.x();
		float aspectHeight = aspectWidth / (16.0f / 9.0f);
		if(aspectHeight > windowSize.y()) {
			aspectHeight = windowSize.y();
			aspectWidth = aspectHeight * (16.0f / 9.0f);
		}
		Vec2f viewportSize = new Vec2f(aspectWidth, aspectHeight);
		float viewportX = (windowSize.x() / 2.0f) - (viewportSize.x() / 2.0f);
		float viewportY = (windowSize.y() / 2.0f) - (viewportSize.y() / 2.0f);
		GL11.glViewport((int) viewportX, (int) viewportY, (int) viewportSize.x(), (int) viewportSize.y());
	}

	public static void setClearColor(float red, float green, float blue) {
		setClearColor(red, green, blue, 1.0f);
	}

	public static void setClearColor(Color3f color) {
		setClearColor(color.r(), color.g(), color.b());
	}

	public static void setClearColor(float red, float green, float blue, float alpha) {
		GL11.glClearColor(red, green, blue, alpha);
	}

	public static void setClearColor(Color4f color) {
		setClearColor(color.r(), color.g(), color.b(), color.a());
	}

	public static void deptTest(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
		} else {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}
	}

	public static void backFaceCulling(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
	}

	// TODO: Option for polygon mode

	public static void clearRenderer() {
		RENDER_BATCH.clear();
		LIGHTS.clear();
	}

	public static void cleanUp() {
		DeletableResource.deleteAll();
	}
}