package io.github.view.graphics;

import io.github.view.math.Vector3;
import org.lwjgl.opengl.GL11;

public final class Debug {

	public static void drawQuads(Vector3... vertices) {
		int previousMode = GL11.glGetInteger(GL11.GL_POLYGON_MODE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL11.glBegin(GL11.GL_QUADS);
		for(Vector3 vertex : vertices) {
			GL11.glVertex3f(vertex.x(), vertex.y(), vertex.z());
		}
		GL11.glEnd();
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, previousMode);
	}
}
