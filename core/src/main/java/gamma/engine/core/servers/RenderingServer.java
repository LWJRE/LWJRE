package gamma.engine.core.servers;

import gamma.engine.core.Application;
import gamma.engine.core.ApplicationProperties;
import gamma.engine.core.nodes.PointLight3D;
import gamma.engine.core.nodes.Renderer3D;
import gamma.engine.core.resources.GLResource;
import gamma.engine.core.resources.Mesh;
import gamma.engine.core.resources.Shader;
import gamma.engine.core.window.Window;
import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;

public class RenderingServer implements EngineServer {

	/** Batch of {@link Renderer3D}s */
	private static final HashMap<Mesh, HashSet<Renderer3D>> RENDER_BATCH = new HashMap<>();

	/** Batch of {@link PointLight3D}s */
	private static final HashSet<PointLight3D> LIGHTS = new HashSet<>();

	private Window mainWindow;

	@Override
	public void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		if(GLFW.glfwInit()) {
			// TODO: Window hints
			String windowTitle = ApplicationProperties.get("window.title", "Untitled");
			int windowWidth = ApplicationProperties.get("window.viewport.width", 400);
			int windowHeight = ApplicationProperties.get("window.viewport.height", 300);
			this.mainWindow = new Window(windowTitle, windowWidth, windowHeight);
			this.mainWindow.makeCurrent();
			this.mainWindow.setVisible(true);
			GL.createCapabilities(); // TODO: Should this be in another class?
			// TODO: Window callbacks
		} else {
			throw new RuntimeException("Unable to initialize GLFW");
		}
		// TODO: Application settings
		setClearColor(0.0f, 0.5f, 1.0f);
		deptTest(true);
		backFaceCulling(true);
	}

	@Override
	public void update() {
		if(this.mainWindow.shouldClose()) {
			Application.quit();
		} else {
			this.mainWindow.update();
			clearScreen();
			render();
			GLFW.glfwPollEvents();
		}
	}

	@Override
	public void terminate() {
		GLResource.deleteAll();
		this.mainWindow.destroy();
		GLFW.glfwTerminate();
		GLFW.glfwSetErrorCallback(null).free();
	}

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
	 * Called every frame.
	 * Clears the screen from everything that was rendered on the previous frame and starts the rendering process.
	 */
	private static void render() {
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

	public static void clearScreen() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
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

	public static void lineWidth(float width) {
		GL11.glLineWidth(width);
	}

	public static void fillPolygons(boolean fill) {
		if(fill) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		}
	}

	public static void setViewport(int width, int height) {
		setViewport(0, 0, width, height);
	}

	public static void setViewport(int x, int y, int width, int height) {
		GL11.glViewport(x, y, width, height);
	}
}
