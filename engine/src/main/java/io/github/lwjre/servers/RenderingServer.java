package io.github.lwjre.servers;

import io.github.hexagonnico.vecmatlib.color.Color3f;
import io.github.hexagonnico.vecmatlib.color.Color4f;
import io.github.lwjre.ApplicationProperties;
import io.github.lwjre.nodes.PointLight3D;
import io.github.lwjre.nodes.Renderer3D;
import io.github.lwjre.resources.GLResource;
import io.github.lwjre.resources.Mesh;
import io.github.lwjre.resources.Shader;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.HashSet;

public class RenderingServer implements EngineServer {

	/** Batch of {@link Renderer3D}s */
	private static final HashMap<Mesh, HashSet<Renderer3D>> RENDER_BATCH = new HashMap<>();

	/** Batch of {@link PointLight3D}s */
	private static final HashSet<PointLight3D> LIGHTS = new HashSet<>();

	@Override
	public void init() {
		GL.createCapabilities();
		setClearColor(ApplicationProperties.get("rendering.options.defaultClearColor", new Color4f(0.3f, 0.3f, 0.3f, 1.0f)));
		deptTest(ApplicationProperties.get("rendering.options.depthTest", true));
		backFaceCulling(ApplicationProperties.get("rendering.options.backFaceCulling", true));
		lineWidth(ApplicationProperties.get("rendering.options.lineWidth", 1.0f));
		fillPolygons(ApplicationProperties.get("rendering.options.fillPolygons", true));
		blend(ApplicationProperties.get("rendering.options.blend", false));
	}

	@Override
	public void update() {
		clearScreen();
		int i = 0;
		for(PointLight3D light : LIGHTS) {
			if(i == ApplicationProperties.get("rendering.shaders.lights", LIGHTS.size()))
				break;
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

	@Override
	public void terminate() {
		GLResource.deleteAll();
	}

	/**
	 * Adds a {@link Mesh} to the render batch.
	 * Meshes are drawn in batches for a more efficient rendering.
	 *
	 * @param mesh The mesh to add to the batch
	 * @param renderer The renderer node that needs to render this mesh
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
	 * Lights need to loaded in all shaders before the debug process starts.
	 *
	 * @param light The light to add to the batch
	 */
	public static void addToBatch(PointLight3D light) {
		LIGHTS.add(light);
	}

	/**
	 * Removes a {@link Mesh} from the render batch.
	 *
	 * @param mesh The mesh to remove
	 * @param renderer The renderer that draws the mesh
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
	 * Clears the screen.
	 * Called every frame to clear everything that was rendered during the previous frame.
	 */
	public static void clearScreen() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Sets the color to use when calling {@link RenderingServer#clearScreen()}.
	 *
	 * @see GL11#glClearColor(float, float, float, float)
	 *
	 * @param red The red value of the color
	 * @param green The green value of the color
	 * @param blue The blue value of the color
	 */
	public static void setClearColor(float red, float green, float blue) {
		setClearColor(red, green, blue, 1.0f);
	}

	/**
	 * Sets the color to use when calling {@link RenderingServer#clearScreen()}.
	 *
	 * @see GL11#glClearColor(float, float, float, float)
	 *
	 * @param color The color to use
	 */
	public static void setClearColor(Color3f color) {
		setClearColor(color.r(), color.g(), color.b());
	}

	/**
	 * Sets the color to use when calling {@link RenderingServer#clearScreen()}.
	 *
	 * @see GL11#glClearColor(float, float, float, float)
	 *
	 * @param red The red value of the color
	 * @param green The green value of the color
	 * @param blue The blue value of the color
	 * @param alpha The alpha value of the color
	 */
	public static void setClearColor(float red, float green, float blue, float alpha) {
		GL11.glClearColor(red, green, blue, alpha);
	}

	/**
	 * Sets the color to use when calling {@link RenderingServer#clearScreen()}.
	 *
	 * @see GL11#glClearColor(float, float, float, float)
	 *
	 * @param color The color to use
	 */
	public static void setClearColor(Color4f color) {
		setClearColor(color.r(), color.g(), color.b(), color.a());
	}

	/**
	 * Enables or disables depth test.
	 * Depth test draws object that are further from the camera behind those that are closer.
	 *
	 * @see <a href="https://www.khronos.org/opengl/wiki/Depth_Test">OpenGL wiki</a>
	 *
	 * @param enable True to enable, false to disable
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
	 * When enabled, faces of models that are not visible will not be rendered.
	 *
	 * @see <a href="https://www.khronos.org/opengl/wiki/Face_Culling">OpenGL wiki</a>
	 *
	 * @param enable True to enable, false to disable
	 */
	public static void backFaceCulling(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		} else {
			GL11.glDisable(GL11.GL_CULL_FACE);
		}
	}

	/**
	 * Sets the width of rendered line segments.
	 *
	 * @see GL11#glLineWidth(float)
	 *
	 * @param width Width of the line
	 */
	public static void lineWidth(float width) {
		GL11.glLineWidth(width);
	}

	/**
	 * Enables or disables polygon filling.
	 * When enabled, faces of polygons will be filled.
	 * When disabled, only the outline of polygons will be drawn.
	 *
	 * @see GL11#glPolygonMode(int, int)
	 *
	 * @param fill True to set fill mode to {@link GL11#GL_FILL}, false to set fill mode to {@link GL11#GL_LINE}
	 */
	public static void fillPolygons(boolean fill) {
		if(fill) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		}
	}

	/**
	 * Enables or disables blending.
	 * Allows transparent object to blend with objects behind them.
	 *
	 * @see <a href="https://www.khronos.org/opengl/wiki/Blending">OpenGL wiki</a>
	 *
	 * @param enable True to enable blend, false to disable it
	 */
	public static void blend(boolean enable) {
		if(enable) {
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		} else {
			GL11.glDisable(GL11.GL_BLEND);
		}
	}

	/**
	 * Resizes the viewport.
	 * Called when the {@link DisplayServer#window()} is resized.
	 * Changes the viewport to keep the same aspect ratio if {@code rendering.viewport.scale} is enabled.
	 *
	 * @param width New window width
	 * @param height New window height
	 */
	public static void resizeViewport(int width, int height) {
		if(ApplicationProperties.get("rendering.viewport.scale", true)) {
			float targetWidth = ApplicationProperties.get("window.viewport.width", 400);
			float targetHeight = ApplicationProperties.get("window.viewport.height", 300);
			float aspectWidth = width;
			float aspectHeight = width / (targetWidth / targetHeight);
			if(aspectHeight > height) {
				aspectHeight = height;
				aspectWidth = aspectHeight * (targetWidth / targetHeight);
			}
			float viewportX = (width / 2.0f) - (aspectWidth / 2.0f);
			float viewportY = (height / 2.0f) - (aspectHeight / 2.0f);
			setViewport((int) viewportX, (int) viewportY, (int) aspectWidth, (int) aspectHeight);
		} else {
			setViewport(width, height);
		}
	}

	/**
	 * Sets the viewport to the given size.
	 *
	 * @see GL11#glViewport(int, int, int, int)
	 *
	 * @param width New viewport width
	 * @param height New viewport height
	 */
	public static void setViewport(int width, int height) {
		setViewport(0, 0, width, height);
	}

	/**
	 * Sets the viewport to the given value.
	 *
	 * @see GL11#glViewport(int, int, int, int)
	 *
	 * @param x New viewport origin x
	 * @param y New viewport origin y
	 * @param width New viewport width
	 * @param height New viewport height
	 */
	public static void setViewport(int x, int y, int width, int height) {
		GL11.glViewport(x, y, width, height);
	}
}
