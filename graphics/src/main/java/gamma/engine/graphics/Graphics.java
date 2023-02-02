package gamma.engine.graphics;

import org.lwjgl.opengl.GL11;

/**
 * Static class containing wrappers for some OpenGL functions that works as a "graphics settings" class.
 *
 * @author Nico
 */
public final class Graphics {

	// TODO: Make sure OpenGL context is current on the calling thread when GL* functions are called

	/**
	 * Sets the clear color. See {@link GL11#glClearColor(float, float, float, float)}.
	 *
	 * @param red Red RGBA component
	 * @param green Green RGBA component
	 * @param blue Blue RGBA component
	 * @param alpha Alpha RGBA component
	 */
	public static void clearColor(float red, float green, float blue, float alpha) {
		GL11.glClearColor(red, green, blue, alpha);
	}

	/**
	 * Sets the clear color. See {@link GL11#glClearColor(float, float, float, float)}.
	 *
	 * @param red Red RGB component
	 * @param green Green RGB component
	 * @param blue Blue RGB component
	 */
	public static void clearColor(float red, float green, float blue) {
		clearColor(red, green, blue, 1.0f);
	}

	/**
	 * Clears the frame buffer. See {@link GL11#glClear(int)}.
	 */
	public static void clearFramebuffer() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	/**
	 * Sets the OpenGL viewport. See {@link GL11#glViewport(int, int, int, int)}.
	 *
	 * @param width Viewport width
	 * @param height Viewport height
	 */
	public static void setViewport(int width, int height) {
		GL11.glViewport(0, 0, width, height);
	}

	/**
	 * Enables/disables depth test. When depth test is enabled, OpenGL will render 3D objects at the correct depth level.
	 *
	 * @param enable True to enable depth testing, false to disable it.
	 */
	public static void depthTest(boolean enable) {
		if(enable) GL11.glEnable(GL11.GL_DEPTH_TEST); else GL11.glDisable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Enables/disables back face culling. When back face culling is enabled, OpenGL will not render faces of meshes that are not visible.
	 *
	 * @param enable True to enable back face culling, false to disable it.
	 */
	public static void backFaceCulling(boolean enable) {
		if(enable) GL11.glEnable(GL11.GL_CULL_FACE); else GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
}
