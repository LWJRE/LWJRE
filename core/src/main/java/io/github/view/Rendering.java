package io.github.view;

import org.lwjgl.opengl.GL11;

public final class Rendering {

	public static void renderingProcess() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
}
