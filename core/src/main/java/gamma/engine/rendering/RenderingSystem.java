package gamma.engine.rendering;

import gamma.engine.tree.PointLight3D;
import gamma.engine.tree.Renderer3D;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import vecmatlib.color.Color3f;
import vecmatlib.color.Color4f;
import vecmatlib.vector.Vec2f;
import vecmatlib.vector.Vec2i;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Static class used to render objects in batches.
 * Objects that can be rendered must be added to the render batch when they enter the scene.
 *
 * @author Nico
 */
public final class RenderingSystem {

	/** Batch of {@link Renderer3D}s */
	private static final HashMap<Mesh, HashSet<Renderer3D>> RENDER_BATCH = new HashMap<>();

	/** Batch of {@link PointLight3D}s */
	private static final HashSet<PointLight3D> LIGHTS = new HashSet<>();

	/**
	 * Adds an object to the batch.
	 * Meshes are rendered in batches to improve performance.
	 * The next {@link RenderingSystem#render()} call will call {@link Renderer3D#render(Mesh)} with the given {@link Mesh}.
	 *
	 * @param mesh The mesh to render
	 * @param renderer The renderer object
	 */
	public static void addToBatch(Mesh mesh, Renderer3D renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).add(renderer);
		} else {
			HashSet<Renderer3D> batch = new HashSet<>();
			batch.add(renderer);
			RENDER_BATCH.put(mesh, batch);
		}
	}

	/**
	 * Adds a light to the batch.
	 * Lights need to loaded in all shaders before the rendering process starts.
	 *
	 * @param light The light to add to the batch
	 */
	public static void addToBatch(PointLight3D light) {
		LIGHTS.add(light);
	}

	/**
	 * Removes the given {@link Renderer3D} from the batch that corresponds to the given {@link Mesh}.
	 * This method is the opposite of {@link RenderingSystem#addToBatch(Mesh, Renderer3D)}.
	 *
	 * @param mesh The mesh that correspond to said batch
	 * @param renderer The renderer object to remove
	 */
	public static void removeFromBatch(Mesh mesh, Renderer3D renderer) {
		if(RENDER_BATCH.containsKey(mesh)) {
			RENDER_BATCH.get(mesh).remove(renderer);
		}
	}

	/**
	 * Removes the given {@link Renderer3D} from all batches.
	 *
	 * @param renderer The renderer object to remove
	 */
	public static void removeFromBatch(Renderer3D renderer) {
		RENDER_BATCH.values().forEach(batch -> batch.remove(renderer));
	}

	/**
	 * Removes a light from the batch.
	 *
	 * @param light The light to remove
	 */
	public static void removeFromBatch(PointLight3D light) {
		LIGHTS.remove(light);
	}

	/**
	 * Initializes the {@code RenderingSystem} by making the OpenGL context current in the calling thread.
	 * Called when the {@link gamma.engine.Application} starts.
	 *
	 * @see GL#createCapabilities()
	 */
	public static void init() {
		GL.createCapabilities();
		// TODO: Give default options
		setClearColor(0.0f, 0.5f, 1.0f);
		deptTest(true);
		backFaceCulling(true);
	}

	/**
	 * Called every frame.
	 * Clears the screen from everything that was rendered on the previous frame and starts the rendering process.
	 */
	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glLineWidth(1.0f);
		int i = 0;
		for(PointLight3D light : LIGHTS) {
			Shader.setUniformStatic("point_lights[" + i + "].position", light.globalPosition());
			Shader.setUniformStatic("point_lights[" + i + "].ambient", light.ambient.multiply(light.energy));
			Shader.setUniformStatic("point_lights[" + i + "].diffuse", light.diffuse.multiply(light.energy));
			Shader.setUniformStatic("point_lights[" + i + "].specular", light.specular.multiply(light.energy));
			Shader.setUniformStatic("point_lights[" + i + "].attenuation", light.attenuation);
			i++;
		}
		Shader.setUniformStatic("lights_count", i);
		RENDER_BATCH.forEach((mesh, batch) -> {
			mesh.bind();
			batch.forEach(renderer -> renderer.render(mesh));
		});
	}

	/**
	 * Called when the window is resized.
	 * Adjusts the OpenGL viewport.
	 *
	 * @see GL11#glViewport(int, int, int, int)
	 *
	 * @param width Width of the new viewport
	 * @param height Height of the new viewport
	 */
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

	/**
	 * Sets the color to use when the screen is cleared at the start of {@link RenderingSystem#render()}.
	 *
	 * @param red Red component of the default clear color
	 * @param green Green component of the default clear color
	 * @param blue Blue component of the default clear color
	 */
	public static void setClearColor(float red, float green, float blue) {
		setClearColor(red, green, blue, 1.0f);
	}

	/**
	 * Sets the color to use when the screen is cleared at the start of {@link RenderingSystem#render()}.
	 *
	 * @param color Default clear color
	 */
	public static void setClearColor(Color3f color) {
		setClearColor(color.r(), color.g(), color.b());
	}

	/**
	 * Sets the color to use when the screen is cleared at the start of {@link RenderingSystem#render()}.
	 *
	 * @param red Red component of the default clear color
	 * @param green Green component of the default clear color
	 * @param blue Blue component of the default clear color
	 * @param alpha Alpha component of the default clear color
	 */
	public static void setClearColor(float red, float green, float blue, float alpha) {
		GL11.glClearColor(red, green, blue, alpha);
	}

	/**
	 * Sets the color to use when the screen is cleared at the start of {@link RenderingSystem#render()}.
	 *
	 * @param color Default clear color
	 */
	public static void setClearColor(Color4f color) {
		setClearColor(color.r(), color.g(), color.b(), color.a());
	}

	/**
	 * Enables or disables depth test.
	 *
	 * @param enable True to enable depth test, false to disable it
	 */
	public static void deptTest(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		} else {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		}
	}

	/**
	 * Enables or disables back face culling.
	 *
	 * @param enable True to enable back face culling, false to disable it
	 */
	public static void backFaceCulling(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
	}

	// TODO: Option for polygon mode

	/**
	 * Removes everything from the {@code RenderingSystem}.
	 * Clears the batches.
	 */
	public static void clearRenderer() {
		RENDER_BATCH.clear();
		LIGHTS.clear();
	}

	/**
	 * Cleans up the {@code RenderingSystem}.
	 * Called when the {@link gamma.engine.Application} terminates.
	 *
	 * @see GL#createCapabilities()
	 */
	public static void cleanUp() {
		DeletableResource.deleteAll();
	}
}